package net.savagelabs.skyblockx.command.island.cmd.home

import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.persist.Message
import me.rayzr522.jsonmessage.JSONMessage
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.*

class CmdHomeList : Command<SCommandInfo, SCommandRequirements>() {


    init {
        aliases.add("list")

        this.commandRequirements =
            SCommandRequirementsBuilder().withPermission(Permission.HOME)
                .asIslandMember(true).build()
    }

    override fun perform(info: SCommandInfo) {
        info.message(Message.instance.commandHomeListHeader)
        for ((index, home) in info.iPlayer!!.getIsland()!!.getAllHomes().keys.withIndex()) {
            JSONMessage.create(color(String.format(Message.instance.commandHomeListFormat, index + 1, home)))
                .tooltip(color(Message.instance.commandHomeListRemoveTooltip)).runCommand("/is home go $home")
                .send(info.player!!)
        }
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandHomeList
    }

}
