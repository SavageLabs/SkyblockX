package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.deleteIsland
import io.illyria.skyblockx.persist.Message

class CmdDelete : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("delete")

        commandRequirements =
           _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().asIslandMember(true)
                .withPermission(Permission.DELETE).build()
    }


    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        info.island!!.delete()
        info.message(Message.commandDeleteDeletedIsland)

    }


    override fun getHelpInfo(): String {
        return Message.commandDeleteHelp
    }

}