package net.savagellc.savageskyblock.command.cmd

import net.savagellc.savageskyblock.command.info
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.core.createIsland
import net.savagellc.savageskyblock.persist.Message


class CmdCreate : SCommand() {

    init {
        aliases.add("create")

        commandRequirements = CommandRequirementsBuilder().asIslandMember(false).asPlayer(true).withPermission(Permission.CREATE).build()
    }

    override fun perform(info: info) {
        if (info.iPlayer!!.hasIsland()) {
            info.message(Message.commandCreateAlreadyHaveAnIsland)
            return
        }
        info.message("Creating island and filling it.")
        val island = createIsland(info.player!!, "island")
        info.message("Success.")

    }


    override fun getHelpInfo(): String {
       return Message.commandCreateHelp
    }

}