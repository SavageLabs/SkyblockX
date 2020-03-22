package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import org.ocpsoft.prettytime.Duration
import org.ocpsoft.prettytime.PrettyTime
import java.util.*
import kotlin.time.ExperimentalTime

class CmdCalc : SCommand() {

    init {
        aliases.add("calc")

        commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.CALC).asIslandMember(true).build()
    }

    @ExperimentalTime
    override fun perform(info: CommandInfo) {
        if (!info.island!!.canManualCalc()) {
            val cooldown = (System.currentTimeMillis() - info.island!!.lastManualCalc ) / 1000
            info.message(String.format(Message.commandCalcCooldown, (Config.islandTopManualCalcCooldownMiliseconds / 1000) - cooldown))
            return
        }
        info.island!!.lastManualCalc = System.currentTimeMillis()
        val calcInfo = info.island!!.calcIsland()
        Globals.islandValues?.map?.put(info.island!!.islandID, calcInfo)
        info.message(Message.commandCalcMessage)
    }

    override fun getHelpInfo(): String {
        return Message.commandCalcHelp
    }
}