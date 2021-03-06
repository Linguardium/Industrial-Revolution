package me.steven.indrev.blockentities.battery

import me.steven.indrev.blockentities.MachineBlockEntity
import me.steven.indrev.components.InventoryController
import me.steven.indrev.inventories.IRInventory
import me.steven.indrev.registry.MachineRegistry
import me.steven.indrev.utils.Tier
import team.reborn.energy.Energy

class ChargePadBlockEntity(tier: Tier) : MachineBlockEntity(tier, MachineRegistry.CHARGE_PAD_REGISTRY) {
    init {
        this.inventoryController = InventoryController {
            IRInventory(1, intArrayOf(0), intArrayOf(0)) { _, stack -> Energy.valid(stack) }
        }
    }

    override fun machineTick() {
        if (world?.isClient == true) return
        val inventory = inventoryController?.inventory ?: return
        val stack = inventory.getStack(0)
        if (Energy.valid(stack)) {
            setWorkingState(true)
            Energy.of(this).into(Energy.of(stack)).move()
        } else setWorkingState(false)
    }
}