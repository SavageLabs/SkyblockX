package net.savagelabs.skyblockx.command.island.cmd.home

import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.event.player.PlayerTeleportEvent
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.SCommand

class CmdHomeGo : SCommand() {

    init {
        aliases.add("go")

        requiredArgs.add(Argument("home-name", 0, HomeArgument()))

        commandRequirements = CommandRequirementsBuilder().asIslandMember(true).build()
    }

    override fun perform(info: CommandInfo) {
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