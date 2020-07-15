package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.BooleanArgument
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

class CmdChat : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("chat")

		optionalArgs.add(Argument("channel", 0, BooleanArgument()))

		commandRequirements = SCommandRequirementsBuilder()
			.withPermission(Permission.CHAT)
			.asIslandMember(true)
			.build()
	}


	override fun perform(info: SCommandInfo) {
		val iPlayer = info.iPlayer!!
		if (info.args.isEmpty()) {
			iPlayer.isUsingIslandChat = !iPlayer.isUsingIslandChat
		} else {
			val useIslandChat = info.getArgAsBoolean(0) ?: return
			iPlayer.isUsingIslandChat = useIslandChat
		}

		info.message(
			Message.instance.commandChatChange,
			if (iPlayer.isUsingIslandChat) Message.instance.commandChatOn else Message.instance.commandChatOff
		)
	}

	override fun getHelpInfo(): String {
		return Message.instance.commandChatHelp
	}
}