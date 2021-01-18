package net.savagelabs.skyblockx.upgrade.impl

import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.getIslandFromLocation
import net.savagelabs.skyblockx.core.isNotInSkyblockWorld
import net.savagelabs.skyblockx.event.IslandUpgradeEvent
import net.savagelabs.skyblockx.gui.wrapper.GUIItem
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.upgrade.Upgrade
import net.savagelabs.skyblockx.upgrade.UpgradeLevelInfo
import net.savagelabs.skyblockx.upgrade.levelItemsOrErrorByPreview
import net.savagelabs.skyblockx.upgrade.maxLevelItemOrErrorByPreview
import org.bukkit.Bukkit
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFromToEvent

/**
 * This upgrade increases the Generator level of an Island.
 */
object GeneratorUpgrade : Upgrade {
    override val id: String = "GENERATOR"
    override val preview: Map<Int, UpgradeLevelInfo> by lazy { this.levelItemsOrErrorByPreview() }
    override val maxLevelItem: GUIItem by lazy { this.maxLevelItemOrErrorByPreview() }
    override val listener: Listener = GeneratorListener()

    override fun commence(player: IPlayer, island: Island, level: Int) {
        if (!player.takeMoney(this.priceOf(level))) {
            return
        }

        // set island's level of upgrade correspondingly
        island.upgrades[this.id] = level

        Bukkit.getPluginManager().callEvent(IslandUpgradeEvent(island, this.id))
    }

    /**
     * This listener handles all Generator related events.
     */
    private class GeneratorListener : Listener {
        /**
         * [Array] this array contains all relative block faces to be used for the surrounding water check.
         */
        private val relatives: Array<out BlockFace> = arrayOf(BlockFace.WEST, BlockFace.SOUTH, BlockFace.EAST, BlockFace.NORTH)

        /**
         * This event handles the Generator itself.
         */
        @EventHandler
        private fun BlockFromToEvent.onGenerate() {
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
}