package net.savagelabs.skyblockx.core

import com.cryptomorin.xseries.XMaterial
import io.papermc.lib.PaperLib
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.rayzr522.jsonmessage.JSONMessage
import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.event.IslandPostLevelCalcEvent
import net.savagelabs.skyblockx.event.IslandPreLevelCalcEvent
import net.savagelabs.skyblockx.persist.*
import net.savagelabs.skyblockx.persist.data.SLocation
import net.savagelabs.skyblockx.persist.data.getSLocation
import net.savagelabs.skyblockx.quest.Quest
import net.savagelabs.skyblockx.quest.incrementQuestInOrder
import net.savagelabs.skyblockx.sedit.SkyblockEdit
import net.savagelabs.skyblockx.world.Point
import net.savagelabs.skyblockx.world.spiral
import org.bukkit.*
import org.bukkit.block.Biome
import org.bukkit.block.CreatureSpawner
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import java.lang.reflect.InvocationTargetException
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.streams.toList
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue


@Suppress("UNCHECKED_CAST")
data class Island(
    val islandID: Int,
    val point: Point,
    var ownerUUID: String,
    var ownerTag: String,
    var islandSize: Int
) {


    var inventory = Bukkit.createInventory(null, (Config.chestRows[1] ?: 3) * 9)
        get() {
            if (field == null) field = Bukkit.createInventory(null, (Config.chestRows[1] ?: 3) * 9)
            return field
        }

    var beenToNether = false
    var netherFilePath = "nether-island.structure"

    lateinit var islandGoPoint: SLocation

    val minLocation: SLocation = getSLocation(point.getLocation())
    val maxLocation: SLocation = getSLocation(
        point.getLocation()
            .add(islandSize.toDouble(), 256.0, islandSize.toDouble())
    )

    var lastManualCalc: Long = -1L

    fun canManualCalc(): Boolean {
        return lastManualCalc == -1L || System.currentTimeMillis() - lastManualCalc > Config.islandTopManualCalcCooldownMiliseconds
    }


    fun setBiome(biome: Biome) {
        val world = Bukkit.getWorld(Config.skyblockWorldName)!!
        for (x in minLocation.x.toInt()..maxLocation.x.toInt()) {
            for (y in 0 until 256) {
                for (z in minLocation.z.toInt()..maxLocation.z.toInt()) {
                    world.getBlockAt(x, y, z).biome = biome
                }
            }
        }
    }


    var maxCoopPlayers = Config.defaultMaxCoopPlayers
    var maxIslandHomes = Config.defaultMaxIslandHomes


    var questData = HashMap<String, Int>()
    var currentQuest: String? = null

    var allowVisitors = false

    var oneTimeQuestsCompleted = mutableSetOf<String>()

    var memberLimit = Config.defaultIslandMemberLimit

    val members = mutableSetOf<String>()

    var currentQuestOrderIndex: Int? = 0

    fun isOneTimeQuestAlreadyCompleted(id: String): Boolean {
        return oneTimeQuestsCompleted != null && oneTimeQuestsCompleted.isNotEmpty() && oneTimeQuestsCompleted.contains(
            id
        )
    }

    fun assignNewOwner(ownerIPlayer: IPlayer) {
        ownerUUID = ownerIPlayer.uuid
        ownerTag = ownerTag
    }


    @ExperimentalTime
    fun calcIsland(): CalcInfo {
        var price = 0.0
        val spawnerMap = hashMapOf<EntityType, Int>()
        val mapAmt = hashMapOf<XMaterial, Int>()
        var time: TimedValue<Unit>? = null
        runBlocking {
            time = measureTimedValue {
                val chunkList = mutableSetOf<Chunk>()
                var chunks = mutableSetOf<ChunkSnapshot>()
                val world = Bukkit.getWorld(Config.skyblockWorldName)!!
                for (x in minLocation.x.toInt()..maxLocation.x.toInt()) {
                    for (z in minLocation.z.toInt()..maxLocation.z.toInt()) {
                        Bukkit.getScheduler().runTask(Globals.skyblockX, Runnable {
                            PaperLib.getChunkAtAsync(Location(world, x.toDouble(), 0.0, z.toDouble()))
                                .thenAccept { chunkList.add(it) }
                        })

                    }
                    delay(Config.islandTopChunkLoadDelayInMiliseconds)
                }
                chunks = chunkList.map { it.chunkSnapshot }.toMutableSet()
                val useNewGetBlockTypeSnapshotMethod = XMaterial.getVersion() >= 12.0
                for (chunkSnapshot in chunks) {
                    for (x in 0 until 16) {
                        for (y in 0 until 256) {
                            for (z in 0 until 16) {
                                val blockType =
                                    getChunkSnapshotBlockType(
                                        useNewGetBlockTypeSnapshotMethod,
                                        chunkSnapshot,
                                        x,
                                        y,
                                        z
                                    )!!
                                if (blockType == Material.AIR) continue
                                val xmat = XMaterial.matchXMaterial(blockType) ?: continue
                                if (xmat == XMaterial.SPAWNER) {
                                    PaperLib.getChunkAtAsync(
                                        Bukkit.getWorld(chunkSnapshot.worldName)!!,
                                        chunkSnapshot.x,
                                        chunkSnapshot.z
                                    ).thenAccept {
                                        val state = it.getBlock(x, y, z).state
                                        state as CreatureSpawner
                                        val spawnedType = state.spawnedType
                                        price += (BlockValues.spawnerValues[spawnedType] ?: 0.0)
                                        val spawnerAmount = spawnerMap.getOrDefault(spawnedType, 0)
                                        spawnerMap[spawnedType] = spawnerAmount + 1
                                    }
                                    continue
                                }
                                price += BlockValues.blockValues[xmat] ?: 0.0
                                mapAmt[xmat] = mapAmt.getOrDefault(xmat, 0) + 1
                            }
                        }
                    }
                }
            }
        }
        return CalcInfo(time?.duration ?: Duration.ZERO, price, mapAmt, spawnerMap, islandID)
    }

    data class CalcInfo @ExperimentalTime constructor(
        val timeDuration: Duration,
        var worth: Double,
        val matAmt: Map<XMaterial, Int>,
        val spawnerAmt: Map<EntityType, Int>,
        val islandID: Int
    )

    /**
     * Gets the chunksnapshot's block type.
     *
     * @param useNew        - use the new method or not, this is only here as this method is designed to be called a lot,
     * and I dont want to do the processing over and over.
     * @param chunkSnapshot - the chunksnapshot to calculate.
     * @param x             - the x to get the block of
     * @param y             - the y to get the block of
     * @param z             - the z to get the block of
     * @return - the Material it gets.
     */
    @Throws(NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
    fun getChunkSnapshotBlockType(useNew: Boolean, chunkSnapshot: ChunkSnapshot, x: Int, y: Int, z: Int): Material? {
        return if (useNew) chunkSnapshot.getBlockType(x, y, z)
        // TODO: Optimize this by caching the methods as this is reflection being called for EVERY block.
        else {
            val id = chunkSnapshot.javaClass.getMethod(
                "getBlockTypeId",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
                .invoke(chunkSnapshot, x, y, z) as Int
            return Class.forName("org.bukkit.Material").getMethod("getMaterial", Int::class.javaPrimitiveType).invoke(
                null,
                id
            ) as Material
        }
    }

    fun messageAllOnlineIslandMembers(message: String) {
        // Color message in case.
        val messageFormatted = color(message)

        // Message the owner
        Bukkit.getPlayer(UUID.fromString(ownerUUID))?.sendMessage(messageFormatted)

        // Message island members
        for (member in members) {
            Bukkit.getPlayer(member)?.sendMessage(messageFormatted)
        }
    }


    fun getIslandMembers(): Set<IPlayer> {
        if (members == null || members.size == 0) return emptySet()
        return members.stream().map { uuid -> getIPlayerByUUID(uuid) }?.toList()?.toSet() as Set<IPlayer>
    }

    fun getAllMemberUUIDs(): Set<String> {
        if (members == null || members.size == 0) return emptySet()
        return members
    }

    fun inviteMember(iPlayer: IPlayer) {
        if (memberLimit <= members.size) {
            return
        }
        if (iPlayer.islandsInvitedTo == null) {
            iPlayer.islandsInvitedTo = HashSet()
        }
        iPlayer.islandsInvitedTo.add(islandID)
    }

    fun addMember(iPlayer: IPlayer) {
        if (memberLimit <= members.size) {
            return
        }
        if (!members.contains(iPlayer.getPlayer().uniqueId.toString())) {
            members.add(iPlayer.getPlayer().uniqueId.toString())
        }

        iPlayer.assignIsland(this)
    }


    fun kickMember(name: String) {
        members.remove(getIPlayerByName(name)?.uuid)
        getIPlayerByName(name)?.assignIsland(-1)
    }

    fun getValue(): Double {
        return Globals.islandValues?.map?.get(islandID)?.worth ?: 0.0
    }

    fun getLevel(): Double {
        return floor((25 + sqrt(Config.levelIncrementFactor * Config.levelIncrementFactor - 4 * Config.levelIncrementFactor * (-1 * this.getValue()))) / (2 * Config.levelIncrementFactor))
    }

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
            maxCoopPlayers = getMaxPermission(owner, "skyblockx.limits.coop-players")
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
            maxIslandHomes = getMaxPermission(owner, "skyblockx.limits.island-homes")
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

    fun getQuestCompletedAmount(id: String): Int {
        // We just inserted a zero right above :)
        return questData[id] ?: 0
    }

    fun completeQuest(completingPlayer: IPlayer, quest: Quest) {
        currentQuest = null
        // Set it to complete amount just to prevent confusion
        questData[quest.id] = 0
        if (quest.oneTime) {
            if (oneTimeQuestsCompleted == null) {
                oneTimeQuestsCompleted = mutableSetOf<String>()
            }
            oneTimeQuestsCompleted.add(quest.id)
        }
        // Check for quest ordering system.
        if (Quests.useQuestOrder && Quests.questOrder.contains(quest.id)) {
            val indexOfQuest = Quests.questOrder.indexOf(quest.id) + 1
            currentQuestOrderIndex = if (Quests.questOrder.size > indexOfQuest) {
                indexOfQuest
            } else {
                null
            }
            incrementQuestInOrder(this)
        }
        quest.giveRewards(completingPlayer)
    }

    fun sendTeamQuestProgress(quest: Quest, vararg players: Player) {
        val progressAmt = (getQuestCompletedAmount(quest.id).toDouble() / quest.amountTillComplete.toDouble()) * 10
        var progress = ""
        for (completed in 0..9) {
            progress += if (completed < progressAmt) Message.questProgressCompletedChar else Message.questProgressInCompleteChar
        }

        JSONMessage.actionbar(
            color(
                Message.questProgressBarMessage
                    .replace("{quest-name}", quest.name)
                    .replace("{progress-bar}", progress)
                    .replace("{percentage}", DecimalFormat("##").format(progressAmt * 10))
            ), *players
        )
    }

    fun setQuestData(id: String, value: Int) {
        questData[id] = value
    }

    fun changeCurrentQuest(id: String?) {
        currentQuest = id
    }

    fun addQuestData(id: String, value: Int = 1) {
        questData[id] = (questData[id] ?: 0) + value
    }

    fun subtractQuestData(name: String, value: Int) {
        questData[name] = (questData[name] ?: 0) + value
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
                authorizer.message(
                    String.format(
                        Message.commandCoopAuthorized,
                        authorizer.getPlayer().name,
                        iPlayer.getPlayer().name
                    )
                )
            }
        }

        // the iplayer needs an island for this.
        if (iPlayer.coopedIslandIds == null) {
            iPlayer.coopedIslandIds = java.util.HashSet<Int>()
        }
        currentCoopPlayers.add(UUID.fromString(iPlayer.uuid))
        iPlayer.coopedIslandIds.add(islandID)
    }

    fun hasCoopPlayer(iPlayer: IPlayer): Boolean {
        return currentCoopPlayers != null && currentCoopPlayers.contains(UUID.fromString(iPlayer.uuid))
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


    fun getIslandCenter(): Location {
        return Location(
            minLocation.getLocation().world,
            minLocation.x + (Config.islandMaxSizeInBlocks / 2),
            101.toDouble(),
            minLocation.z + (Config.islandMaxSizeInBlocks / 2)
        )
    }


    fun containsBlock(v: Location): Boolean {
        if (v.world !== minLocation.getLocation().world) return false
        val x = v.x
        val y = v.y
        val z = v.z
        return x >= minLocation.x && x < maxLocation.x + 1 && y >= minLocation.y && y < maxLocation.y + 1 && z >= minLocation.z && z < maxLocation.z + 1
    }

    /**
     * This is built for checking bounds without checking the Y axis as it is not needed.
     */
    fun locationInIsland(v: Location): Boolean {
        if (isNotInSkyblockWorld(v.world!!))
            return false

        val x = v.x
        val z = v.z
        return x >= minLocation.x && x < maxLocation.x + 1 && z >= minLocation.z && z < maxLocation.z + 1
    }


    /**
     * Delete the players island by removing the whole team, Deleting the actual blocks is too intensive.
     */
    fun delete() {
        val ownerIPlayer = getIPlayerByUUID(ownerUUID)
        ownerIPlayer?.unassignIsland()
        val player = ownerIPlayer?.getPlayer()
        teleportAsync(player, Bukkit.getWorld(Config.defaultWorld)!!.spawnLocation, Runnable { })
        if (Config.islandDeleteClearInventory) player?.inventory?.clear()
        if (Config.islandDeleteClearEnderChest) player?.enderChest?.clear()
        getAllMemberUUIDs().forEach { memberUUID ->
            val iplayer = getIPlayerByUUID(memberUUID)
            iplayer?.unassignIsland()
            val player = iplayer?.getPlayer()
            teleportAsync(player, Bukkit.getWorld(Config.defaultWorld)!!.spawnLocation.add(0.0, 1.0, 0.0), Runnable { })
            if (Config.islandDeleteClearInventory) player?.inventory?.clear()
            if (Config.islandDeleteClearEnderChest) player?.enderChest?.clear()
        }
        Data.islands.remove(islandID)
        // Delete island from value map.
        Globals.islandValues?.map?.remove(islandID)
        deleteIslandBlocks()
    }

    fun deleteIslandBlocks() {
        if (!Config.removeBlocksOnIslandDelete) return
        val start = minLocation.getLocation()
        val end = maxLocation.getLocation()
        val world = Bukkit.getWorld(start.world!!.name)!!

        for (x in start.x.toInt()..end.x.toInt()) {
            for (y in start.y.toInt()..end.y.toInt()) {
                for (z in start.z.toInt()..end.z.toInt()) {
                    world.getBlockAt(x, y, z).type = Material.AIR
                }
            }
        }
    }

    fun promoteNewLeader(name: String) {
        // Get new leader.
        val newLeader = getIPlayerByName(name)!!
        // remove new leader from member list.
        members.remove(Bukkit.getPlayer(name)?.uniqueId.toString())
        // Make old leader a member
        val oldleader = getIPlayerByName(ownerTag)!!
        members.add(oldleader.getPlayer().uniqueId.toString())
        // Assign again just in case :P
        oldleader.assignIsland(this)
        // Actually make the leader the leader of the island
        ownerUUID = newLeader.uuid
        ownerTag = newLeader.name
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
    val lowerCaseOwnerTag = ownerTag.toLowerCase()
    for (island in Data.islands.values) {
        if (island.ownerTag.toLowerCase() == lowerCaseOwnerTag) {
            return island
        }
    }

    return null
}

fun createIsland(player: Player?, schematic: String, teleport: Boolean = true): Island {
    var size = if (player == null) Config.islandMaxSizeInBlocks else getMaxPermission(player, "skyblockx.size")
    size = if (size <= 0 || size > Config.islandMaxSizeInBlocks) Config.islandMaxSizeInBlocks else size
    val island =
        Island(
            Data.nextIslandID,
            spiral(Data.nextIslandID),
            player?.uniqueId.toString(),
            player?.name ?: "SYSTEM_OWNED",
            size
        )
    Data.islands[Data.nextIslandID] = island
    Data.nextIslandID++
    // Make player null because we dont want to send them the SkyblockEdit Engine's success upon pasting the island.
    SkyblockEdit().pasteIsland(schematic, island.getIslandCenter(), null)
    if (player != null) {
        val iPlayer = getIPlayer(player)
        iPlayer.assignIsland(island)
        if (teleport) teleportAsync(player, island.getIslandCenter(), Runnable { })
        incrementQuestInOrder(island)
    }
    // Use deprecated method for 1.8 support.
    player?.sendTitle(color(Message.islandCreatedTitle), color(Message.islandCreatedSubtitle))
    player?.sendMessage(color("${Message.messagePrefix}${String.format(Message.islandCreationMessage, size)}"))
    if (player != null) updateWorldBorder(player, player.location, 10L)
    island.islandGoPoint = getSLocation(island.getIslandCenter())
    return island
}

fun deleteIsland(player: Player) {
    val iPlayer = getIPlayer(player)
    if (iPlayer.hasIsland()) {
        Data.islands.remove(iPlayer.getIsland()!!.islandID)
        iPlayer.unassignIsland()
    }
}

data class IslandTopInfo(val map: HashMap<Int, Island.CalcInfo>, val time: Long)

@ExperimentalTime
        /**
         * CALL THIS ASYNC OR WHOLE SERVER WILL SLEEP :).
         */
fun calculateAllIslands() {
    val islandVals = hashMapOf<Int, Island.CalcInfo>()
    val pluginManager = Bukkit.getPluginManager()
    for ((key, island) in Data.islands) {
        val islandPreCalcEvent = IslandPreLevelCalcEvent(island, island.getValue())
        Bukkit.getScheduler().callSyncMethod(Globals.skyblockX) { pluginManager.callEvent(islandPreCalcEvent) }
        if (islandPreCalcEvent.isCancelled) continue
        val worth = island.calcIsland()
        val islandPostCalcEvent = IslandPostLevelCalcEvent(island, worth.worth)
        Bukkit.getScheduler().callSyncMethod(Globals.skyblockX) { pluginManager.callEvent(islandPostCalcEvent) }
        worth.worth = islandPostCalcEvent.levelAfterCalc ?: worth.worth
        islandVals[key] = worth
        Globals.skyblockX.logger.info("Finished Island ${island.ownerTag}")
    }
    Globals.islandValues = IslandTopInfo(islandVals, System.nanoTime())
}

