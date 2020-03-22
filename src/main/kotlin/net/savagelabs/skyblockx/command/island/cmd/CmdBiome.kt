package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.enumValueOrNull
import net.savagelabs.skyblockx.persist.Message
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