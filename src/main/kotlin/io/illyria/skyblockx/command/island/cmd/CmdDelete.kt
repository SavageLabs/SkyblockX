package io.illyria.skyblockx.command.island.cmd

import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.deleteIsland
import io.illyria.skyblockx.persist.Message

class CmdDelete : io.illyria.skyblockx.command.SCommand() {

    init {
        aliases.add("delete")

        commandRequirements =
            io.illyria.skyblockx.command.CommandRequirementsBuilder().asIslandMember(true)
                .withPermission(Permission.DELETE).build()
    }


    override fun perform(info: io.illyria.skyblockx.command.CommandInfo) {
        deleteIsland(info.player!!)
        info.message(Message.commandDeleteDeletedIsland)

    }


    override fun getHelpInfo(): String {
        return Message.commandDeleteHelp
    }

}