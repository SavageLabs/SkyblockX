package io.illyria.skyblockx.command.island.cmd

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.core.getIslandById
import io.illyria.skyblockx.core.getIslandByOwnerTag
import io.illyria.skyblockx.persist.Message
import me.rayzr522.jsonmessage.JSONMessage
import org.bukkit.event.player.PlayerTeleportEvent

class CmdVisit : SCommand() {

    init {
        aliases.add("visit")
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
            info.message(Message.commandVisitPossibleLocationsHeader)
            for ((index, location) in possibleLocations.withIndex()) {
                JSONMessage.create(color(String.format(Message.commandVisitPossibleLocationsFormat, index + 1, location)))
                    .suggestCommand("/is tp $location")
                    .tooltip("Click to run /is tp $location")
                    .send(info.player)
            }
            return
        }
        // Location was specified, so we can teleport to it.
        val targetLocation = getIslandByOwnerTag(info.args[0])
        if (targetLocation == null) {
            info.message(String.format(Message.commandVisitThisIslandIsNotValid, info.args[0]))
            return
        }

        // Check if they can actually go to the location
        if (!targetLocation.allowVisitors && info.iPlayer!!.islandID != targetLocation.islandID
            && !info.iPlayer!!.isCoopedIsland(targetLocation.islandID)) {
            info.message(Message.commandVisitNoPermission)
            return
        }

        // TODO: Allow them to set island teleport location maybe? idk.
        info.player!!.teleport(targetLocation.getIslandCenter(), PlayerTeleportEvent.TeleportCause.PLUGIN)
        info.message(String.format(Message.commandVisitTeleporting, targetLocation.ownerTag))


    }

    override fun getHelpInfo(): String {
        return Message.commandVisitHelp
    }


}