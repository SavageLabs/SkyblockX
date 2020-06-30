package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import java.text.NumberFormat

class CmdWorth : SCommand() {

    init {
        aliases.add("worth")
        aliases.add("level")

        commandRequirements = CommandRequirementsBuilder()
            .withPermission(Permission.WORTH)
            .asIslandMember(true)
            .build()
    }


    override fun perform(info: CommandInfo) {
        val numberFormat = NumberFormat.getInstance(Config.instance.numberFormatLocale)
        info.message(Message.instance.commandWorthValue, numberFormat.format(info.island!!.getValue()))
        info.message(Message.instance.commandWorthLevel, numberFormat.format(info.island!!.getLevel()))
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandWorthHelp
    }
}