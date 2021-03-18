package net.savagelabs.skyblockx.upgrade.impl

import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.getIslandFromLocation
import net.savagelabs.skyblockx.core.isNotInSkyblockWorld
import net.savagelabs.skyblockx.event.IslandUpgradeEvent
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.upgrade.Upgrade
import net.savagelabs.skyblockx.upgrade.UpgradeType
import org.bukkit.Bukkit
import org.bukkit.block.BlockFace
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFromToEvent

/**
 * This upgrade increases the Generator level of an Island.
 */
object GeneratorUpgradeType : UpgradeType<BlockFromToEvent>(id = "GENERATOR") {
    override var listener: Listener? = null

    /**
     * [Array] this array contains all relative block faces to be used for the surrounding water check.
     */
    private val relatives: Array<out BlockFace> = arrayOf(BlockFace.WEST, BlockFace.SOUTH, BlockFace.EAST, BlockFace.NORTH)

    override fun commence(player: IPlayer, island: Island, level: Int, upgrade: Upgrade) {
        if (!player.takeMoney(this.priceOf(level, upgrade))) {
            return
        }

        // set island's level of upgrade correspondingly
        island.upgrades[this.id] = level

        Bukkit.getPluginManager().callEvent(IslandUpgradeEvent(island, this.id))
    }

    override fun onEvent(event: BlockFromToEvent) = with (event) {
        // no skyblock world or to block face of down
        if (!Config.instance.islandOreGeneratorEnabled || face == BlockFace.DOWN || isNotInSkyblockWorld(block.world)) return

        // make sure the flowing type is lava
        val preciseType = block.type.name
        if (preciseType != "LAVA" && preciseType != "STATIONARY_LAVA") {
            return
        }

        // break this function if none relatives are of type WATER
        if (relatives.none {
                    val relative = toBlock.getRelative(it).type.name
                    relative == "WATER" || relative == "STATIONARY_WATER"
        }) {
            return
        }

        // handle the Generator
        val level = getIslandFromLocation(block.location)?.upgrades?.get(id) ?: 0
        toBlock.type = SkyblockX.generatorAlgorithm[level]?.choose()?.parseMaterial() ?: return

        // make sure the event doesn't continue further to place cobblestone...
        this.isCancelled = true
    }
}