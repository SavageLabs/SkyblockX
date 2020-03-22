package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message

class CmdSbBypass : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("bypass")

        commandRequirements =
            _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().asPlayer(true).withPermission(Permission.ADMIN_BYPASS)
                .build()
    }


    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        info.iPlayer!!.inBypass = !info.isBypassing()
        info.message(String.format(Message.commandBypassToggle, if (info.iPlayer!!.inBypass) "in" else "out of"))
    }

    override fun getHelpInfo(): String {
        return Message.commandBypassHelp

    }

}