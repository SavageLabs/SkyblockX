package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirements
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.persist.BlockValues
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Message
import net.prosavage.baseplugin.XMaterial
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class CmdValue : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("value")

        commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().asPlayer(true).build()
    }



    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        val blockMaterial = info.player!!.inventory.itemInMainHand.type
        val xmat = XMaterial.matchXMaterial(blockMaterial)
        val value = BlockValues.blockValues[xmat] ?: 0.0
        info.message(String.format(Message.commandValueInfo, NumberFormat.getInstance(Config.numberFormatLocale).format(value)))

    }

    override fun getHelpInfo(): String {
        return Message.commandValueHelp
    }
}