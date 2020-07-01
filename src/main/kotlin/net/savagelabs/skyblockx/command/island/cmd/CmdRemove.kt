package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.PlayerArgument
import net.savagelabs.skyblockx.command.*
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.teleportAsync
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.Bukkit

class CmdRemove : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("remove")
        aliases.add("expel")

        requiredArgs.add(Argument("player", 0, PlayerArgument()))

        commandRequirements =
           SCommandRequirementsBuilder().asIslandMember(true).asPlayer(true)
                .withPermission(Permission.REMOVE).build()
    }

    override fun perform(info: SCommandInfo) {
        val target = info.getArgAsIPlayer(0) ?: return
        // Remove the target's co-op status if theyre co-op.
        if (target.hasCoopIsland() && target.coopedIslandIds.contains(info.iPlayer!!.islandID)) {
            target.removeCoopIsland(info.iPlayer!!.getIsland()!!)
            target.message(Message.instance.commandRemovedCoopStatus)
            info.message(String.format(Message.instance.commandRemoveInvokerCoopRemoved, target.getPlayer().name))
        }


        val targetNewLocation =
            target.getIsland()?.getIslandCenter() ?: Bukkit.getWorld(Config.instance.defaultWorld)!!.spawnLocation

        // Check if they're even on the island, to prevent abuse.
        if (!info.iPlayer!!.getIsland()!!.containsBlock(target.getPlayer().location)) {
            info.message(Message.instance.commandRemoveInvokerPlayerNotOnIsland)
            return
        }

        // Teleport them cuz they're on the island.
        teleportAsync(target.getPlayer(), targetNewLocation, Runnable { })
        info.message(String.format(Message.instance.commandRemoveInvokerSuccess, target.getPlayer().name))


    }


    override fun getHelpInfo(): String {
        return Message.instance.commandRemoveHelp
    }


}