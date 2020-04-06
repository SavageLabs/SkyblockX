package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerTeleportEvent

class CmdRemove : SCommand() {

    init {
        aliases.add("remove")
        aliases.add("expel")

        requiredArgs.add(Argument("player", 0, PlayerArgument()))

        commandRequirements =
           CommandRequirementsBuilder().asIslandMember(true).asPlayer(true)
                .withPermission(Permission.REMOVE).build()
    }

    override fun perform(info: CommandInfo) {
        val target = info.getArgAsIPlayer(0) ?: return
        // Remove the target's co-op status if theyre co-op.
        if (target.hasCoopIsland() && target.coopedIslandIds.contains(info.iPlayer!!.islandID)) {
            target.removeCoopIsland(info.iPlayer!!.getIsland()!!)
            target.message(Message.commandRemovedCoopStatus)
            info.message(String.format(Message.commandRemoveInvokerCoopRemoved, target.getPlayer().name))
        }


        val targetNewLocation =
            target.getIsland()?.getIslandCenter() ?: Bukkit.getWorld(Config.defaultWorld)!!.spawnLocation

        // Check if they're even on the island, to prevent abuse.
        if (!info.iPlayer!!.getIsland()!!.containsBlock(target.getPlayer().location)) {
            info.message(Message.commandRemoveInvokerPlayerNotOnIsland)
            return
        }

        // Teleport them cuz they're on the island.
        target.getPlayer().teleport(targetNewLocation, PlayerTeleportEvent.TeleportCause.PLUGIN)
        info.message(String.format(Message.commandRemoveInvokerSuccess, target.getPlayer().name))


    }


    override fun getHelpInfo(): String {
        return Message.commandRemoveHelp
    }


}