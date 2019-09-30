package net.savagellc.savageskyblock.listener

import net.savagellc.savageskyblock.Globals
import net.savagellc.savageskyblock.core.IPlayer
import net.savagellc.savageskyblock.core.getIPlayer
import net.savagellc.savageskyblock.core.getIslandById
import net.savagellc.savageskyblock.persist.Data
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class DataListener : Listener {


    @EventHandler
    fun onPlayerConnect(event: PlayerJoinEvent) {
        if (Data.IPlayers.containsKey(event.player.uniqueId.toString())) {
            // Update owner tag changes :)
            val island = getIslandById(Data.IPlayers[event.player.uniqueId.toString()]!!.islandID) ?: return
            // Check if the player is an island owner.
            if (island!!.ownerUUID == event.player.uniqueId.toString()) {
                // Update tag if not equal.
                if (island.ownerTag != event.player.name) {
                    island.ownerTag = event.player.name
                    Globals.savageSkyblock.logger.info("Updated ${event.player.name}'s tag since they changed their name.")
                }
            }
            return
        }
        Data.IPlayers[event.player.uniqueId.toString()] = IPlayer(event.player.uniqueId.toString())
        Globals.savageSkyblock.logger.info("${event.player.name}'s IPlayer instance was created")
    }

    @EventHandler
    fun onPlayerDisconnect(event: PlayerQuitEvent) {
        val iPlayer = getIPlayer(event.player)

        for (authorizedUser in iPlayer.coopedPlayersAuthorized) {
            val island = iPlayer.getIsland() ?: continue
            island.removeCoopPlayer(authorizedUser)
        }
    }


}