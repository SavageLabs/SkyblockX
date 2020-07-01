package net.savagelabs.skyblockx.command.island.cmd

import com.cryptomorin.xseries.XMaterial
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.persist.BlockValues
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import java.text.NumberFormat

class CmdValue : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("value")

        commandRequirements = SCommandRequirementsBuilder().asPlayer(true).build()
    }


    override fun perform(info: SCommandInfo) {
        val blockMaterial = info.player!!.itemInHand.type
        val xmat = XMaterial.matchXMaterial(blockMaterial)
        val value = BlockValues.instance.blockValues[xmat] ?: 0.0
        info.message(
            String.format(
                Message.instance.commandValueInfo,
                NumberFormat.getInstance(Config.instance.numberFormatLocale).format(value)
            )
        )
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandValueHelp
    }
}