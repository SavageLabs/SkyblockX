package net.savagellc.savageskyblock.command.cmd

import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.core.createIsland
import net.savagellc.savageskyblock.persist.Message
import org.bukkit.Material

class CmdGo : SCommand() {


    init {
        aliases.add("go")
        this.optionalArgs.add("player")

        // TODO: Remove later after testing :)
//        this.optionalArgs.add("fill")

        commandRequirements = CommandRequirementsBuilder().asIslandMember(true).asPlayer(true).withPermission(Permission.GO).build()
    }


    override fun perform(commandInfo: CommandInfo) {

        val island = commandInfo.iPlayer!!.getIsland()!!
        commandInfo.message(Message.commandGoTeleporting)
        commandInfo.player!!.teleport(island.getIslandSpawn())
//        if (commandInfo.args.size > 0) {
//            if (commandInfo.args[0] == "fill") {
//                island.fillIsland(Material.valueOf(commandInfo.args[0]))
//            } else if (commandInfo.args[0] == "dummy") {
//                val createIsland = createIsland(null, "island")
//                createIsland.fillIsland(Material.BROWN_WOOL)
//            }
////        }

    }


    override fun getHelpInfo(): String {
        return Message.commandGoHelp
    }

}
