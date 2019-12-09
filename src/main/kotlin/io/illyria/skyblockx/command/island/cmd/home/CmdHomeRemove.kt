package io.illyria.skyblockx.command.island.cmd.home

import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message

class CmdHomeRemove : io.illyria.skyblockx.command.SCommand() {


    init {
        aliases.add("remove")

        requiredArgs.add(Argument("home-name", 0, HomeArgument()))

        commandRequirements = io.illyria.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.HOME)
            .asIslandMember(true).build()
    }

    override fun perform(info: io.illyria.skyblockx.command.CommandInfo) {
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