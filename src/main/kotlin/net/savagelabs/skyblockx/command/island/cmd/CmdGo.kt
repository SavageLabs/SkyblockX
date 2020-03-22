package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message
import org.bukkit.event.player.PlayerTeleportEvent

class CmdGo : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {


    init {
        aliases.add("go")


        commandRequirements =
           _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().asIslandMember(true).asPlayer(true)
                .withPermission(Permission.GO).build()
    }


    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        val island = info.iPlayer!!.getIsland()!!
        info.message(Message.commandGoTeleporting)
        info.player!!.teleport(island.getIslandCenter(), PlayerTeleportEvent.TeleportCause.PLUGIN)
    }


    override fun getHelpInfo(): String {
        return Message.commandGoHelp
    }

}
