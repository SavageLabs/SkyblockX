package io.illyria.skyblockx.command.island.cmd

import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message

class CmdBypass : io.illyria.skyblockx.command.SCommand() {

    init {
        aliases.add("bypass")

        commandRequirements =
            io.illyria.skyblockx.command.CommandRequirementsBuilder().asPlayer(true).withPermission(Permission.BYPASS)
                .build()
    }


    override fun perform(info: io.illyria.skyblockx.command.CommandInfo) {
        info.iPlayer!!.inBypass = !info.isBypassing()
        info.message(String.format(Message.commandBypassToggle, if (info.iPlayer!!.inBypass) "in" else "out of"))
    }

    override fun getHelpInfo(): String {
        return Message.commandBypassHelp

    }

}