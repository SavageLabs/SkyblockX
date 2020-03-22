package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

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