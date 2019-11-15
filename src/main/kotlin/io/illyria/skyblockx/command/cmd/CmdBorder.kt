package io.illyria.skyblockx.command.cmd

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.gui.IslandBorderGUI
import io.illyria.skyblockx.persist.Message


class CmdBorder : SCommand() {

    init {
        aliases.add("border")

        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.BORDER).asIslandMember(true).build()
    }

    override fun perform(info: CommandInfo) {
        IslandBorderGUI().showGui(info.player!!)
    }

    override fun getHelpInfo(): String {
        return Message.commandBorderHelp
    }
}