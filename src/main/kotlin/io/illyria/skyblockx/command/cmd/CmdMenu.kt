package io.illyria.skyblockx.command.cmd

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.gui.IslandMenuGUI
import io.illyria.skyblockx.persist.Message

class CmdMenu : SCommand() {

    init {
        aliases.add("menu")


        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.MENU).asIslandMember(true).build()
    }

    override fun perform(info: CommandInfo) {
        IslandMenuGUI().showGui(info.player!!)
    }

    override fun getHelpInfo(): String {
        return Message.commandMenuHelp
    }


}