package net.savagelabs.skyblockx.persist.data

import net.savagelabs.skyblockx.persist.Config
import org.bukkit.Bukkit
import org.bukkit.Location

/**
 * Meant ONLY for location inside the skyblock world.
 * This class's sole purpose is to handle changes between worldname in config.json.
 * It allows the player to change the worldname in server and in the config.json.
 */
data class SLocation(val x: Double, val y: Double, val z: Double) {
    fun getLocation(): Location {
        return Location(Bukkit.getWorld(Config.skyblockWorldName), x, y, z)
    }
}


fun getSLocation(location: Location): SLocation {
    return SLocation(location.x, location.y, location.z)
}