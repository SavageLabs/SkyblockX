package io.illyria.skyblockx.command.island.cmd

import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message

class CmdCoop : SCommand() {

    init {
        aliases.add("co-op")
        aliases.add("coop")

        requiredArgs.add(Argument("player", 0, PlayerArgument()))

        commandRequirements =
            io.illyria.skyblockx.command.CommandRequirementsBuilder().asPlayer(true).asIslandMember(true)
                .withPermission(Permission.COOP).build()
    }


    override fun perform(info: io.illyria.skyblockx.command.CommandInfo) {
        val target = info.getArgAsIPlayer(0) ?: return
        if (!info.iPlayer!!.getIsland()!!.canHaveMoreCoopPlayers()) {
            info.message(Message.commandCoopCannotHaveMoreCoopPlayers)
            return
        }

        info.iPlayer!!.getIsland()!!.coopPlayer(info.iPlayer, target)

        target.message(String.format(Message.commandCoopMessageRecipient, info.player!!.name))
        info.iPlayer!!.message(String.format(Message.commandCoopInvokerSuccess, target.getPlayer().name))
    }


    override fun getHelpInfo(): String {
        return Message.commandCoopHelp
    }


}