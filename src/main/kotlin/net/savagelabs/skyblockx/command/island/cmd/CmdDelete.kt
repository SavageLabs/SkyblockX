package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

class CmdDelete : SCommand() {

    init {
        aliases.add("delete")

        commandRequirements =
            CommandRequirementsBuilder()
                .asPlayer(true)
                .asIslandMember(true)
                .withPermission(Permission.DELETE)
                .build()
    }


    override fun perform(info: CommandInfo) {
        info.island!!.delete()
        info.message(Message.instance.commandDeleteDeletedIsland)
    }


    override fun getHelpInfo(): String {
        return Message.instance.commandDeleteHelp
    }

}