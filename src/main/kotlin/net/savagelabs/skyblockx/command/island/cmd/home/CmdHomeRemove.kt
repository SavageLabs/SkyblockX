package net.savagelabs.skyblockx.command.island.cmd.home

import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder

class CmdHomeRemove : SCommand() {


    init {
        aliases.add("remove")

        requiredArgs.add(Argument("home-name", 0, HomeArgument()))

        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.HOME)
            .asIslandMember(true).build()
    }

    override fun perform(info: CommandInfo) {
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