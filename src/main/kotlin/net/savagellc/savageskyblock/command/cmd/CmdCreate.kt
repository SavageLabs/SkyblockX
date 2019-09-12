package net.savagellc.savageskyblock.command.cmd

import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.core.createIsland
import net.savagellc.savageskyblock.persist.Message
import org.bukkit.Material


class CmdCreate : SCommand() {

    init {
        aliases.add("create")

        commandRequirements = CommandRequirementsBuilder().asIslandMember(false).asPlayer(true).withPermission(Permission.CREATE).build()
    }

    override fun perform(commandInfo: CommandInfo) {
        if (commandInfo.iPlayer!!.hasIsland()) {
            commandInfo.message(Message.commandCreateAlreadyHaveAnIsland)
            return
        }
        commandInfo.message("Creating island and filling it.")
        val island = createIsland(commandInfo.player!!)

        island.fillIsland(Material.GREEN_WOOL)
        commandInfo.message("Success.")

    }


    override fun getHelpInfo(): String {
       return Message.commandCreateHelp
    }

}