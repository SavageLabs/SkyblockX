package net.savagelabs.skyblockx.listener

import com.cryptomorin.xseries.XMaterial
import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.core.canUseBlockAtLocation
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.isNotInSkyblockWorld
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.persist.Quests
import net.savagelabs.skyblockx.quest.QuestGoal
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.metadata.FixedMetadataValue


class BlockListener : Listener {


    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        // FUTURE CONTRIBUTIONS: Attempt to split checks into small blocks.
        if (isNotInSkyblockWorld(event.blockPlaced.world)) {
            return
        }

        // We're gonna need this.
        val iPlayer = getIPlayer(event.player)


        // Check if they dont have an island or a co-op island, if not, deny.
        if (!iPlayer.hasCoopIsland() && !iPlayer.hasIsland() && !iPlayer.inBypass) {
            iPlayer.message(Message.listenerActionDeniedCreateAnIslandFirst)
            event.isCancelled = true
            return
        }


        // Check using the general #canUseBlockAtLocation, will actually check co-op and own island.
        if (!canUseBlockAtLocation(iPlayer, event.block.location)) {
            iPlayer.message(Message.listenerBlockPlacementDenied)
            event.isCancelled = true
            return
        }

        // We're gonna need this more than once here, store to prevent lookups.
        val island = iPlayer.getIsland()
        // Quest checking block.
        if (iPlayer.hasIsland() && island!!.currentQuest != null) {
            // Assert non-null because the if check for this block will trigger.
            val currentQuest = island.currentQuest!!
            // Find the quest that the island has activated.
            val targetQuest =
                Quests.islandQuests.find { quest -> quest.type == QuestGoal.PLACE_BLOCKS && quest.id == currentQuest }
                    ?: return
            // Use XMaterial to parse the material, if null, try to use native material just in case.
            val material = XMaterial.matchXMaterial(event.block.type)?.name ?: event.block.type.name

            // Check if the material we just processed is the targetQuest's material instead of just checking if the quest is equal.
            if (material == targetQuest.goalParameter) {
                // Increment that quest data by 1 :)
                island.addQuestData(targetQuest.id, 1)
                island.sendTeamQuestProgress(targetQuest, event.player)
                // Check if quest is complete :D
                if (targetQuest.isComplete(island.getQuestCompletedAmount(targetQuest.id))) {
                    island.completeQuest(iPlayer, targetQuest)
                }
            }

        }


        // Anti-abuse for skyblock.
        event.block.setMetadata("skyblock-placed-by-player", FixedMetadataValue(Globals.skyblockX, true)
        )
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        // FUTURE CONTRIBUTIONS: Attempt to split checks into small blocks.
        if (isNotInSkyblockWorld(event.block.world)) {
            return
        }


        // Need this a lot.
        val iPlayer = getIPlayer(event.player)

        if (iPlayer.inBypass) return


        // Check if they have an island or co-op island, if not, deny.
        if (!iPlayer.hasCoopIsland() && !iPlayer.hasIsland() && !iPlayer.inBypass) {
            iPlayer.message(Message.listenerActionDeniedCreateAnIslandFirst)
            event.isCancelled = true
            return
        }

        // Check if they can use the block on the island, or co-op island.
        if (!canUseBlockAtLocation(iPlayer, event.block.location)) {
            iPlayer.message(Message.listenerBlockPlacementDenied)
            event.isCancelled = true
            return
        }


        // We're gonna need this more than once here, store to prevent lookups.
        val island = iPlayer.getIsland()
        // Quest checking block.
        if (iPlayer.hasIsland() && island!!.currentQuest != null) {
            // Assert non-null because the if check for this block will trigger.
            val currentQuest = island.currentQuest!!
            // Find the quest that the island has activated.
            val targetQuest =
                Quests.islandQuests.find { quest -> quest.type == QuestGoal.BREAK_BLOCKS && quest.id == currentQuest }
                    ?: return
            // Use XMaterial to parse the material, if null, try to use native material just in case.
            val material = XMaterial.matchXMaterial(event.block.type)?.name ?: event.block.type.name

            // Check if the material we just processed is the targetQuest's material instead of just checking if the quest is equal.
            if (material == targetQuest.goalParameter && !event.block.hasMetadata("skyblock-placed-by-player")) {
                // Increment that quest data by 1 :)
                island.addQuestData(targetQuest.id, 1)
                island.sendTeamQuestProgress(targetQuest, event.player)

                // Check if quest is complete :D
                if (targetQuest.isComplete(island.getQuestCompletedAmount(targetQuest.id))) {
                    island.completeQuest(iPlayer, targetQuest)
                }
            }

        }

    }

    @EventHandler
    fun onBlockFromToEvent(event: BlockFromToEvent) {
        // No skyblock world or generating from down block face.
        if (!Config.islandOreGeneratorEnabled || event.face == BlockFace.DOWN || isNotInSkyblockWorld(event.block.world)) return

        Bukkit.getScheduler().runTask(Globals.skyblockX, Runnable {
            run {
                if (event.toBlock.type == Material.COBBLESTONE) event.toBlock.location.block.type =
                    Globals.generatorAlgorithm[1]!!.choose().parseMaterial()!!
            }
        })


    }

}