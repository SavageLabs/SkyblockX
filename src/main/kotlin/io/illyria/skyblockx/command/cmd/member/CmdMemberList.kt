package io.illyria.skyblockx.command.cmd.member

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.persist.Message
import me.rayzr522.jsonmessage.JSONMessage

class CmdMemberList : SCommand() {

    init {
        aliases.add("list")

        commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).build()
    }


    override fun perform(info: CommandInfo) {
        info.message(Message.commandMemberListHeader)

        JSONMessage.create(color(String.format(Message.commandMemberListFormat, 1, info.island!!.ownerTag)))
            .tooltip(color(Message.commandMemberListRemoveTooltip))
            .runCommand("/is member kick ${info.island!!.ownerTag}")
            .send(info.player!!)

        for ((index, member) in info.iPlayer!!.getIsland()!!.getAllMembers().withIndex()) {
            JSONMessage.create(color(String.format(Message.commandMemberListFormat, index + 2, member)))
                .tooltip(color(Message.commandMemberListRemoveTooltip)).runCommand("/is member kick $member")
                .send(info.player!!)
        }

    }

    override fun getHelpInfo(): String {
        return Message.commandMemberListHelp
    }

}