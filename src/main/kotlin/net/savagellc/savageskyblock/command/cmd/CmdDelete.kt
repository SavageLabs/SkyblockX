package net.savagellc.savageskyblock.command.cmd

import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.core.deleteIsland
import net.savagellc.savageskyblock.persist.Message

class CmdDelete : SCommand() {

    init {
        aliases.add("delete")

        commandRequirements =
            CommandRequirementsBuilder().asIslandMember(true).withPermission(Permission.DELETE).build()
    }


    override fun perform(info: CommandInfo) {
        deleteIsland(info.player!!)
        info.message(Message.commandDeleteDeletedIsland)

    }


    override fun getHelpInfo(): String {
        return Message.commandDeleteHelp
    }

}