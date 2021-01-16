package net.savagelabs.skyblockx.command.island.cmd


import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.BooleanArgument
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

class CmdAllowVisitors : Command<SCommandInfo, SCommandRequirements>() {

	init {
		aliases.add("allow-visitors")
		aliases.add("visitors")

		this.optionalArgs.add(
			Argument(
				"toggle",
				0,
				BooleanArgument()
			)
		)
		commandRequirements = SCommandRequirementsBuilder()
			.withPermission(Permission.ALLOWVISITOR)
			.asIslandMember(true)
			.asLeader(true)
			.build()
	}

	override fun perform(info: SCommandInfo) {
		if (info.args.size == 1) {
			val argAsBoolean = info.getArgAsBoolean(0) ?: return
			info.island!!.allowVisitors = argAsBoolean
		} else {
			info.island!!.allowVisitors = !info.island!!.allowVisitors
		}

		info.message(Message.instance.commandAllowVisitorsStatus, info.island!!.allowVisitors.toString())
	}

	override fun getHelpInfo(): String {
		return Message.instance.commandAllowVisitorsHelp
	}
}