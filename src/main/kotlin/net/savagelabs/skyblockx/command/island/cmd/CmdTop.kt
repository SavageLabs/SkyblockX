package net.savagelabs.skyblockx.command.island.cmd

import com.cryptomorin.xseries.XMaterial
import me.rayzr522.jsonmessage.JSONMessage
import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.buildBar
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Data
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.entity.EntityType
import java.text.DecimalFormat
import kotlin.time.ExperimentalTime

class CmdTop : SCommand() {

    init {
        aliases.add("top")

        optionalArgs.add(Argument("page", 0, IntArgument()))

        commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.INFO)
                .build()
    }


    @ExperimentalTime
    override fun perform(info: CommandInfo) {
        if (Globals.islandValues == null || Globals.islandValues!!.map.isEmpty()) {
            info.message(Message.commandTopNotCalculated)
            return
        }
        val decimalFormat = DecimalFormat()
        val sortedBy = Globals.islandValues!!.map.values.sortedByDescending { entry -> entry.worth }
        var counter = 1
        // Should be able to add the prefix if they want in message.json, right now, it doesn't match because the top island entries part does not have the prefix.
        if (Config.useIslandTopHeadMessage) info.message(Config.islandTopHeadMessage, false)
        if (Config.useIslandTopHeaderBar) {
            info.message(buildBar(Config.islandTopbarElement), false)
        }
        if (info.args.isNotEmpty()) {
            counter = info.getArgAsInt(0) ?: return
            if (counter <= 0) {
                info.message(Message.commandTopInvalidPage)
                return
            }
        }
        val startIndex = (counter - 1) * Config.commandTopPageSize
        if (startIndex > sortedBy.size - 1) {
            info.message(Message.commandTopIndexTooHigh)
            return
        }

        for (islandindex in startIndex..startIndex + Config.commandTopPageSize) {
            val entry = sortedBy.getOrNull(islandindex) ?: break
            val builder = StringBuilder()
            entry.matAmt.forEach { xmat -> builder.append("${xmat.key.name}: ${xmat.value}\n") }
            var tooltip = ""
            for (line in Config.islandTopTooltip) {
                var lineBasicParsed = line
                    .replace("{rank}", counter.toString())
                    .replace("{leader}", Data.islands[entry.islandID]!!.ownerTag)
                    .replace("{amount}", decimalFormat.format(entry.worth))
                entry.matAmt.forEach { xmat ->
                    lineBasicParsed = lineBasicParsed.replace("{${xmat.key.name}}", decimalFormat.format(xmat.value))
                }
                entry.spawnerAmt.forEach { entity ->
                    lineBasicParsed =
                        lineBasicParsed.replace("{${entity.key.name}}", decimalFormat.format(entity.value))
                }
                EntityType.values().forEach { entityType ->
                    lineBasicParsed = lineBasicParsed.replace("{${entityType.name}}", 0.toString())
                }
                XMaterial.values()
                    .forEach { xmat -> lineBasicParsed = lineBasicParsed.replace("{${xmat.name}}", 0.toString()) }
                tooltip += color("\n$lineBasicParsed")
            }
            val line = color(Config.islandTopLineFormat.replace("{rank}", counter.toString())
                .replace("{leader}", Data.islands[entry.islandID]!!.ownerTag)
                .replace("{amount}", decimalFormat.format(entry.worth)))
            if (info.isPlayer()) {
                JSONMessage.create(line).tooltip(tooltip).send(info.player)
            } else {
                info.message(line, false)
            }
        }

    }

    override fun getHelpInfo(): String {
        return Message.commandTopHelp
    }

}