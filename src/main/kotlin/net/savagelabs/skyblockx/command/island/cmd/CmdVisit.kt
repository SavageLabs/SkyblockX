package net.savagelabs.skyblockx.command.island.cmd

import me.rayzr522.jsonmessage.JSONMessage
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.*
import net.savagelabs.skyblockx.persist.Message

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
            info.message(Message.instance.commandVisitPossibleLocationsHeader)
            for ((index, location) in possibleLocations.withIndex()) {
                JSONMessage.create(color(String.format(Message.instance.commandVisitPossibleLocationsFormat, index + 1, location)))
                    .suggestCommand("/is tp $location")
                    .tooltip("Click to run /is tp $location")
                    .send(info.player)
            }
            return
        }
        // Location was specified, so we can teleport to it.
        val targetLocation = getIslandByOwnerTag(info.args[0])
        if (targetLocation == null) {
            info.message(String.format(Message.instance.commandVisitThisIslandIsNotValid, info.args[0]))
            return
        }

        // Check if they can actually go to the location
        if (!targetLocation.allowVisitors && info.iPlayer!!.islandID != targetLocation.islandID
            && !info.iPlayer!!.isCoopedIsland(targetLocation.islandID)) {
            info.message(Message.instance.commandVisitNoPermission)
            return
        }

        teleportAsync(
            info.player!!,
            targetLocation.islandGoPoint!!.getLocation(),
            Runnable { info.message(String.format(Message.instance.commandVisitTeleporting, targetLocation.ownerTag)) })
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandVisitHelp
    }


}