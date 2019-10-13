package io.illyria.skyblockx.command.cmd

import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.core.createIsland
import io.illyria.skyblockx.gui.IslandCreateGUI
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Message
import me.rayzr522.jsonmessage.JSONMessage


class CmdCreate : io.illyria.skyblockx.command.SCommand() {

    init {
        aliases.add("create")

        optionalArgs.add(Argument("island-type", 0, StringArgument()))

        commandRequirements =
            io.illyria.skyblockx.command.CommandRequirementsBuilder().asIslandMember(false).asPlayer(true)
                .withPermission(Permission.CREATE).build()
    }

    override fun perform(info: io.illyria.skyblockx.command.CommandInfo) {
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
        IslandCreateGUI().showGui(info.player!!)
    }


    override fun getHelpInfo(): String {
        return Message.commandCreateHelp
    }

}