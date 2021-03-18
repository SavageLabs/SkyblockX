package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.PlayerArgument
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.gui.buildMenu
import net.savagelabs.skyblockx.gui.menu.CoopInviteMenu
import net.savagelabs.skyblockx.gui.menu.CoopManageMenu
import net.savagelabs.skyblockx.persist.Message

class CmdCoop : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("co-op")
		aliases.add("coop")

		optionalArgs.add(Argument("manage or (player)", 0, PlayerArgument()))

		commandRequirements =
			SCommandRequirementsBuilder().asPlayer(true).asIslandMember(true)
				.withPermission(Permission.COOP).build()
	}

	override fun perform(info: SCommandInfo) {
		if (info.args.isEmpty()) {
			buildMenu(CoopInviteMenu(info.player!!, info.island!!)).open(info.player!!)
			return
		}
		if (info.args[0].equals("manage", true)) {
			openManageMenu(info)
			return
		}
		val target = info.getArgAsIPlayer(0, cannotReferenceYourSelf = true) ?: return
		info.iPlayer!!.attemptToCoopPlayer(target)
	}

	fun openManageMenu(info: SCommandInfo) {
		buildMenu(CoopManageMenu(info.player!!)).open(info.player!!)
	}


	override fun getHelpInfo(): String {
		return Message.instance.commandCoopHelp
	}

}