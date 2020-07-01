package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.*
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.gui.IslandBorderGUI
import net.savagelabs.skyblockx.persist.Message


class CmdBorder : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("border")

        commandRequirements = SCommandRequirementsBuilder()
            .withPermission(Permission.BORDER)
            .asIslandMember(true)
            .build()
    }

    override fun perform(info: SCommandInfo) {
        IslandBorderGUI().showGui(info.player!!)
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandBorderHelp
    }
}