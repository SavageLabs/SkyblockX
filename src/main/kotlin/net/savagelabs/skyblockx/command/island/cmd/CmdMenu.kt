package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.gui.IslandMenuGUI
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message

class CmdMenu : SCommand() {

    init {
        aliases.add("menu")


        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.MENU).asIslandMember(true).build()
    }

    override fun perform(info: CommandInfo) {
        if (Config.instance.openIslandMenuOnBaseCommand) IslandMenuGUI().showGui(info.player!!)
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandMenuHelp
    }


}