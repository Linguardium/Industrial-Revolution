package me.steven.indrev.items

import me.steven.indrev.items.rechargeable.IRRechargeable
import me.steven.indrev.utils.Tier
import me.steven.indrev.utils.getShortEnergyDisplay
import net.minecraft.block.BlockState
import net.minecraft.block.Material
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.PickaxeItem
import net.minecraft.item.ToolMaterial
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import team.reborn.energy.Energy
import team.reborn.energy.EnergyHolder
import team.reborn.energy.EnergySide
import team.reborn.energy.EnergyTier

class IRMiningDrill(
    toolMaterial: ToolMaterial,
    private val tier: Tier,
    private val maxStored: Double,
    settings: Settings
) :
    PickaxeItem(toolMaterial, 0, 0F, settings),
    EnergyHolder, IRRechargeable {
    override fun getMiningSpeedMultiplier(stack: ItemStack, state: BlockState?): Float {
        val material = state?.material
        return if (SUPPORTED_MATERIALS.contains(material) && Energy.of(stack).energy > 0) this.material.miningSpeedMultiplier * 2 else 0F
    }

    override fun postMine(
        stack: ItemStack,
        world: World?,
        state: BlockState?,
        pos: BlockPos?,
        miner: LivingEntity?
    ): Boolean = Energy.of(stack).use(1.0)

    override fun postHit(stack: ItemStack?, target: LivingEntity?, attacker: LivingEntity?): Boolean = Energy.of(stack).use(2.0)

    override fun canMine(state: BlockState?, world: World?, pos: BlockPos?, miner: PlayerEntity?): Boolean {
        val stack = miner?.mainHandStack ?: return super.canMine(state, world, pos, miner)
        return super.canMine(state, world, pos, miner) && Energy.of(stack).energy > 0
    }

    override fun appendTooltip(
        stack: ItemStack?,
        world: World?,
        tooltip: MutableList<Text>?,
        context: TooltipContext?
    ) {
        super.appendTooltip(stack, world, tooltip, context)
        val handler = Energy.of(stack)
        tooltip?.add(TranslatableText("gui.widget.energy").formatted(Formatting.BLUE))
        tooltip?.add(LiteralText("${getShortEnergyDisplay(handler.energy)} / ${getShortEnergyDisplay(handler.maxStored)} LF"))
        tooltip?.add(TranslatableText("item.indrev.rechargeable.tooltip").formatted(Formatting.ITALIC, Formatting.GRAY))
    }

    override fun canRepair(stack: ItemStack?, ingredient: ItemStack?): Boolean = false

    override fun getMaxStoredPower(): Double = maxStored

    override fun getMaxInput(side: EnergySide?): Double = tier.io

    override fun getMaxOutput(side: EnergySide?): Double = 0.0

    override fun getTier(): EnergyTier = EnergyTier.HIGH

    override fun inventoryTick(stack: ItemStack, world: World?, entity: Entity?, slot: Int, selected: Boolean) {
        val handler = Energy.of(stack)
        stack.damage = (stack.maxDamage - handler.energy.toInt()).coerceAtLeast(1)
    }

    companion object {
        private val SUPPORTED_MATERIALS = arrayOf(
            Material.METAL,
            Material.STONE,
            Material.WOOD,
            Material.BAMBOO,
            Material.COBWEB,
            Material.PISTON,
            Material.GOURD,
            Material.SOIL
        )
    }
}