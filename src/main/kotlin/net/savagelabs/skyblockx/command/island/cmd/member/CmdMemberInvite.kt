package net.savagelabs.skyblockx.command.island.cmd.member

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.PlayerArgument
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.command.island.IslandBaseCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.gui.buildMenu
import net.savagelabs.skyblockx.gui.menu.InviteMenu
import net.savagelabs.skyblockx.persist.Message

class CmdMemberInvite : Command<SCommandInfo, SCommandRequirements>() {


	init {
		aliases.add("invite")

		optionalArgs.add(Argument("player", 0, PlayerArgument()))

		commandRequirements =
			SCommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).build()
	}


	override fun perform(info: SCommandInfo) {
		val island = info.island!!

		if (info.args.isEmpty()) {
			buildMenu(InviteMenu(info.island!!, info.iPlayer!!)).open(info.player!!)
			return
		}
		val playerToInvite = info.getArgAsIPlayer(0, true, cannotReferenceYourSelf = true) ?: return

		island.attemptInvite(info.iPlayer!!, playerToInvite)
	}

	override fun getHelpInfo(): String {
		return Message.instance.commandMemberInviteHelp
	}

}

class CmdInvite : Command<SCommandInfo, SCommandRequirements>() {
	init {
		aliases.add("invite")
		optionalArgs.add(Argument("player", 0, PlayerArgument()))
		commandRequirements =
			SCommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).build()
	}

	override fun perform(info: SCommandInfo) {
		IslandBaseCommand.instance.subCommands.find { command -> command is CmdMember }
			?.subCommands?.find { subcommand -> subcommand is CmdMemberInvite }?.perform(info)
	}

	override fun getHelpInfo(): String {
		return Message.instance.commandMemberInviteHelp
	}
}

