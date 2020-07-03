package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.gui.IslandBorderGUI
import net.savagelabs.skyblockx.gui.menu.BorderMenu
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
        BorderMenu().build().open(info.player)
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandBorderHelp
    }
}