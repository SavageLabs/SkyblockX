package net.savagelabs.skyblockx.command.island.cmd.member

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.command.argument.MemberArgument
import net.savagelabs.skyblockx.command.island.IslandBaseCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.getIPlayerByName
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.Bukkit

class CmdMemberPromote : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("promote")
		aliases.add("leader")

		requiredArgs.add(Argument("island-member", 0, MemberArgument()))
		commandRequirements =
			SCommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).asLeader(true).build()
	}

	override fun perform(info: SCommandInfo) {
		val island = info.island!!
		if (island.getIslandMembers(false).isEmpty()) {
			info.message(Message.instance.commandMemberNoMembers)
			return
		}


		val playerNameToPromote = info.args[0]
		if (playerNameToPromote == info.player!!.name) {
			info.message(Message.instance.genericCannotReferenceYourSelf)
			return
		}

		if (!info.island!!.getIslandMembers(false).contains(getIPlayerByName(playerNameToPromote))) {
			info.message(Message.instance.commandMemberPromoteNotFound)
			return
		}

		island.promoteNewLeader(playerNameToPromote)
		island.messageAllOnlineIslandMembers(
			String.format(
				Message.instance.commandMemberPromotedSuccess,
				playerNameToPromote
			)
		)
		Bukkit.getPlayer(playerNameToPromote)
			?.sendMessage(color(Message.instance.commandMemberPromoteYouHaveBeenPromoted))
	}

	override fun getHelpInfo(): String {
		return Message.instance.commandMemberPromoteHelp
	}
}

class CmdPromote : Command<SCommandInfo, SCommandRequirements>() {
	init {
		aliases.add("promote")
		aliases.add("leader")

		requiredArgs.add(Argument("island-member", 0, MemberArgument()))
		commandRequirements =
			SCommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).asLeader(true).build()
	}

	override fun perform(info: SCommandInfo) {
		// Execute command go just to make a shorthand version for /is member kick <member>.
		IslandBaseCommand.instance.subCommands.find { command -> command is CmdMember }
			?.subCommands?.find { command -> command is CmdMemberPromote }?.perform(info)
	}

	override fun getHelpInfo(): String {
		return Message.instance.commandMemberKickHelp
	}

}