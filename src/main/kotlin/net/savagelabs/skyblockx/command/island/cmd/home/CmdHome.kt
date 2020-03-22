package net.savagelabs.skyblockx.command.island.cmd.home

import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message

class CmdHome : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("home")

        optionalArgs.add(Argument("home-name", 0, HomeArgument()))

        commandRequirements =
            _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.HOME).asPlayer(true)
                .asIslandMember(true).build()

        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.home.CmdHomeList())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.home.CmdHomeSet())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.home.CmdHomeRemove())
        subCommands.add(_root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.home.CmdHomeGo())
    }


    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        // No Args / Invalid args specified.
        if (info.args.size != 1) {
            generateHelp(1, info.player!!)
            return
        }

        // Execute command go just to make a shorthand version for /is home go <home>.
        this.subCommands.find { command -> command is _root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.home.CmdHomeGo }?.perform(info)


    }

    override fun getHelpInfo(): String {
        return Message.commandHomeHelp
    }
}

