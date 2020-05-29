package me.steven.indrev.blockentities.crafters

import me.steven.indrev.inventories.DefaultSidedInventory
import me.steven.indrev.items.Upgrade
import me.steven.indrev.items.UpgradeItem
import me.steven.indrev.registry.MachineRegistry
import me.steven.indrev.utils.Tier
import net.minecraft.inventory.BasicInventory
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.SmeltingRecipe

class ElectricFurnaceBlockEntity(tier: Tier) :
    CraftingMachineBlockEntity<SmeltingRecipe>(MachineRegistry.ELECTRIC_FURNACE_BLOCK_ENTITY, tier, 250.0) {
    private var currentRecipe: SmeltingRecipe? = null
    override fun tryStartRecipe(inventory: DefaultSidedInventory): SmeltingRecipe? {
        val inputStacks = BasicInventory(*(inventory.inputSlots).map { inventory.getInvStack(it) }.toTypedArray())
        val optional = world?.recipeManager?.getFirstMatch(RecipeType.SMELTING, inputStacks, world)
        val recipe = optional?.orElse(null) ?: return null
        val outputStack = inventory.getInvStack(1).copy()
        if (outputStack.isEmpty || (outputStack.count + recipe.output.count < outputStack.maxCount && outputStack.item == recipe.output.item)) {
            if (!isProcessing() && recipe.matches(inputStacks, this.world)) {
                processTime = recipe.cookTime
                totalProcessTime = recipe.cookTime
            }
            this.currentRecipe = recipe
        }
        return recipe
    }

    override fun createInventory(): DefaultSidedInventory = DefaultSidedInventory(6, intArrayOf(0), intArrayOf(1)) { slot, stack ->
        if (stack?.item is UpgradeItem) getUpgradeSlots().contains(slot) else true
    }

    override fun getUpgradeSlots(): IntArray = intArrayOf(2, 3, 4, 5)

    override fun getAvailableUpgrades(): Array<Upgrade> = Upgrade.ALL

    override fun getCurrentRecipe(): SmeltingRecipe? = currentRecipe
}