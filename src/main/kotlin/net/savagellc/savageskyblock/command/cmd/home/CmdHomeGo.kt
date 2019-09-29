package net.savagellc.savageskyblock.command.cmd.home

import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.persist.Message
import net.savagellc.savageskyblock.persist.data.getSLocation

class CmdHomeGo : SCommand() {

    init {
        aliases.add("go")

        requiredArgs.add("home-name")

        commandRequirements = CommandRequirementsBuilder().asIslandMember(true).build()
    }

    override fun perform(info: CommandInfo) {
        val home = info.args[0]
        if (!info.island!!.hasHome(home)) {
            info.message(Message.commandHomeDoesNotExist)
            return
        }

        info.player!!.teleport(info.island!!.getHome(home)!!.getLocation())
        info.message(String.format(Message.commandHomeGoSuccess, home))
    }

    override fun getHelpInfo(): String {
        return Message.commandHomeGoHelp
    }


}