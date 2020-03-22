package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.gui.IslandBorderGUI
import net.savagelabs.skyblockx.persist.Message


class CmdBorder : SCommand() {

    init {
        aliases.add("border")

        commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.BORDER).asIslandMember(true).build()
    }

    override fun perform(info: CommandInfo) {
        IslandBorderGUI().showGui(info.player!!)
    }

    override fun getHelpInfo(): String {
        return Message.commandBorderHelp
    }
}