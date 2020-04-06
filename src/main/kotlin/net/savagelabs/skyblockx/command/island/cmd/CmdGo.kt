package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.teleportAsync
import net.savagelabs.skyblockx.persist.Message

class CmdGo : SCommand() {


    init {
        aliases.add("go")


        commandRequirements =
           CommandRequirementsBuilder().asIslandMember(true).asPlayer(true)
                .withPermission(Permission.GO).build()
    }


    override fun perform(info: CommandInfo) {
        val island = info.iPlayer!!.getIsland()!!
        teleportAsync(
            info.player!!,
            island.islandGoPoint.getLocation(),
            Runnable { info.message(Message.commandGoTeleporting) })
    }


    override fun getHelpInfo(): String {
        return Message.commandGoHelp
    }

}
