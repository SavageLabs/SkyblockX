package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

class CmdCoop : SCommand() {

    init {
        aliases.add("co-op")
        aliases.add("coop")

        requiredArgs.add(Argument("player", 0, PlayerArgument()))

        commandRequirements =
           CommandRequirementsBuilder().asPlayer(true).asIslandMember(true)
                .withPermission(Permission.COOP).build()
    }


    override fun perform(info: CommandInfo) {
        val target = info.getArgAsIPlayer(0) ?: return
        if (!info.iPlayer!!.getIsland()!!.canHaveMoreCoopPlayers()) {
            info.message(Message.instance.commandCoopCannotHaveMoreCoopPlayers)
            return
        }

        info.iPlayer!!.getIsland()!!.coopPlayer(info.iPlayer, target)

        target.message(String.format(Message.instance.commandCoopMessageRecipient, info.player!!.name))
        info.iPlayer!!.message(String.format(Message.instance.commandCoopInvokerSuccess, target.getPlayer().name))
    }


    override fun getHelpInfo(): String {
        return Message.instance.commandCoopHelp
    }


}