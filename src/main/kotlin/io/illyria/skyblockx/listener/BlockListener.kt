package io.illyria.skyblockx.listener

import io.illyria.skyblockx.core.canUseBlockAtLocation
import io.illyria.skyblockx.core.getIPlayer
import io.illyria.skyblockx.core.getIslandFromLocation
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Message
import io.illyria.skyblockx.quest.QuestGoal
import net.prosavage.baseplugin.XMaterial
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockFormEvent
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.metadata.FixedMetadataValue


class BlockListener : Listener {


    @EventHandler
    fun onBlockBreak(event: BlockPlaceEvent) {
        // FUTURE CONTRIBUTIONS: Attempt to split checks into small blocks.
        if (event.block.location.world?.name != Config.skyblockWorldName) {
            return
        }

        // We're gonna need this.
        val iPlayer = getIPlayer(event.player)

        // Check if they dont have an island or a co-op island, if not, deny.
        if (!iPlayer.hasCoopIsland() && !iPlayer.hasIsland()) {
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
        val island = iPlayer.getIsland()!!
        // Quest checking block.
        if (iPlayer.hasIsland() && island.currentQuest != null) {
            // Assert non-null because the if check for this block will trigger.
            val currentQuest = island.currentQuest!!
            // Find the quest that the island has activated.
            val targetQuest =
                Config.islandQuests.find { quest -> quest.type == QuestGoal.PLACE_BLOCKS && quest.id == currentQuest } ?: return
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
        event.block.setMetadata("skyblock-placed-by-player", FixedMetadataValue(io.illyria.skyblockx.Globals.skyblockX, true))
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        // FUTURE CONTRIBUTIONS: Attempt to split checks into small blocks.
        if (event.block.location.world?.name != Config.skyblockWorldName) {
            return
        }


        // Need this a lot.
        val iPlayer = getIPlayer(event.player)

        // Check if they have an island or co-op island, if not, deny.
        if (!iPlayer.hasCoopIsland() && !iPlayer.hasIsland()) {
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
        val island = iPlayer.getIsland()!!
        // Quest checking block.
        if (iPlayer.hasIsland() && island.currentQuest != null) {
            // Assert non-null because the if check for this block will trigger.
            val currentQuest = island.currentQuest!!
            // Find the quest that the island has activated.
            val targetQuest =
                Config.islandQuests.find { quest -> quest.type == QuestGoal.BREAK_BLOCKS && quest.id == currentQuest } ?: return
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

}