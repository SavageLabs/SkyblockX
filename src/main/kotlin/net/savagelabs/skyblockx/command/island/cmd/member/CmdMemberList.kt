package net.savagelabs.skyblockx.command.island.cmd.member

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.persist.Message
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

        for ((index, member) in info.iPlayer!!.getIsland()!!.getIslandMembers().withIndex()) {
            JSONMessage.create(color(String.format(Message.commandMemberListFormat, index + 2, member.name)))
                .tooltip(color(Message.commandMemberListRemoveTooltip)).runCommand("/is member kick ${member.name}")
                .send(info.player!!)
        }

    }

    override fun getHelpInfo(): String {
        return Message.commandMemberListHelp
    }

}