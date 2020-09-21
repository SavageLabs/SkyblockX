package net.savagelabs.skyblockx.command.island.cmd

import com.cryptomorin.xseries.XMaterial
import me.rayzr522.jsonmessage.JSONMessage
import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.IntArgument
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.buildBar
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Data
import net.savagelabs.skyblockx.persist.Message
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.time.ExperimentalTime

class CmdTop : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("top")

        optionalArgs.add(Argument("page", 0, IntArgument()))

        commandRequirements =
            SCommandRequirementsBuilder().withPermission(Permission.INFO)
                .build()
    }


    @ExperimentalTime
    override fun perform(info: SCommandInfo) {
        if (SkyblockX.islandValues == null || SkyblockX.islandValues!!.map.isEmpty()) {
            info.message(Message.instance.commandTopNotCalculated)
            return
        }
        val sortedBy = SkyblockX.islandValues!!.map.values.sortedByDescending { entry -> entry.worth }
        var counter = 1
        // Should be able to add the prefix if they want in Message.instance.json, right now, it doesn't match because the top island entries part does not have the prefix.
        if (Config.instance.useIslandTopHeadMessage) info.message(Config.instance.islandTopHeadMessage, false)
        if (Config.instance.useIslandTopHeaderBar) {
            info.message(buildBar(Config.instance.islandTopbarElement), false)
        }
        if (info.args.isNotEmpty()) {
            counter = info.getArgAsInt(0) ?: return
            if (counter <= 0) {
                info.message(Message.instance.commandTopInvalidPage)
                return
            }
        }
        val startIndex = (counter - 1) * Config.instance.commandTopPageSize
        if (startIndex > sortedBy.size - 1) {
            info.message(Message.instance.commandTopIndexTooHigh)
            return
        }

        for (islandindex in startIndex..startIndex + Config.instance.commandTopPageSize) {
            val entry = sortedBy.getOrNull(islandindex) ?: break
            val builder = StringBuilder()
            entry.matAmt.forEach { xmat -> builder.append("${xmat.key.name}: ${xmat.value}\n") }
            var tooltip = arrayListOf<String>();
            val island = Data.instance.islands[entry.islandID]!!
            for (line in Config.instance.islandTopTooltip) {
                var lineBasicParsed = line.parseContext(island, islandindex, entry)
                entry.matAmt.forEach { xmat ->
                    lineBasicParsed = lineBasicParsed.replace(
                        "{${xmat.key.name}}",
                        NumberFormat.getInstance(Config.instance.numberFormatLocale).format(xmat.value)
                    )
                }
                XMaterial.values().toList()
                    .forEach { xmat -> lineBasicParsed = lineBasicParsed.replace("{${xmat.name}}", 0.toString()) }
                tooltip.add(color(lineBasicParsed))
            }
            val line = color(
                Config.instance.islandTopLineFormat.parseContext(island, islandindex, entry)
            )
            if (info.isPlayer()) {
                JSONMessage.create(line).tooltip(tooltip.joinToString("\n")).send(info.player)
            } else {
                info.message(line, false.toString())
            }
        }

    }

    private fun String.parseContext(island: Island, islandIndex: Int, entry: Island.CalcInfo): String {
        return this.replace("{name}", island.islandName)
            .replace("{rank}", NumberFormat.getInstance(Config.instance.numberFormatLocale).format(islandIndex + 1))
            .replace("{leader}", island.getLeader()?.name ?: "SYSTEM")
            .replace("{amount}", NumberFormat.getInstance(Config.instance.numberFormatLocale).format(entry.worth))
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandTopHelp
    }

}