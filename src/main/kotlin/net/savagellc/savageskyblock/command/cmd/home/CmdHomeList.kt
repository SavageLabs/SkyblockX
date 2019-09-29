package net.savagellc.savageskyblock.command.cmd.home

import me.rayzr522.jsonmessage.JSONMessage
import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.core.color
import net.savagellc.savageskyblock.persist.Message

class CmdHomeList : SCommand() {


    init {
        aliases.add("list")

        this.commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.HOME).asIslandMember(true).build()
    }

    override fun perform(info: CommandInfo) {
        info.message(Message.commandHomeListHeader)
        for ((index, home) in info.iPlayer!!.getIsland()!!.getAllHomes().keys.withIndex()) {
            JSONMessage.create(color(String.format(Message.commandHomeListFormat, index + 1, home)))
                .tooltip(color(Message.commandHomeListRemoveTooltip)).runCommand("/is home remove $home")
                .send(info.player!!)
        }
    }

    override fun getHelpInfo(): String {
        return Message.commandHomeList
    }

}
