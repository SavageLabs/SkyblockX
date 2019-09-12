package net.savagellc.savageskyblock.listener

import net.savagellc.savageskyblock.core.IPlayer
import net.savagellc.savageskyblock.persist.Data
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class DataListener : Listener {


    @EventHandler
    fun onPlayerConnect(event: PlayerJoinEvent) {
        if (Data.IPlayers.containsKey(event.player.uniqueId.toString())) {
            println("${event.player.name} joined and his IPlayer instance already exists")
            return
        }
        Data.IPlayers[event.player.uniqueId.toString()] = IPlayer(event.player.uniqueId.toString())
        println("${event.player.name}'s IPlayer instance was created")

    }




}