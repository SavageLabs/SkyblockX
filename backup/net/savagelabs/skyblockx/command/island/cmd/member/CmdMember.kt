package net.savagelabs.skyblockx.command.island.cmd.member

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.gui.buildMenu
import net.savagelabs.skyblockx.gui.menu.MemberMenu
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message

class CmdMember : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("member")
		aliases.add("members")

		commandRequirements =
			SCommandRequirementsBuilder()
				.withPermission(Permission.MEMBER)
				.asIslandMember(true)
				.build()

		subCommands.add(CmdMemberInvite())
		subCommands.add(CmdMemberList())
		subCommands.add(CmdMemberKick())
		subCommands.add(CmdMemberPromote())
	}

	override fun perform(info: SCommandInfo) {
		if (Config.instance.showMemberManagerGUI) buildMenu(MemberMenu(info.player!!, info.island!!)).open(info.player)
		else
		// No Args / Invalid args specified.
			if (info.args.size != 1) {
				generateHelp(1, info.player!!, info.args)
				return
			}

	}

	override fun getHelpInfo(): String {
		return Message.instance.commandMemberHelp
	}

}