package net.savagelabs.skyblockx.command.island.cmd

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.command.argument.BiomeArgument
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.createIsland
import net.savagelabs.skyblockx.core.enumValueOrNull
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.Bukkit
import org.bukkit.block.Biome

class CmdBiome : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("biome")
        aliases.add("setbiome")


        requiredArgs.add(Argument("biome-type", 0, BiomeArgument()))

        commandRequirements = SCommandRequirementsBuilder()
            .asIslandMember(true)
            .withPermission(Permission.BIOME)
            .build()
    }


    override fun perform(info: SCommandInfo) {
        val biome = enumValueOrNull<Biome>(info.args[0]) ?: run {
            info.message(Message.instance.commandBiomeInvalidBiome)
            return
        }
        info.island?.setBiome(biome)
        info.message(Message.instance.commandBiomeSuccess, biome.name)
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandBiomeHelp
    }
}