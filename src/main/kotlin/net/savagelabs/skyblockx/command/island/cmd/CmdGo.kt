package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.event.player.PlayerTeleportEvent

class CmdGo : SCommand() {


    init {
        aliases.add("go")


        commandRequirements =
           CommandRequirementsBuilder().asIslandMember(true).asPlayer(true)
                .withPermission(Permission.GO).build()
    }


    override fun perform(info: CommandInfo) {
        val island = info.iPlayer!!.getIsland()!!
        info.message(Message.commandGoTeleporting)
        info.player!!.teleport(island.getIslandCenter(), PlayerTeleportEvent.TeleportCause.PLUGIN)
    }


    override fun getHelpInfo(): String {
        return Message.commandGoHelp
    }

}
