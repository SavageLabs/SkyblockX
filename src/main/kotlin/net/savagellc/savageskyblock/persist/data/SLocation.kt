package net.savagellc.savageskyblock.persist.data

import net.savagellc.savageskyblock.persist.Config
import org.bukkit.Bukkit
import org.bukkit.Location

data class SLocation(val x: Double, val y: Double, val z: Double) {
    fun getLocation(): Location {
        return Location(Bukkit.getWorld(Config.skyblockWorldName), x, y, z)
    }

}