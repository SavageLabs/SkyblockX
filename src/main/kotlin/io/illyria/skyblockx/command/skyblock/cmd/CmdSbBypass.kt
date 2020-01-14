package io.illyria.skyblockx.command.skyblock.cmd

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message

class CmdSbBypass : SCommand() {

    init {
        aliases.add("bypass")

        commandRequirements =
            CommandRequirementsBuilder().asPlayer(true).withPermission(Permission.ADMIN_BYPASS)
                .build()
    }


    override fun perform(info: CommandInfo) {
        info.iPlayer!!.inBypass = !info.isBypassing()
        info.message(String.format(Message.commandBypassToggle, if (info.iPlayer!!.inBypass) "in" else "out of"))
    }

    override fun getHelpInfo(): String {
        return Message.commandBypassHelp

    }

}