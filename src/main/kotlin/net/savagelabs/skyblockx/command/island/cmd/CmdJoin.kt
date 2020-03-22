package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.persist.Message

class CmdJoin : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("join")

        requiredArgs.add(Argument("player-name", 0, PlayerArgument()))

        commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().asPlayer(true).build()
    }


    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        val targetIPlayer = info.getArgAsIPlayer(0) ?: return

        if (!info.iPlayer!!.isInvitedToIsland(targetIPlayer.getIsland()!!)) {
            info.message(String.format(Message.commandJoinNotInvited, targetIPlayer.getPlayer().name))
            return
        }


        targetIPlayer.getIsland()!!.addMember(info.iPlayer!!)
        info.iPlayer!!.islandsInvitedTo.remove(targetIPlayer.getIsland()!!.islandID)
        info.message(String.format(Message.commandJoinSuccess, targetIPlayer.getPlayer().name))
    }


    override fun getHelpInfo(): String {
        return Message.commandJoinHelp
    }
}