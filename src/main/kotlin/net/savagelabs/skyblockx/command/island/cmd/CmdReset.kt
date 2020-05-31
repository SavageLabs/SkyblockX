package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.command.island.IslandBaseCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

class CmdReset : SCommand() {

    init {
        aliases.add("reset")

        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.RESET).asIslandMember(true).asLeader(true).build()
    }

    override fun perform(info: CommandInfo) {
        IslandBaseCommand.instance.subCommands.find { command -> command is CmdDelete }?.perform(info)
        IslandBaseCommand.instance.subCommands.find { command -> command is CmdCreate }?.perform(info)
    }

    override fun getHelpInfo(): String {
        return Message.commandResetHelp
    }

}