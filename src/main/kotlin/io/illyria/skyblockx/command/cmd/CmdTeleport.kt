package io.illyria.skyblockx.command.cmd

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.core.getIslandById
import io.illyria.skyblockx.core.getIslandByOwnerTag
import io.illyria.skyblockx.persist.Message
import me.rayzr522.jsonmessage.JSONMessage

class CmdTeleport : SCommand() {

    init {
        aliases.add("teleport")
        aliases.add("tp")


        optionalArgs.add(Argument("island owner's name", 0, PlayerArgument()))

        commandRequirements =
            CommandRequirementsBuilder().asPlayer(true).withPermission(Permission.TELEPORT)
                .build()
    }


    override fun perform(info: CommandInfo) {
        // list possible locations if empty.
        if (info.args.isEmpty()) {
            val possibleLocations = ArrayList<String>()
            // If we own an island, show ours.
            if (info.iPlayer!!.hasIsland()) {
                possibleLocations.add(info.iPlayer!!.getIsland()!!.ownerTag)
            }
            // Show co-op islands
            if (info.iPlayer!!.hasCoopIsland()) {
                for (id in info.iPlayer!!.coopedIslandIds) {
                    val island = getIslandById(id) ?: continue
                    possibleLocations.add(island.ownerTag)
                }
            }

            // Message them.
            info.message(Message.commandTpPossibleLocationsHeader)
            for ((index, location) in possibleLocations.withIndex()) {
                JSONMessage.create(color(String.format(Message.commandTpPossibleLocationsFormat, index + 1, location)))
                    .suggestCommand("/is tp $location")
                    .tooltip("Click to run /is tp $location")
                    .send(info.player)
            }
            return
        }
        // Location was specified, so we can teleport to it.
        val targetLocation = getIslandByOwnerTag(info.args[0])
        if (targetLocation == null) {
            info.message(String.format(Message.commandTpThisIslandIsNotValid, info.args[0]))
            return
        }

        // Check if they can actually go to the location
        if (!targetLocation.allowVisitors && info.iPlayer!!.islandID != targetLocation.islandID && !info.iPlayer!!.isCoopedIsland(targetLocation.islandID)) {
            info.message(Message.commandTpNoPermission)
            return
        }

        // TODO: Allow them to set island teleport location maybe? idk.
        info.player!!.teleport(targetLocation.getIslandCenter())
        info.message(String.format(Message.commandTpTeleporting, targetLocation.ownerTag))


    }

    override fun getHelpInfo(): String {
        return Message.commandTpHelp
    }


}