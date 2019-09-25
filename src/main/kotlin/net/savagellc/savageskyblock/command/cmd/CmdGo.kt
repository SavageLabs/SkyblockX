package net.savagellc.savageskyblock.command.cmd

import net.savagellc.savageskyblock.command.info
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.persist.Message

class CmdGo : SCommand() {


    init {
        aliases.add("go")
        this.optionalArgs.add("player")

        // TODO: Remove later after testing :)
//        this.optionalArgs.add("fill")

        commandRequirements = CommandRequirementsBuilder().asIslandMember(true).asPlayer(true).withPermission(Permission.GO).build()
    }


    override fun perform(info: info) {

        val island = info.iPlayer!!.getIsland()!!
        info.message(Message.commandGoTeleporting)
        info.player!!.teleport(island.getIslandSpawn())
//        if (info.args.size > 0) {
//            if (info.args[0] == "fill") {
//                island.fillIsland(Material.valueOf(info.args[0]))
//            } else if (info.args[0] == "dummy") {
//                val createIsland = createIsland(null, "island")
//                createIsland.fillIsland(Material.BROWN_WOOL)
//            }
////        }

    }


    override fun getHelpInfo(): String {
        return Message.commandGoHelp
    }

}
