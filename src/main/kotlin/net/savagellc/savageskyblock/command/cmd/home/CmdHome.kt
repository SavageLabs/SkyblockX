package net.savagellc.savageskyblock.command.cmd.home

import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.persist.Message

class CmdHome : SCommand() {

    init {
        aliases.add("home")

        optionalArgs.add("home-name")

        commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.HOME).asPlayer(true).asIslandMember(true).build()

        subCommands.add(CmdHomeList())
        subCommands.add(CmdHomeSet())
        subCommands.add(CmdHomeRemove())
        subCommands.add(CmdHomeGo())
    }


    override fun perform(info: CommandInfo) {
        generateHelp(1, info.player!!)


    }

    override fun getHelpInfo(): String {
        return Message.commandHomeHelp
    }
}

