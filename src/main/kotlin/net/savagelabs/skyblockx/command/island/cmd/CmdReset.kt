package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

class CmdReset : SCommand() {

    init {
        aliases.add("reset")

        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.RESET).asIslandMember(true).asLeader(true).build()
    }

    override fun perform(info: CommandInfo) {
        Globals.islandBaseCommand.subCommands.find { command -> command is CmdDelete }?.perform(info)
        Globals.islandBaseCommand.subCommands.find { command -> command is CmdCreate }?.perform(info)
    }

    override fun getHelpInfo(): String {
        return Message.commandResetHelp
    }

}