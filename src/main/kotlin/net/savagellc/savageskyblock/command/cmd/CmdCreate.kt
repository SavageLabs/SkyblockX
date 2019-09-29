package net.savagellc.savageskyblock.command.cmd

import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.gui.IslandCreateGUI
import net.savagellc.savageskyblock.persist.Message


class CmdCreate : SCommand() {

    init {
        aliases.add("create")

        commandRequirements =
            CommandRequirementsBuilder().asIslandMember(false).asPlayer(true).withPermission(Permission.CREATE).build()
    }

    override fun perform(info: CommandInfo) {
        if (info.iPlayer!!.hasIsland()) {
            info.message(Message.commandCreateAlreadyHaveAnIsland)
            return
        }

        val islandCreateGUI = IslandCreateGUI()
        islandCreateGUI.buildGui()
        islandCreateGUI.showGui(info.player!!)
    }


    override fun getHelpInfo(): String {
        return Message.commandCreateHelp
    }

}