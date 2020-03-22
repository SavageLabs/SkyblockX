package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.IslandTopInfo
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.runIslandCalc
import net.savagelabs.skyblockx.persist.Data
import net.savagelabs.skyblockx.persist.Message
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