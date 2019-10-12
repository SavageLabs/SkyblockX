package net.savagellc.savageskyblock.command.cmd.home

import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.persist.Message
import net.savagellc.savageskyblock.persist.data.getSLocation

class CmdHomeSet : SCommand() {


    init {
        aliases.add("set")

        // The home is not set yet, so we cannot suggest those.
        this.requiredArgs.add(Argument("home-name", 0, StringArgument()))

        this.commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.HOME).asIslandMember(true).build()
    }

    override fun perform(info: CommandInfo) {
        if (!info.iPlayer!!.getIsland()!!.canHaveMoreHomes()) {
            info.message(Message.commandHomeCannotHaveMoreHomes)
            return
        }

        // lowercase the home name for uniformity :)
        val homeName = info.args[0].toLowerCase()
        val playerLocation = info.player!!.location

        // Check if the island even contains the location for the new home.
        if (!info.island!!.containsBlock(playerLocation)) {
            info.message(Message.commandHomeSetNotInIsland)
            return
        }


        info.iPlayer!!.getIsland()!!.addHome(homeName, getSLocation(playerLocation))
        info.message(String.format(Message.commandHomeHomeSet, homeName))
    }

    override fun getHelpInfo(): String {
        return Message.commandHomeSetHelp
    }

}