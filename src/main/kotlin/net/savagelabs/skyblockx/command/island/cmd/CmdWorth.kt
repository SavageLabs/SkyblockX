package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import java.text.NumberFormat

class CmdWorth : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("worth")
        aliases.add("level")

        commandRequirements = SCommandRequirementsBuilder()
            .withPermission(Permission.WORTH)
            .asIslandMember(true)
            .build()
    }


    override fun perform(info: SCommandInfo) {
        val numberFormat = NumberFormat.getInstance(Config.instance.numberFormatLocale)
        info.message(Message.instance.commandWorthValue, numberFormat.format(info.island!!.getValue()))
        info.message(Message.instance.commandWorthLevel, numberFormat.format(info.island!!.getLevel()))
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandWorthHelp
    }
}