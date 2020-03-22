package net.savagelabs.skyblockx.command.island.cmd.home

import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.persist.Message
import org.bukkit.event.player.PlayerTeleportEvent

class CmdHomeGo : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("go")

        requiredArgs.add(Argument("home-name", 0, HomeArgument()))

        commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().asIslandMember(true).build()
    }

    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        val home = info.args[0]
        if (!info.island!!.hasHome(home)) {
            info.message(Message.commandHomeDoesNotExist)
            return
        }

        info.player!!.teleport(info.island!!.getHome(home)!!.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN)
        info.message(String.format(Message.commandHomeGoSuccess, home))
    }

    override fun getHelpInfo(): String {
        return Message.commandHomeGoHelp
    }


}