package me.steven.indrev.blockentities.crafters

import me.steven.indrev.blockentities.InterfacedMachineBlockEntity
import me.steven.indrev.inventories.DefaultSidedInventory
import me.steven.indrev.items.Upgrade
import me.steven.indrev.utils.Tier
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.container.ArrayPropertyDelegate
import net.minecraft.container.PropertyDelegate
import net.minecraft.inventory.BasicInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SidedInventory
import net.minecraft.nbt.CompoundTag
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeFinder
import net.minecraft.recipe.RecipeInputProvider
import net.minecraft.util.Tickable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import team.reborn.energy.EnergySide
import kotlin.math.ceil

abstract class CraftingMachineBlockEntity<T : Recipe<Inventory>>(
        type: BlockEntityType<*>,
        tier: Tier,
        baseBuffer: Double
) :
        InterfacedMachineBlockEntity(type, tier, baseBuffer), Tickable, RecipeInputProvider, UpgradeProvider {
    var inventory: DefaultSidedInventory? = null
        get() = field ?: createInventory().apply { field = this }
    var processTime: Int = 0
        set(value) {
            field = value.apply { propertyDelegate[2] = this }
        }
        get() = field.apply { propertyDelegate[2] = this }
    var totalProcessTime: Int = 0
        set(value) {
            field = value.apply { propertyDelegate[3] = this }
        }
        get() = field.apply { propertyDelegate[3] = this }

    override fun tick() {
        super.tick()
        if (world?.isClient == true) return
        val inputInventory = BasicInventory(*(inventory!!.inputSlots).map { inventory!!.getInvStack(it) }.toTypedArray())
        val outputStack = inventory!!.getInvStack(1).copy()
        if (isProcessing()) {
            val recipe = getCurrentRecipe()
            if (inputInventory.isInvEmpty) reset()
            else if (recipe?.matches(inputInventory, this.world) == false) tryStartRecipe(inventory!!) ?: reset()
            else if (takeEnergy(Upgrade.ENERGY.apply(this, inventory!!))) {
                processTime = (processTime - ceil(Upgrade.SPEED.apply(this, inventory!!)).toInt()).coerceAtLeast(0)
                if (processTime <= 0) {
                    (inventory!!.inputSlots).forEachIndexed { index, slot -> inventory!!.setInvStack(slot, inputInventory.getInvStack(index).apply { count-- }) }
                    val output = recipe?.output ?: return
                    if (outputStack.item == output.item)
                        inventory!!.setInvStack(1, outputStack.apply { increment(output.count) })
                    else if (outputStack.isEmpty)
                        inventory!!.setInvStack(1, output.copy())
                    onCraft()
                    reset()
                }
            } else reset()
        } else if (energy > 0 && !inputInventory.isInvEmpty && processTime <= 0) {
            reset()
            tryStartRecipe(inventory!!)
        }
        markDirty()
    }

    abstract fun tryStartRecipe(inventory: DefaultSidedInventory): T?

    abstract fun getCurrentRecipe(): T?

    abstract fun createInventory(): DefaultSidedInventory

    private fun reset() {
        processTime = 0
        totalProcessTime = 0
    }

    override fun getMaxStoredPower(): Double = Upgrade.BUFFER.apply(this, inventory!!)

    override fun createDelegate(): PropertyDelegate = ArrayPropertyDelegate(4)

    override fun getMaxOutput(side: EnergySide?): Double = 0.0

    fun isProcessing() = processTime > 0 && energy > 0

    override fun getBaseValue(upgrade: Upgrade): Double = when (upgrade) {
        Upgrade.ENERGY -> 1.0 * Upgrade.SPEED.apply(this, inventory!!)
        Upgrade.SPEED -> 1.0
        Upgrade.BUFFER -> baseBuffer
    }

    override fun fromTag(tag: CompoundTag?) {
        processTime = tag?.getInt("ProcessTime") ?: 0
        totalProcessTime = tag?.getInt("MaxProcessTime") ?: 0
        super.fromTag(tag)
    }

    override fun toTag(tag: CompoundTag?): CompoundTag {
        tag?.putInt("ProcessTime", processTime)
        tag?.putInt("MaxProcessTime", totalProcessTime)
        return super.toTag(tag)
    }

    override fun fromClientTag(tag: CompoundTag?) {
        processTime = tag?.getInt("ProcessTime") ?: 0
        totalProcessTime = tag?.getInt("MaxProcessTime") ?: 0
        super.fromClientTag(tag)
    }

    override fun toClientTag(tag: CompoundTag?): CompoundTag {
        tag?.putInt("ProcessTime", processTime)
        tag?.putInt("MaxProcessTime", totalProcessTime)
        return super.toClientTag(tag)
    }

    override fun getInventory(state: BlockState?, world: IWorld?, pos: BlockPos?): SidedInventory = inventory!!

    override fun provideRecipeInputs(recipeFinder: RecipeFinder?) {
        for (i in 0 until inventory!!.invSize)
            recipeFinder?.addItem(inventory!!.getInvStack(i))
    }

    open fun onCraft() {}
}