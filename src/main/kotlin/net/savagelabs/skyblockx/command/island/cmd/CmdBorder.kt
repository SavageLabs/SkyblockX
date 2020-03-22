package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.gui.IslandBorderGUI
import io.illyria.skyblockx.persist.Message


class CmdBorder : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("border")

        commandRequirements =
            _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.BORDER).asIslandMember(true).build()
    }

    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        IslandBorderGUI().showGui(info.player!!)
    }

    override fun getHelpInfo(): String {
        return Message.commandBorderHelp
    }
}