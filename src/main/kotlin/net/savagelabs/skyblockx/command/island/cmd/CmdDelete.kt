package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

class CmdDelete : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("delete")

        commandRequirements =
            SCommandRequirementsBuilder()
                .asPlayer(true)
                .asIslandMember(true)
                .withPermission(Permission.DELETE)
                .build()
    }


    override fun perform(info: SCommandInfo) {
        info.island!!.delete()
        info.message(Message.instance.commandDeleteDeletedIsland)
    }


    override fun getHelpInfo(): String {
        return Message.instance.commandDeleteHelp
    }

}