package io.illyria.skyblockx.command.cmd

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.gui.IslandQuestGUI
import io.illyria.skyblockx.persist.Message

class CmdJoin : SCommand() {

    init {
        aliases.add("join")

        requiredArgs.add(Argument("player-name", 0, PlayerArgument()))

        commandRequirements = CommandRequirementsBuilder().asPlayer(true).build()
    }


    override fun perform(info: CommandInfo) {
        val targetIPlayer = info.getArgAsIPlayer(0) ?: return

        if (!info.iPlayer!!.isInvitedToIsland(targetIPlayer.getIsland()!!)) {
            info.message(String.format(Message.commandJoinNotInvited, targetIPlayer.getPlayer().name))
            return
        }

        targetIPlayer.getIsland()!!.addMember(info.iPlayer!!)

    }


    override fun getHelpInfo(): String {
        return Message.commandJoinHelp
    }
}