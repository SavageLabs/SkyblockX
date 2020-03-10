package io.illyria.skyblockx.command.island.cmd

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.enumValueOrNull
import io.illyria.skyblockx.persist.Message
import org.bukkit.block.Biome

class CmdBiome : SCommand() {

    init {
        aliases.add("biome")
        aliases.add("setbiome")


        requiredArgs.add(Argument("biome-type", 0, BiomeArgument()))

        commandRequirements = CommandRequirementsBuilder()
            .asIslandMember(true)
            .withPermission(Permission.BIOME)
            .build()
    }


    override fun perform(info: CommandInfo) {
        val biome = enumValueOrNull<Biome>(info.args[0]) ?: run {
            info.message(Message.commandBiomeInvalidBiome)
            return
        }
        info.island?.setBiome(biome)
        info.message(Message.commandBiomeSuccess, biome.name)
    }

    override fun getHelpInfo(): String {
        return Message.commandBiomeHelp
    }
}