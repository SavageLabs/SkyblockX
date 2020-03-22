package net.savagelabs.skyblockx.command.island.cmd.home

import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message

class CmdHomeRemove : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {


    init {
        aliases.add("remove")

        requiredArgs.add(Argument("home-name", 0, HomeArgument()))

        commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.HOME)
            .asIslandMember(true).build()
    }

    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        if (!info.island!!.hasHome(info.args[0])) {
            info.message(Message.commandHomeDoesNotExist)
            return
        }

        info.island!!.removeHome(info.args[0])
        info.message(String.format(Message.commandHomeRemoveSuccess, info.args[0]))
    }

    override fun getHelpInfo(): String {
        return Message.commandHomeRemoveHelp
    }
}