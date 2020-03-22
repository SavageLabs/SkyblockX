package net.savagelabs.skyblockx.command.island.cmd.home

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.persist.Message
import me.rayzr522.jsonmessage.JSONMessage

class CmdHomeList : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {


    init {
        aliases.add("list")

        this.commandRequirements =
            _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.HOME)
                .asIslandMember(true).build()
    }

    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
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
