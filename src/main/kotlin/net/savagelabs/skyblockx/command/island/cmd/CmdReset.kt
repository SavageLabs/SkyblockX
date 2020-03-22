package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message

class CmdReset : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("reset")

        commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.RESET).asIslandMember(true).asLeader(true).build()
    }

    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        _root_ide_package_.net.savagelabs.skyblockx.Globals.islandBaseCommand.subCommands.find { command -> command is _root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdDelete }?.perform(info)
        _root_ide_package_.net.savagelabs.skyblockx.Globals.islandBaseCommand.subCommands.find { command -> command is _root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.CmdCreate }?.perform(info)
    }

    override fun getHelpInfo(): String {
        return Message.commandResetHelp
    }

}