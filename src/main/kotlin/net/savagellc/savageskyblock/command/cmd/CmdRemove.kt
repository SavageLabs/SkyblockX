package net.savagellc.savageskyblock.command.cmd

import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.persist.Message
import org.bukkit.Bukkit

class CmdRemove : SCommand() {

    init {
        aliases.add("remove")
        aliases.add("expel")

        requiredArgs.add("player")

        commandRequirements = CommandRequirementsBuilder().asIslandMember(true).asPlayer(true).withPermission(Permission.REMOVE).build()
    }

    override fun perform(commandInfo: CommandInfo) {
        val target = commandInfo.getArgAsIPlayer(0) ?: return
        // TODO: Remove target even if they're not co-op ( Was not implemented because we do not have a default spawn mechanic set yet. )
        if (target.hasCoopIsland() && target.coopedIslandIds.contains(commandInfo.iPlayer!!.islandID)) {
            target.removeCoopIsland(commandInfo.iPlayer!!.islandID)
            target.message(Message.commandRemovedCoopStatus)
            commandInfo.message(String.format(Message.commandRemoveInvokerCoopRemoved, target.getPlayer().name))
        }
        // TODO: Implement a default spawn mechanic to set the default spawn location, so that it is more elegant.
        val targetNewLocation = target.getIsland()?.getIslandSpawn() ?: Bukkit.getWorld("world")!!.spawnLocation
        target.getPlayer().teleport(targetNewLocation)

        commandInfo.message(String.format(Message.commandRemoveInvokerSuccess, target.getPlayer().name))
    }


    override fun getHelpInfo(): String {
        return Message.commandRemoveHelp
    }


}