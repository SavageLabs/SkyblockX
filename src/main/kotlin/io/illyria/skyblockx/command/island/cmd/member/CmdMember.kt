package io.illyria.skyblockx.command.island.cmd.member

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.gui.IslandMemberGUI
import io.illyria.skyblockx.persist.Message

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
        IslandMemberGUI().showGui(info.player!!)
        // No Args / Invalid args specified.
        if (info.args.size != 1) {
            generateHelp(1, info.player!!)
            return
        }

    }

    override fun getHelpInfo(): String {
        return Message.commandMemberHelp
    }

}