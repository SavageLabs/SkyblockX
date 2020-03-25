package net.savagelabs.skyblockx.command.island.cmd

import com.cryptomorin.xseries.XMaterial
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.persist.BlockValues
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import java.text.NumberFormat

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