package net.savagelabs.skyblockx.command.island.cmd

import me.rayzr522.jsonmessage.JSONMessage
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.createIsland
import net.savagelabs.skyblockx.gui.IslandCreateGUI
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message


class CmdCreate : SCommand() {

    init {
        aliases.add("create")

        optionalArgs.add(Argument("island-type", 0, StringArgument()))

        commandRequirements =
           CommandRequirementsBuilder().asIslandMember(false).asPlayer(true)
                .withPermission(Permission.CREATE).build()
    }

    override fun perform(info: CommandInfo) {
        if (info.iPlayer!!.hasIsland()) {
            info.message(Message.commandCreateAlreadyHaveAnIsland)
            return
        }

        if (info.isBypassing().not() && info.iPlayer!!.lastIslandResetTime != -1L) {
            val timeNow = System.currentTimeMillis() / 1000
            val resetTime = info.iPlayer!!.lastIslandResetTime
            val difference = timeNow - resetTime
            if (difference < Config.islandResetCoolDownSeconds) {
                info.message(Message.commandCreateCooldown, (Config.islandResetCoolDownSeconds - difference).toString())
                return
            }
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