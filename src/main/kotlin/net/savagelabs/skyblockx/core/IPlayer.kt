package net.savagelabs.skyblockx.core

import com.fasterxml.jackson.annotation.JsonIgnore
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Data
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.sedit.Position
import net.savagelabs.worldborder.WorldBorderUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

data class IPlayer(val uuid: String, val name: String) {

    var lastIslandResetTime = -1L

    var inBypass = false

    var falling = false

    @JsonIgnore
    var teleportDeath: Location? = null

    var islandID = -1

    var choosingPosition = false
    var chosenPosition = Position.POSITION1


    var coopedIslandIds = HashSet<Int>()

    var borderColor = WorldBorderUtil.Color.GREEN
        get() = field

    var islandsInvitedTo = HashSet<Int>()


    fun isInvitedToIsland(island: Island): Boolean {
        return islandsInvitedTo != null && islandsInvitedTo.contains(island.islandID)
    }


    // This is for the coop players that this iplayer instance has authorized.
    // So that we can remove the co-op status of these players when this guy logs out.
    @JsonIgnore
    var coopedPlayersAuthorized = HashSet<IPlayer>()

    @JsonIgnore
    var pos1: Location? = null

    @JsonIgnore
    var pos2: Location? = null

    @JsonIgnore
    var syncData = false

    @JsonIgnore
    fun getPlayer(): Player? {
        return Bukkit.getPlayer(UUID.fromString(uuid))
    }

    @JsonIgnore
    fun isLeader(): Boolean {
        return hasIsland() && getIsland()!!.ownerUUID == uuid
    }

    @JsonIgnore
    fun isOnline(): Boolean {
        return Bukkit.getPlayer(UUID.fromString(uuid)) != null
    }

    @JsonIgnore
    fun isOnOwnIsland(): Boolean {
        return !(this.hasIsland() && this.getIsland()!!.containsBlock(getPlayer()!!.location))
    }

    fun message(message: String) {
        getPlayer()!!.sendMessage(color(Message.instance.messagePrefix + message))
    }

    fun isCoopedIsland(id: Int): Boolean {
        return coopedIslandIds != null && coopedIslandIds.contains(id)
    }

    fun removeCoopIsland(island: Island) {
        coopedIslandIds.remove(island.islandID)
        island.currentCoopPlayers.remove(UUID.fromString(uuid))
    }

    fun attemptToCoopPlayer(target: IPlayer) {
        val island = getIsland()!!
        if (!island.canHaveMoreCoopPlayers()) {
            message(Message.instance.commandCoopCannotHaveMoreCoopPlayers)
            return
        }

        island.coopPlayer(this, target)

        target.message(String.format(Message.instance.commandCoopMessageRecipient, name))
        message(String.format(Message.instance.commandCoopInvokerSuccess, target.name))
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

    fun attemptExpel(target: IPlayer) {
        // Remove the target's co-op status if theyre co-op.
        val island = getIsland()!!
        if (target.hasCoopIsland() && target.coopedIslandIds.contains(islandID)) {
            target.removeCoopIsland(island)
            target.message(Message.instance.commandRemovedCoopStatus)
            message(String.format(Message.instance.commandRemoveInvokerCoopRemoved, target.name))
        }


        val targetNewLocation =
            target.getIsland()?.getIslandCenter() ?: Bukkit.getWorld(Config.instance.defaultWorld)!!.spawnLocation

        // Check if they're even on the island, to prevent abuse.
        if (!island.containsBlock(target.getPlayer()!!.location)) {
            message(Message.instance.commandRemoveInvokerPlayerNotOnIsland)
            return
        }

        // Teleport them cuz they're on the island.
        teleportAsync(target.getPlayer(), targetNewLocation, Runnable { })
        message(String.format(Message.instance.commandRemoveInvokerSuccess, target.name))
    }

    fun hasCoopIsland(): Boolean {
        // net.savagelabs.savagepluginx.item.It's not supposed to be null, but fuckin gson man.
        return coopedIslandIds != null && coopedIslandIds.isNotEmpty()
    }

    fun hasIsland(): Boolean {
        return islandID != -1 && Data.instance.islands.containsKey(islandID)
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


    fun message(message: String, vararg args: String) {
        message(String.format(message, *args))
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
    return Data.instance.IPlayers[player.uniqueId.toString()] ?: createIPlayer(player)
}

fun getIPlayerByName(name: String): IPlayer? {
    return Data.instance.IPlayers.values.find { iPlayer -> iPlayer.name == name }
}

fun getIPlayerByUUID(uuid: String): IPlayer? {
    return Data.instance.IPlayers[uuid]
}

fun createIPlayer(player: Player): IPlayer {
    val iPlayer = IPlayer(player.uniqueId.toString(), player.name)
    Data.instance.IPlayers[player.uniqueId.toString()] = iPlayer
    return iPlayer
}


fun canUseBlockAtLocation(iPlayer: IPlayer, location: Location): Boolean {
    // If the world is not the skyblock world, we will not interfere.
    if (location.world!!.name != Config.instance.skyblockWorldName) return true
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
