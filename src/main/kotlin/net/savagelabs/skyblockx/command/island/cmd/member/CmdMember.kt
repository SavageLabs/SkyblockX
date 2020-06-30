package net.savagelabs.skyblockx.command.island.cmd.member

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.gui.IslandMemberGUI
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message

class CmdMember : SCommand() {

    init {
        aliases.add("member")
        aliases.add("members")

        commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).build()

        subCommands.add(CmdMemberInvite())
        subCommands.add(CmdMemberList())
        subCommands.add(CmdMemberKick())
        subCommands.add(CmdMemberPromote())
    }

    override fun perform(info: CommandInfo) {
        if (Config.instance.islandMemberShowMenu) IslandMemberGUI().showGui(info.player!!)
        else
        // No Args / Invalid args specified.
            if (info.args.size != 1) {
                generateHelp(1, info.player!!)
                return
            }

    }

    override fun getHelpInfo(): String {
        return Message.instance.commandMemberHelp
    }

}