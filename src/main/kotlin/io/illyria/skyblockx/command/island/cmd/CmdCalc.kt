package io.illyria.skyblockx.command.island.cmd

import io.illyria.skyblockx.Globals
import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Message
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