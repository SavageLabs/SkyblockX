package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message

class CmdAllowVisitors : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

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
        commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.ALLOWVISITOR).asIslandMember(true).asLeader(true).build()
    }

    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        if (info.args.size == 1) {
            val argAsBoolean = info.getArgAsBoolean(0) ?: return
            info.island!!.allowVisitors = argAsBoolean
        } else {
            info.island!!.allowVisitors = !info.island!!.allowVisitors
        }

        info.message(String.format(Message.commandAllowVisitorsStatus, info.island!!.allowVisitors))
    }

    override fun getHelpInfo(): String {
        return Message.commandAllowVisitorsHelp
    }
}