package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
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
            info.message(String.format(Message.instance.commandCalcCooldown, (Config.instance.islandTopManualCalcCooldownMiliseconds / 1000) - cooldown))
            return
        }
        info.island!!.lastManualCalc = System.currentTimeMillis()
        val calcInfo = info.island!!.calcIsland()
        SkyblockX.islandValues?.map?.put(info.island!!.islandID, calcInfo)
        info.message(Message.instance.commandCalcMessage)
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandCalcHelp
    }
}