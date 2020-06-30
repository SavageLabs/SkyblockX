package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.persist.Message

class CmdJoin : SCommand() {

    init {
        aliases.add("join")

        requiredArgs.add(Argument("player-name", 0, PlayerArgument()))

        commandRequirements = CommandRequirementsBuilder().asPlayer(true).build()
    }


    override fun perform(info: CommandInfo) {
        val targetIPlayer = info.getArgAsIPlayer(0) ?: return
        if (info.iPlayer!!.hasIsland()) {
            info.message(Message.instance.commandJoinHaveIsland)
            return
        }

        if (!info.iPlayer!!.isInvitedToIsland(targetIPlayer.getIsland()!!)) {
            info.message(String.format(Message.instance.commandJoinNotInvited, targetIPlayer.getPlayer().name))
            return
        }


        targetIPlayer.getIsland()!!.addMember(info.iPlayer!!)
        info.iPlayer!!.islandsInvitedTo.remove(targetIPlayer.getIsland()!!.islandID)
        info.message(String.format(Message.instance.commandJoinSuccess, targetIPlayer.getPlayer().name))
    }


    override fun getHelpInfo(): String {
        return Message.instance.commandJoinHelp
    }
}