package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.PlayerArgument
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.persist.Message

class CmdJoin : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("join")

		requiredArgs.add(Argument("player-name", 0, PlayerArgument()))

		commandRequirements = SCommandRequirementsBuilder().asPlayer(true).build()
	}


	override fun perform(info: SCommandInfo) {
		val targetIPlayer = info.getArgAsIPlayer(0) ?: return
		if (info.iPlayer!!.hasIsland()) {
			info.message(Message.instance.commandJoinHaveIsland)
			return
		}

		if (!info.iPlayer!!.isInvitedToIsland(targetIPlayer.getIsland()!!)) {
			info.message(Message.instance.commandJoinNotInvited, targetIPlayer.name)
			return
		}


		targetIPlayer.getIsland()!!.addMember(info.iPlayer!!)
		info.iPlayer!!.islandsInvitedTo.remove(targetIPlayer.getIsland()!!.islandID)
		info.message(Message.instance.commandJoinSuccess, targetIPlayer.name)
	}


	override fun getHelpInfo(): String {
		return Message.instance.commandJoinHelp
	}
}