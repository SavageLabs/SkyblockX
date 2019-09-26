package net.savagellc.savageskyblock.core

import net.savagellc.savageskyblock.persist.Config
import net.savagellc.savageskyblock.persist.Data
import net.savagellc.savageskyblock.persist.data.SLocation
import net.savagellc.savageskyblock.sedit.SkyblockEdit
import net.savagellc.savageskyblock.world.Point
import net.savagellc.savageskyblock.world.spiral
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

data class Island(val islandID: Int, val point: Point, val ownerUUID: String, var ownerTag: String) {

    val minLocation = point.getLocation()
    val maxLocation = point.getLocation()
        .add(Config.islandMaxSizeInBlocks.toDouble(), 256.toDouble(), Config.islandMaxSizeInBlocks.toDouble())

    var homes = HashMap<String, SLocation>()

    fun resetAllHomes() {
        homes.clear()
    }

    fun addHome(name: String, sLocation: SLocation) {
        // TODO: Check permissions and implement max homes feature.
        homes[name] = sLocation
    }

    fun removeHome(name: String) {
        homes.remove(name)
    }

    fun getHome(name: String): SLocation? {
        return homes[name]
    }

    fun hasHome(name: String): Boolean {
        return homes.containsKey(name)
    }


    fun getOwnerIPlayer(): IPlayer? {
        return Data.IPlayers[ownerUUID]
    }

    fun getIslandSpawn(): Location {
        return Location(
            minLocation.world,
            minLocation.x + (Config.islandMaxSizeInBlocks / 2),
            101.toDouble(),
            minLocation.z + (Config.islandMaxSizeInBlocks / 2)
        )
    }

    fun fillIsland(material: Material = Material.BONE_BLOCK) {
        val origin = point.getLocation()
        for (x in origin.x.toInt()..(origin.x + Config.islandMaxSizeInBlocks).toInt()) {
            for (z in origin.z.toInt()..(origin.z + Config.islandMaxSizeInBlocks).toInt()) {
                origin.world!!.getBlockAt(Location(origin.world, x.toDouble(), 100.toDouble(), z.toDouble())).type =
                    material
            }
        }
    }

    fun containsBlock(v: Location): Boolean {
        if (v.world !== minLocation.world) return false
        val x = v.x
        val y = v.y
        val z = v.z
        return x >= minLocation.blockX && x < maxLocation.blockX + 1 && y >= minLocation.blockY && y < maxLocation.blockY + 1 && z >= minLocation.blockZ && z < maxLocation.blockZ + 1
    }

    /**
     * This is built for checking bounds without checking the Y axis as it is not needed.
     */
    fun locationInIsland(v: Location): Boolean {
        val x = v.x
        val z = v.z
        return x >= minLocation.blockX && x < maxLocation.blockX + 1 && z >= minLocation.blockZ && z < maxLocation.blockZ + 1
    }

}

fun getIslandById(id: Int): Island? {
    return Data.islands[id]
}

fun getIslandFromLocation(location: Location): Island? {
    for (island in Data.islands.values) {
        if (island.locationInIsland(location)) {
            return island
        }
    }
    return null
}

fun getIslandByOwnerTag(ownerTag: String): Island? {
    for (island in Data.islands.values) {
        if (island.ownerTag == ownerTag) {
            return island
        }
    }
    return null
}

fun createIsland(player: Player?, schematic: String): Island {
    val island = Island(
        Data.nextIslandID,
        spiral(Data.nextIslandID),
        player?.uniqueId.toString(),
        player?.name ?: "player name was null :shrug:"
    )
    Data.islands[Data.nextIslandID] = island
    Data.nextIslandID++
    // Make player null because we dont want to send them the SkyblockEdit Engine's success upon pasting the island.
    SkyblockEdit().pasteIsland(schematic, island.getIslandSpawn(), null)
    if (player != null) {
        val iPlayer = getIPlayer(player)
        iPlayer.assignIsland(island)
    }
    return island
}

fun deleteIsland(player: Player) {
    val iPlayer = getIPlayer(player)
    if (iPlayer.hasIsland()) {
        Data.islands.remove(iPlayer.getIsland()!!.islandID)
        iPlayer.unassignIsland()
    }
}