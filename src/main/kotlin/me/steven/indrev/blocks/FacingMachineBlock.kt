package me.steven.indrev.blocks

import me.steven.indrev.blockentities.MachineBlockEntity
import me.steven.indrev.components.InventoryController
import me.steven.indrev.utils.Tier
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

open class FacingMachineBlock(
    settings: Settings,
    tier: Tier,
    screenHandler: ((Int, PlayerInventory, ScreenHandlerContext) -> ScreenHandler)?,
    blockEntityProvider: () -> MachineBlockEntity
) : MachineBlock(settings, tier, screenHandler, blockEntityProvider) {

    override fun getPlacementState(ctx: ItemPlacementContext?): BlockState? {
        super.getPlacementState(ctx)
        return this.defaultState.with(HORIZONTAL_FACING, ctx?.playerFacing?.opposite)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        super.appendProperties(builder)
        builder?.add(HORIZONTAL_FACING)
    }

    override fun onPlaced(world: World?, pos: BlockPos?, state: BlockState, placer: LivingEntity?, itemStack: ItemStack?) {
        val blockEntity = world?.getBlockEntity(pos)
        if (blockEntity is MachineBlockEntity) {
            val direction = state[HORIZONTAL_FACING]
            val inventoryController = blockEntity.inventoryController ?: return
            val itemConfig = inventoryController.itemConfig
            itemConfig[direction.rotateYClockwise()] = InventoryController.Mode.INPUT
            itemConfig[direction.rotateYCounterclockwise()] = InventoryController.Mode.OUTPUT
        }
        super.onPlaced(world, pos, state, placer, itemStack)
    }

    companion object {
        val HORIZONTAL_FACING: DirectionProperty = Properties.HORIZONTAL_FACING
    }
}