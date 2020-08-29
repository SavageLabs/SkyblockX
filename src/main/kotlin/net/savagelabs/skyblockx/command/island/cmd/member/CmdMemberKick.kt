package net.savagelabs.skyblockx.command.island.cmd.member

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.command.argument.MemberArgument
import net.savagelabs.skyblockx.command.island.IslandBaseCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.getIPlayerByName
import net.savagelabs.skyblockx.event.IslandKickEvent
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.Bukkit

class CmdMemberKick : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("kick")

		requiredArgs.add(Argument("island-member", 0, MemberArgument()))
		commandRequirements =
			SCommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).asLeader(true).build()
	}


	override fun perform(info: SCommandInfo) {
		val island = info.island!!
		if (island.getIslandMembers(false).isEmpty()) {
			info.message(Message.instance.commandMemberKickLimit)
			return
		}
		val playerNameToRemove = info.args[0]
		if (playerNameToRemove == info.player!!.name) {
			info.message(Message.instance.genericCannotReferenceYourSelf)
			return
		}

		if (!info.island!!.getIslandMembers(false)
				.map { member -> member.name }
				.contains(playerNameToRemove)
		) {
			info.message(Message.instance.commandMemberKickNotFound)
			return
		}


		info.island!!.kickMember(playerNameToRemove)
		info.message(Message.instance.commandMemberKicked, playerNameToRemove)

		Bukkit.getPluginManager().callEvent(IslandKickEvent(island, info.player!!.getIPlayer(), getIPlayerByName(playerNameToRemove)!!))
	}

	override fun getHelpInfo(): String {
		return Message.instance.commandMemberKickHelp
	}
}

class CmdKick : Command<SCommandInfo, SCommandRequirements>() {
	init {
		aliases.add("kick")

		requiredArgs.add(Argument("island-member", 0, MemberArgument()))
		commandRequirements =
			SCommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).asLeader(true).build()
	}

	override fun perform(info: SCommandInfo) {
		// Execute command go just to make a shorthand version for /is member kick <member>.
		IslandBaseCommand.instance.subCommands
			.find { command -> command is CmdMember }
			?.subCommands?.find { command -> command is CmdMemberKick }?.perform(info)
	}

	override fun getHelpInfo(): String {
		return Message.instance.commandMemberKickHelp
	}

}