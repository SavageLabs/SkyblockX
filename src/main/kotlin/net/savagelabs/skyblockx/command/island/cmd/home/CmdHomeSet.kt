package net.savagelabs.skyblockx.command.island.cmd.home

import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.persist.data.getSLocation

class CmdHomeSet : SCommand() {


    init {
        aliases.add("set")

        // The home is not set yet, so we cannot suggest those.
        this.requiredArgs.add(Argument("home-name", 0, StringArgument()))

        this.commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.HOME)
                .asIslandMember(true).build()
    }

    override fun perform(info: CommandInfo) {
        if (!info.iPlayer!!.getIsland()!!.canHaveMoreHomes()) {
            info.message(Message.instance.commandHomeCannotHaveMoreHomes)
            return
        }

        // lowercase the home name for uniformity :)
        val homeName = info.args[0].toLowerCase()
        val playerLocation = info.player!!.location

        // Check if the island even contains the location for the new home.
        if (!info.island!!.containsBlock(playerLocation)) {
            info.message(Message.instance.commandHomeSetNotInIsland)
            return
        }
        info.iPlayer!!.getIsland()!!.addHome(homeName, getSLocation(playerLocation))
        info.message(String.format(Message.instance.commandHomeHomeSet, homeName))
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandHomeSetHelp
    }

}