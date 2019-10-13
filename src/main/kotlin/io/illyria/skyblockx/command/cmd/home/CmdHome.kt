package io.illyria.skyblockx.command.cmd.home

import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message

class CmdHome : io.illyria.skyblockx.command.SCommand() {

    init {
        aliases.add("home")

        optionalArgs.add(Argument("home-name", 0, HomeArgument()))

        commandRequirements =
            io.illyria.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.HOME).asPlayer(true)
                .asIslandMember(true).build()

        subCommands.add(io.illyria.skyblockx.command.cmd.home.CmdHomeList())
        subCommands.add(io.illyria.skyblockx.command.cmd.home.CmdHomeSet())
        subCommands.add(io.illyria.skyblockx.command.cmd.home.CmdHomeRemove())
        subCommands.add(io.illyria.skyblockx.command.cmd.home.CmdHomeGo())
    }


    override fun perform(info: io.illyria.skyblockx.command.CommandInfo) {
        // No Args / Invalid args specified.
        if (info.args.size != 1) {
            generateHelp(1, info.player!!)
            return
        }

        // Execute command go just to make a shorthand version for /is home go <home>.
        this.subCommands.find { command -> command is io.illyria.skyblockx.command.cmd.home.CmdHomeGo }?.perform(info)


    }

    override fun getHelpInfo(): String {
        return Message.commandHomeHelp
    }
}

