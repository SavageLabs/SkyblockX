package io.illyria.skyblockx.command.island.cmd

import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.deleteIsland
import io.illyria.skyblockx.persist.Message

class CmdDelete : io.illyria.skyblockx.command.SCommand() {

    init {
        aliases.add("delete")

        commandRequirements =
           CommandRequirementsBuilder().asIslandMember(true)
                .withPermission(Permission.DELETE).build()
    }


    override fun perform(info: io.illyria.skyblockx.command.CommandInfo) {
        info.island!!.delete()
        info.message(Message.commandDeleteDeletedIsland)

    }


    override fun getHelpInfo(): String {
        return Message.commandDeleteHelp
    }

}