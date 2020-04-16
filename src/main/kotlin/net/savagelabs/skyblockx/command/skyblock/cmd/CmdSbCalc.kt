package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message
import kotlin.time.ExperimentalTime

class CmdSbCalc : SCommand() {

    init {
        aliases.add("calc")


        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.ADMIN_CALC).build()
    }

    @ExperimentalTime
    override fun perform(info: CommandInfo) {
        info.message(Message.commandSkyblockCalcStart)
        Globals.skyblockX.startIslandTopTask()

    }

    override fun getHelpInfo(): String {
        return Message.commandSkyblockCalcHelp
    }
}