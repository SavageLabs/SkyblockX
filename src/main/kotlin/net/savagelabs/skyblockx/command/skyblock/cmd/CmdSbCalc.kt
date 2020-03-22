package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Island
import io.illyria.skyblockx.core.IslandTopInfo
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.runIslandCalc
import io.illyria.skyblockx.persist.Data
import io.illyria.skyblockx.persist.Message
import org.bukkit.Bukkit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class CmdSbCalc : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("calc")


        commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.ADMIN_CALC).build()
    }

    @ExperimentalTime
    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        info.message(Message.commandSkyblockCalcStart)
        Bukkit.getScheduler().runTask(_root_ide_package_.net.savagelabs.skyblockx.Globals.skyblockX, Runnable {
            runIslandCalc()
            info.message(String.format(Message.commandSkyblockCalcDone, _root_ide_package_.net.savagelabs.skyblockx.Globals.islandValues?.map?.size ?: "N/A"))
        })
    }

    override fun getHelpInfo(): String {
        return Message.commandSkyblockCalcHelp
    }
}