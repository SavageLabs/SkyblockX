package net.savagellc.savageskyblock.core

import net.savagellc.savageskyblock.persist.Config
import net.savagellc.savageskyblock.persist.Data
import net.savagellc.savageskyblock.persist.Message
import net.savagellc.savageskyblock.persist.data.SLocation
import net.savagellc.savageskyblock.sedit.SkyblockEdit
import net.savagellc.savageskyblock.world.Point
import net.savagellc.savageskyblock.world.spiral
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

import kotlin.collections.HashSet


data class Island(val islandID: Int, val point: Point, val ownerUUID: String, var ownerTag: String) {

    val minLocation = point.getLocation()
    val maxLocation = point.getLocation()
        .add(Config.islandMaxSizeInBlocks.toDouble(), 256.toDouble(), Config.islandMaxSizeInBlocks.toDouble())

    var maxCoopPlayers = Config.defaultMaxCoopPlayers
    var maxIslandHomes = Config.defaultMaxIslandHomes


    /**
     * This set does not have methods on purpose to discourage developers from modifying it, as we need a player to authorize the co-op procedure and because it won't actually affect the co-op status of a player.
     */
    var currentCoopPlayers = HashSet<UUID>()

    /**
     * The whole point of this function is because we cache coop players on the side of the player.
     */
    fun canHaveMoreCoopPlayers(): Boolean {
        // Check the owner's permission for it.
        // Can only check if they online tho :/, so we gotta cache it in maxCoopPlayers

        // If the instance is null, the player is offline.
        val owner = Bukkit.getOfflinePlayer(UUID.fromString(ownerUUID)).player
        if (owner != null) {
            maxCoopPlayers = getMaxPermission(owner, "savageskyblock.limits.coop-players")
            if (maxCoopPlayers == 0) {
                maxCoopPlayers = Config.defaultMaxCoopPlayers
            }
        }

        if (maxCoopPlayers == -1) {
            return true
        }
        // Return if the current size is less than the max, if so we gucci.
        return currentCoopPlayers.size < maxCoopPlayers
    }

    fun canHaveMoreHomes(): Boolean {
        // If the instance is null, the player is offline.
        val owner = Bukkit.getOfflinePlayer(UUID.fromString(ownerUUID)).player
        if (owner != null) {
            maxIslandHomes = getMaxPermission(owner, "savageskyblock.limits.island-homes")
            if (maxIslandHomes == 0) {
                maxIslandHomes = Config.defaultMaxIslandHomes
            }
        }

        if (maxIslandHomes == -1) {
            return true
        }

        // Return if the current size is less than the max, if so we gucci.
        return homes.size < maxIslandHomes
    }

    var homes = HashMap<String, SLocation>()

    fun resetAllHomes() {
        homes.clear()
    }

    fun addHome(name: String, sLocation: SLocation) {

        homes[name] = sLocation
    }

    fun removeHome(name: String) {
        // Lowercase home cause all homes are lowercase.
        homes.remove(name.toLowerCase())
    }

    fun getAllHomes(): Map<String, SLocation> {
        return homes
    }

    fun getHome(name: String): SLocation? {
        return homes[name.toLowerCase()]
    }

    fun hasHome(name: String): Boolean {
        return homes.containsKey(name.toLowerCase())
    }

    fun coopPlayer(authorizer: IPlayer?, iPlayer: IPlayer, notify: Boolean = true) {
        if (authorizer != null) {
            if (authorizer.coopedPlayersAuthorized == null) {
                authorizer.coopedPlayersAuthorized = HashSet()
            }
            authorizer.coopedPlayersAuthorized.add(iPlayer)
            if (notify) {
                authorizer.message(String.format(Message.commandCoopAuthorized, authorizer.getPlayer().name, iPlayer.getPlayer().name))
            }
        }

        // the iplayer needs an island for this.
        if (iPlayer.coopedIslandIds == null) {
            iPlayer.coopedIslandIds = java.util.HashSet<Int>()
        }
        currentCoopPlayers.add(UUID.fromString(iPlayer.uuid))
        iPlayer.coopedIslandIds.add(islandID)
    }

    fun removeCoopPlayer(iPlayer: IPlayer, notify: Boolean = true) {
        iPlayer.coopedIslandIds.remove(islandID)
        currentCoopPlayers.remove(UUID.fromString(iPlayer.uuid))
        if (notify) {
            iPlayer.message(String.format(Message.commandCoopLoggedOut))
        }
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