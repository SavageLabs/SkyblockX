package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.gui.IslandMenuGUI
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Message

class CmdMenu : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("menu")


        commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.MENU).asIslandMember(true).build()
    }

    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        if (Config.openIslandMenuOnBaseCommand) IslandMenuGUI().showGui(info.player!!)
    }

    override fun getHelpInfo(): String {
        return Message.commandMenuHelp
    }


}