package io.illyria.skyblockx.core

import io.illyria.skyblockx.Globals
import io.illyria.skyblockx.persist.Config
import net.prosavage.baseplugin.WorldBorderUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Player

fun color(message: String): String {
    return ChatColor.translateAlternateColorCodes('&', message)
}

fun updateWorldBorder(player: Player, location: Location, delay: Long) {
    if (location.world?.name != Config.skyblockWorldName) {
        val worldBorder = location.world?.worldBorder
        Globals.worldBorderUtil.sendWorldBorder(
            player,
            WorldBorderUtil.Color.Off,
            worldBorder!!.size,
            worldBorder.center
        )
    } else {

        Bukkit.getScheduler()
            .runTaskLater(
                Globals.skyblockX,
                Runnable {
                    val islandFromLocation = getIslandFromLocation(location)
                    val iPlayer = getIPlayer(player)
                    if (islandFromLocation != null) {
                        WorldBorderUtil(Globals.skyblockX).sendWorldBorder(
                            player,
                            iPlayer.borderColor,
                            Config.islandMaxSizeInBlocks.toDouble(),
                            islandFromLocation.getIslandCenter()
                        )
                    }
                },
                delay
            )
    }
}