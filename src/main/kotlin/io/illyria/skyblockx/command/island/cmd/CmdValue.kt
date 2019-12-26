package io.illyria.skyblockx.command.island.cmd

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirements
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.persist.BlockValues
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Message
import net.prosavage.baseplugin.XMaterial
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class CmdValue : SCommand() {

    init {
        aliases.add("value")

        commandRequirements = CommandRequirementsBuilder().asPlayer(true).build()
    }



    override fun perform(info: CommandInfo) {
        val blockMaterial = info.player!!.inventory.itemInMainHand.type
        val xmat = XMaterial.matchXMaterial(blockMaterial)
        val value = BlockValues.blockValues[xmat] ?: 0.0
        info.message(String.format(Message.commandValueInfo, NumberFormat.getInstance(Config.numberFormatLocale).format(value)))

    }

    override fun getHelpInfo(): String {
        return Message.commandValueHelp
    }
}