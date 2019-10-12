package net.savagellc.savageskyblock.command.cmd.home

import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.persist.Message

class CmdHomeRemove : SCommand() {


    init {
        aliases.add("remove")

        requiredArgs.add(Argument("home-name", 0, HomeArgument()))

        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.HOME).asIslandMember(true).build()
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