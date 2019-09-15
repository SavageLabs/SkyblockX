package net.savagellc.savageskyblock.core

import net.savagellc.savageskyblock.persist.Data
import net.savagellc.savageskyblock.sedit.Position
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

data class IPlayer(val uuid: String) {


    var inBypass = false

    var islandID = -1
    var tag = Bukkit.getOfflinePlayer(uuid).name

    var choosingPosition = false
    var chosenPosition = Position.POSITION1

    @Transient
    var pos1: Location? = null
    @Transient
    var pos2: Location? = null

    fun hasIsland(): Boolean {
        return islandID != -1 && Data.islands.containsKey(islandID)
    }

    fun unassignIsland() {
        islandID = -1
    }

    fun assignIsland(island: Island) {
        islandID = island.islandID
    }

    fun assignIsland(islandID: Int) {
        this.islandID = islandID
    }

    fun getIsland(): Island? {
        return Data.islands[islandID]
    }

}

fun getIPlayer(player: Player): IPlayer {
    if (Data.IPlayers.containsKey(player.uniqueId.toString())) {
        return Data.IPlayers[player.uniqueId.toString()]!!
    }
    // The IPlayer instance does not exist, create it.
    val iPlayer = IPlayer(player.uniqueId.toString())
    Data.IPlayers[player.uniqueId.toString()] = iPlayer
    return iPlayer
}

