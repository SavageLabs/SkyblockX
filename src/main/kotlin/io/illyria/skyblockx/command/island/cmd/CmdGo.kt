package io.illyria.skyblockx.command.island.cmd

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message
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
