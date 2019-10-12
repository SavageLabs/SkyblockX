package net.savagellc.savageskyblock.command.cmd.home

import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.persist.Message

class CmdHome : SCommand() {

    init {
        aliases.add("home")

        optionalArgs.add(Argument("home-name", 0, HomeArgument()))

        commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.HOME).asPlayer(true).asIslandMember(true).build()

        subCommands.add(CmdHomeList())
        subCommands.add(CmdHomeSet())
        subCommands.add(CmdHomeRemove())
        subCommands.add(CmdHomeGo())
    }


    override fun perform(info: CommandInfo) {
        // No Args / Invalid args specified.
        if (info.args.size != 1) {
            generateHelp(1, info.player!!)
            return
        }

        // Execute command go just to make a shorthand version for /is home go <home>.
        this.subCommands.find { command -> command is CmdHomeGo }?.perform(info)


    }

    override fun getHelpInfo(): String {
        return Message.commandHomeHelp
    }
}

