package io.illyria.skyblockx.core

import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Data
import io.illyria.skyblockx.persist.Message
import io.illyria.skyblockx.sedit.Position
import net.prosavage.baseplugin.WorldBorderUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

data class IPlayer(val uuid: String) {
    var name: String = Bukkit.getPlayer(UUID.fromString(uuid))!!.player!!.name


    var inBypass = false

    var falling = false

    var islandID = -1

    var choosingPosition = false
    var chosenPosition = Position.POSITION1


    var coopedIslandIds = HashSet<Int>()

    var borderColor = WorldBorderUtil.Color.Green
        get() = field

    var islandsInvitedTo = HashSet<Int>()


    fun isInvitedToIsland(island: Island): Boolean {
        return islandsInvitedTo != null && islandsInvitedTo.contains(island.islandID)
    }


    // This is for the coop players that this iplayer instance has authorized.
    // So that we can remove the co-op status of these players when this guy logs out.
    @Transient
    var coopedPlayersAuthorized = HashSet<IPlayer>()

    @Transient
    var pos1: Location? = null
    @Transient
    var pos2: Location? = null

    fun getPlayer(): Player {
        return Bukkit.getPlayer(UUID.fromString(uuid))!!
    }

    fun isLeader(): Boolean {
        return hasIsland() && getIsland()!!.ownerUUID == uuid
    }


    fun isOnOwnIsland(): Boolean {
        return !(this.hasIsland() && this.getIsland()!!.containsBlock(getPlayer().location))
    }

    fun message(message: String) {
        getPlayer().sendMessage(color(Message.messagePrefix + message))
    }

    fun isCoopedIsland(id: Int): Boolean {
        return coopedIslandIds != null && coopedIslandIds.contains(id)
    }

    fun removeCoopIsland(island: Island) {
        coopedIslandIds.remove(island.islandID)
        island.currentCoopPlayers.remove(UUID.fromString(uuid))
    }

    fun addCoopIsland(island: Island) {
        coopedIslandIds.add(island.islandID)
    }

    fun getCoopIslands(): List<Island> {
        val islands = arrayListOf<Island>()
        for (id in coopedIslandIds) {
            islands.add(getIslandById(id) ?: continue)
        }
        return islands
    }

    fun hasCoopIsland(): Boolean {
        // It's not supposed to be null, but fuckin gson man.
        return coopedIslandIds != null && coopedIslandIds.isNotEmpty()
    }

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
        return getIslandById(islandID)
    }




    fun coopIslandsContainBlock(location: Location): Boolean {
        for (id in coopedIslandIds) {
            // Island was not found, continue to next island.
            val island = getIslandById(id) ?: continue
            if (!island.containsBlock(location)) {
                return false
            }
        }
        return true
    }


}

fun getIPlayer(player: Player): IPlayer {
    // Check data, if not, The IPlayer instance does not exist, create it.
    return Data.IPlayers[player.uniqueId.toString()] ?: createIPlayer(player)
}

fun getIPlayerByName(name: String): IPlayer? {
    return Data.IPlayers.values.find { iPlayer -> iPlayer.name == name }
}

fun getIPlayerByUUID(uuid: String): IPlayer? {
    return Data.IPlayers[uuid]
}

fun createIPlayer(player: Player): IPlayer {
    val iPlayer = IPlayer(player.uniqueId.toString())
    Data.IPlayers[player.uniqueId.toString()] = iPlayer
    return iPlayer
}


fun canUseBlockAtLocation(iPlayer: IPlayer, location: Location): Boolean {
    // If the world is not the skyblock world, we will not interfere.
    if (location.world!!.name != Config.skyblockWorldName) return true
    if (iPlayer.inBypass) return true
    // If they dont have any co-op island or an island of their own, then they cannot do anything.
    if (!iPlayer.hasIsland() && !iPlayer.hasCoopIsland()) return false
    // Check our own island.
    var cancelEvent = (iPlayer.hasIsland() && !iPlayer.getIsland()!!.containsBlock(location))
    // Check the co-op island's permission if our own island's permission have failed.
    if (cancelEvent && iPlayer.hasCoopIsland()) {
        cancelEvent = (iPlayer.hasCoopIsland() && !iPlayer.coopIslandsContainBlock(location))
    }
    return !cancelEvent
}
