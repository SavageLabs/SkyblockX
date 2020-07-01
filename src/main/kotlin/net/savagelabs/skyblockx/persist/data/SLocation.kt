package net.savagelabs.skyblockx.persist.data

import com.fasterxml.jackson.annotation.JsonIgnore
import net.savagelabs.skyblockx.persist.Config
import org.bukkit.Bukkit
import org.bukkit.Location

/**
 * Meant ONLY for location inside the skyblock world.
 * This class's sole purpose is to handle changes between worldname in Config.instance.json.
 * net.savagelabs.savagepluginx.item.It allows the player to change the worldname in server and in the Config.instance.json.
 */
data class SLocation(val x: Double, val y: Double, val z: Double) {
    @JsonIgnore
    fun getLocation(): Location {
        return Location(Bukkit.getWorld(Config.instance.skyblockWorldName), x, y, z)
    }
}


fun getSLocation(location: Location): SLocation {
    return SLocation(location.x, location.y, location.z)
}