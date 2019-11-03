package io.illyria.skyblockx.listener

import io.illyria.skyblockx.Globals
import io.illyria.skyblockx.core.IPlayer
import io.illyria.skyblockx.core.getIPlayer
import io.illyria.skyblockx.core.getIPlayerByName
import io.illyria.skyblockx.core.getIslandById
import io.illyria.skyblockx.persist.Data
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
            if (island.ownerUUID == event.player.uniqueId.toString()) {
                // Update tag if not equal.
                if (island.ownerTag != event.player.name) {
                    island.ownerTag = event.player.name
                    Globals.skyblockX.logger.info("Updated ${event.player.name}'s tag since they changed their name.")
                }
            }
            return
        }
        Data.IPlayers[event.player.uniqueId.toString()] = IPlayer(event.player.uniqueId.toString())
        Globals.skyblockX.logger.info("${event.player.name}'s IPlayer instance was created")
    }

    @EventHandler
    fun onPlayerDisconnect(event: PlayerQuitEvent) {
        val iPlayer = getIPlayerByName(event.player.name) ?: return
        for (authorizedUser in iPlayer.coopedPlayersAuthorized) {
            val island = iPlayer.getIsland() ?: continue
            island.removeCoopPlayer(authorizedUser)
        }
    }


}