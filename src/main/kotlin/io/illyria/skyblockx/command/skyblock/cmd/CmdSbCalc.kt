package io.illyria.skyblockx.command.skyblock.cmd

import io.illyria.skyblockx.Globals
import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Island
import io.illyria.skyblockx.core.IslandTopInfo
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.runIslandCalc
import io.illyria.skyblockx.persist.Data
import io.illyria.skyblockx.persist.Message
import org.bukkit.Bukkit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class CmdSbCalc : SCommand() {

    init {
        aliases.add("calc")


        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.ADMIN_CALC).build()
    }

    @ExperimentalTime
    override fun perform(info: CommandInfo) {
        info.message(Message.commandSkyblockCalcStart)
        Bukkit.getScheduler().runTask(Globals.skyblockX, Runnable {
            runIslandCalc()
            info.message(String.format(Message.commandSkyblockCalcDone, Globals.islandValues?.map?.size ?: "N/A"))
        })
    }

    override fun getHelpInfo(): String {
        return Message.commandSkyblockCalcHelp
    }
}