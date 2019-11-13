package io.illyria.skyblockx.command.cmd.home

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.persist.Message
import me.rayzr522.jsonmessage.JSONMessage

class CmdHomeList : SCommand() {


    init {
        aliases.add("list")

        this.commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.HOME)
                .asIslandMember(true).build()
    }

    override fun perform(info: CommandInfo) {
        info.message(Message.commandHomeListHeader)
        for ((index, home) in info.iPlayer!!.getIsland()!!.getAllHomes().keys.withIndex()) {
            JSONMessage.create(color(String.format(Message.commandHomeListFormat, index + 1, home)))
                .tooltip(color(Message.commandHomeListRemoveTooltip)).runCommand("/is home go $home")
                .send(info.player!!)
        }
    }

    override fun getHelpInfo(): String {
        return Message.commandHomeList
    }

}
