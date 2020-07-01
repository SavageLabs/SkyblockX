package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.*
import net.savagelabs.skyblockx.command.island.IslandBaseCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

class CmdReset : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("reset")

        commandRequirements = SCommandRequirementsBuilder()
            .withPermission(Permission.RESET)
            .asIslandMember(true)
            .asLeader(true)
            .build()
    }

    override fun perform(info: SCommandInfo) {
        IslandBaseCommand.instance.subCommands.find { command -> command is CmdDelete }?.perform(info)
        IslandBaseCommand.instance.subCommands.find { command -> command is CmdCreate }?.perform(info)
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandResetHelp
    }

}