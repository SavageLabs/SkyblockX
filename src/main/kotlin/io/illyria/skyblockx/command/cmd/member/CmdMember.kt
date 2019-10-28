package io.illyria.skyblockx.command.cmd.member

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.persist.Message

class CmdMember : SCommand() {

    init {
        aliases.add("member")
        aliases.add("members")

        commandRequirements = CommandRequirementsBuilder().asIslandMember(true).build()

        subCommands.add(CmdMemberInvite())
        subCommands.add(CmdMemberList())
        subCommands.add(CmdMemberRemove())
    }

    override fun perform(info: CommandInfo) {
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