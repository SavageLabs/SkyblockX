package net.savagellc.savageskyblock.command.cmd

import me.rayzr522.jsonmessage.JSONMessage
import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.core.color
import net.savagellc.savageskyblock.core.createIsland
import net.savagellc.savageskyblock.gui.IslandCreateGUI
import net.savagellc.savageskyblock.persist.Config
import net.savagellc.savageskyblock.persist.Message


class CmdCreate : SCommand() {

    init {
        aliases.add("create")

        optionalArgs.add(Argument("island-type", 0, StringArgument()))

        commandRequirements =
            CommandRequirementsBuilder().asIslandMember(false).asPlayer(true).withPermission(Permission.CREATE).build()
    }

    override fun perform(info: CommandInfo) {
        if (info.iPlayer!!.hasIsland()) {
            info.message(Message.commandCreateAlreadyHaveAnIsland)
            return
        }

        if (info.args.size == 1) {
            val listOfNames = ArrayList<String>()
            for (islandCreationInfo in Config.islandCreateGUIIslandTypes) {
                if (islandCreationInfo.name.equals(info.args[0], true)) {
                    createIsland(info.player, islandCreationInfo.structureFile.replace(".structure", ""))
                    return
                }
                // Populate the list for usage if the command sender didnt send a valid name.
                listOfNames.add(islandCreationInfo.name)
            }

            // If we're here then the name specified was not used, so we can just send them a helpful message to tell them their choices.
            info.message(Message.commandCreateCLIHeader)
            for ((index, islandName) in listOfNames.withIndex()) {
                JSONMessage.create(color(String.format(Message.commandCreateCLIFormat, index + 1, islandName)))
                    .tooltip(color(String.format(Message.commandCreateCLIFormatTooltip, islandName)))
                    .suggestCommand("/is create $islandName")
                    .send(info.player)
            }

            return
        }

        val islandCreateGUI = IslandCreateGUI()
        islandCreateGUI.buildGui()
        islandCreateGUI.showGui(info.player!!)
    }


    override fun getHelpInfo(): String {
        return Message.commandCreateHelp
    }

}