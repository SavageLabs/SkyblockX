package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.command.*
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import kotlin.time.ExperimentalTime

class CmdCalc : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("calc")

        commandRequirements =
            SCommandRequirementsBuilder().withPermission(Permission.CALC).asIslandMember(true).build()
    }

    @ExperimentalTime
    override fun perform(info: SCommandInfo) {
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