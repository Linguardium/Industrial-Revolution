package me.steven.indrev.config

import me.sargunvohra.mcmods.autoconfig1u.ConfigData
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry
import me.sargunvohra.mcmods.autoconfig1u.serializer.PartitioningSerializer
import me.steven.indrev.utils.Tier

@Config(name = "indrev")
class IRConfig : PartitioningSerializer.GlobalData() {

    @ConfigEntry.Category(value = "generators")
    @ConfigEntry.Gui.TransitiveObject
    var generators: Generators = Generators()

    @ConfigEntry.Category(value = "machines")
    @ConfigEntry.Gui.TransitiveObject
    val machines: Machines = Machines()

    @ConfigEntry.Category(value = "cables")
    @ConfigEntry.Gui.TransitiveObject
    val cables: Cables = Cables()

}

@Config(name = "generators")
class Generators : ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    val coalGenerator: GeneratorConfig = GeneratorConfig(16.0, 1.5, 1000.0, Tier.MK1.io)

    @ConfigEntry.Gui.CollapsibleObject
    val solarGeneratorMk1: GeneratorConfig = GeneratorConfig(16.0, 1.5, 500.0, Tier.MK1.io)

    @ConfigEntry.Gui.CollapsibleObject
    val solarGeneratorMk3: GeneratorConfig = GeneratorConfig(64.0, 1.5, 1500.0, Tier.MK3.io)

    @ConfigEntry.Gui.CollapsibleObject
    val heatGenerator: GeneratorConfig = GeneratorConfig(64.0, -1.0, 20000.0, Tier.MK4.io)

    @ConfigEntry.Gui.CollapsibleObject
    val biomassGenerator: GeneratorConfig = GeneratorConfig(128.0, 1.5, 20000.0, Tier.MK3.io)
}

class GeneratorConfig(
    val ratio: Double = 16.0,
    val temperatureBoost: Double = 1.5,
    val maxEnergyStored: Double,
    val maxOutput: Double
)

@Config(name = "machines")
class Machines : ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    val electricFurnaceMk1: HeatMachineConfig = HeatMachineConfig(4.0, 1.0, 2.0, 1000.0, Tier.MK1.io)

    @ConfigEntry.Gui.CollapsibleObject
    val electricFurnaceMk2: HeatMachineConfig = HeatMachineConfig(8.0, 2.0, 2.0, 5000.0, Tier.MK2.io)

    @ConfigEntry.Gui.CollapsibleObject
    val electricFurnaceMk3: HeatMachineConfig = HeatMachineConfig(16.0, 3.0, 2.0, 10000.0, Tier.MK3.io)

    @ConfigEntry.Gui.CollapsibleObject
    val electricFurnaceMk4: HeatMachineConfig = HeatMachineConfig(64.0, 4.0, 2.0, 100000.0, Tier.MK4.io)

    @ConfigEntry.Gui.CollapsibleObject
    val pulverizerMk1: HeatMachineConfig = HeatMachineConfig(4.0, 1.0, 2.0, 1000.0, Tier.MK1.io)

    @ConfigEntry.Gui.CollapsibleObject
    val pulverizerMk2: HeatMachineConfig = HeatMachineConfig(8.0, 2.0, 2.0, 5000.0, Tier.MK2.io)

    @ConfigEntry.Gui.CollapsibleObject
    val pulverizerMk3: HeatMachineConfig = HeatMachineConfig(16.0, 3.0, 2.0, 10000.0, Tier.MK3.io)

    @ConfigEntry.Gui.CollapsibleObject
    val pulverizerMk4: HeatMachineConfig = HeatMachineConfig(64.0, 4.0, 2.0, 100000.0, Tier.MK4.io)

    @ConfigEntry.Gui.CollapsibleObject
    val compressorMk1: HeatMachineConfig = HeatMachineConfig(4.0, 1.0, 2.0, 1000.0, Tier.MK1.io)

    @ConfigEntry.Gui.CollapsibleObject
    val compressorMk2: HeatMachineConfig = HeatMachineConfig(8.0, 2.0, 2.0, 5000.0, Tier.MK2.io)

    @ConfigEntry.Gui.CollapsibleObject
    val compressorMk3: HeatMachineConfig = HeatMachineConfig(16.0, 3.0, 2.0, 10000.0, Tier.MK3.io)

    @ConfigEntry.Gui.CollapsibleObject
    val compressorMk4: HeatMachineConfig = HeatMachineConfig(64.0, 4.0, 2.0, 100000.0, Tier.MK4.io)

    @ConfigEntry.Gui.CollapsibleObject
    val infuserMk1: MachineConfig = MachineConfig(4.0, 1.0, 1000.0, Tier.MK1.io)

    @ConfigEntry.Gui.CollapsibleObject
    val infuserMk2: MachineConfig = MachineConfig(8.0, 2.0, 5000.0, Tier.MK2.io)

    @ConfigEntry.Gui.CollapsibleObject
    val infuserMk3: MachineConfig = MachineConfig(16.0, 3.0, 10000.0, Tier.MK3.io)

    @ConfigEntry.Gui.CollapsibleObject
    val infuserMk4: MachineConfig = MachineConfig(64.0, 4.0, 100000.0, Tier.MK4.io)

    @ConfigEntry.Gui.CollapsibleObject
    val recycler: MachineConfig = MachineConfig(8.0, 2.0, 50000.0, Tier.MK2.io)

    @ConfigEntry.Gui.CollapsibleObject
    val chopper: MachineConfig = MachineConfig(64.0, 3.0, 50000.0, Tier.MK4.io)

    @ConfigEntry.Gui.CollapsibleObject
    val rancher: MachineConfig = MachineConfig(64.0, 3.0, 50000.0, Tier.MK4.io)

    @ConfigEntry.Gui.CollapsibleObject
    val miner: MachineConfig = MachineConfig(64.0, 0.3, 50000.0, Tier.MK4.io)
}

class HeatMachineConfig(
    energyCost: Double,
    processSpeed: Double,
    val processTemperatureBoost: Double,
    maxEnergyStored: Double,
    maxInput: Double
) : MachineConfig(energyCost, processSpeed, maxEnergyStored, maxInput)

open class MachineConfig(
    val energyCost: Double,
    val processSpeed: Double,
    val maxEnergyStored: Double,
    val maxInput: Double
)

@Config(name = "cables")
class Cables : ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    val cableMk1 = CableConfig(Tier.MK1.io, Tier.MK1.io)

    @ConfigEntry.Gui.CollapsibleObject
    val cableMk2 = CableConfig(Tier.MK2.io, Tier.MK2.io)

    @ConfigEntry.Gui.CollapsibleObject
    val cableMk3 = CableConfig(Tier.MK3.io, Tier.MK3.io)

    @ConfigEntry.Gui.CollapsibleObject
    val cableMk4 = CableConfig(Tier.MK4.io, Tier.MK4.io)
}

class CableConfig(val maxOutput: Double, val maxInput: Double)