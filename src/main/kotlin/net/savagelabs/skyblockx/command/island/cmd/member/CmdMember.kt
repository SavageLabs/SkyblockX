package net.savagelabs.skyblockx.command.island.cmd.member

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.gui.IslandMemberGUI
import io.illyria.skyblockx.persist.Message

class CmdMember : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("member")
        aliases.add("members")

        commandRequirements =
            _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).build()

        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.member.CmdMemberInvite())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.member.CmdMemberList())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.member.CmdMemberKick())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.member.CmdMemberPromote())
    }

    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
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