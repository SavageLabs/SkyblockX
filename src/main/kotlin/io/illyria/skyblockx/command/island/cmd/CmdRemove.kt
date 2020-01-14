package io.illyria.skyblockx.command.island.cmd

import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message
import org.bukkit.Bukkit

class CmdRemove : SCommand() {

    init {
        aliases.add("remove")
        aliases.add("expel")

        requiredArgs.add(Argument("player", 0, PlayerArgument()))

        commandRequirements =
           CommandRequirementsBuilder().asIslandMember(true).asPlayer(true)
                .withPermission(Permission.REMOVE).build()
    }

    override fun perform(info: io.illyria.skyblockx.command.CommandInfo) {
        val target = info.getArgAsIPlayer(0) ?: return
        // Remove the target's co-op status if theyre co-op.
        if (target.hasCoopIsland() && target.coopedIslandIds.contains(info.iPlayer!!.islandID)) {
            target.removeCoopIsland(info.iPlayer!!.getIsland()!!)
            target.message(Message.commandRemovedCoopStatus)
            info.message(String.format(Message.commandRemoveInvokerCoopRemoved, target.getPlayer().name))
        }

        // TODO: Implement a default spawn mechanic to set the default spawn location, so that it is more elegant.

        val targetNewLocation = target.getIsland()?.getIslandCenter() ?: Bukkit.getWorld("world")!!.spawnLocation

        // Check if they're even on the island, to prevent abuse.
        if (!info.iPlayer!!.getIsland()!!.containsBlock(target.getPlayer().location)) {
            info.message(Message.commandRemoveInvokerPlayerNotOnIsland)
            return
        }

        // Teleport them cuz they're on the island.
        target.getPlayer().teleport(targetNewLocation)
        info.message(String.format(Message.commandRemoveInvokerSuccess, target.getPlayer().name))


    }


    override fun getHelpInfo(): String {
        return Message.commandRemoveHelp
    }


}