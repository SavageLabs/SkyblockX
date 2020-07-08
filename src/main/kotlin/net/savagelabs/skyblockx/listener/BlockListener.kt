package net.savagelabs.skyblockx.listener

import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.canUseBlockAtLocation
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.getIslandFromLocation
import net.savagelabs.skyblockx.core.isNotInSkyblockWorld
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.persist.Quests
import net.savagelabs.skyblockx.quest.QuestGoal
import net.savagelabs.skyblockx.upgrade.UpgradeType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.metadata.FixedMetadataValue


class BlockListener : Listener {

//
//    @EventHandler
//    fun onPlayerMove(event: PlayerMoveEvent) {
//        println(event.player.itemInHand.serialize())
//    }


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
            iPlayer.message(Message.instance.listenerActionDeniedCreateAnIslandFirst)
            event.isCancelled = true
            return
        }


        // Check using the general #canUseBlockAtLocation, will actually check co-op and own island.
        if (!canUseBlockAtLocation(iPlayer, event.block.location)) {
            iPlayer.message(Message.instance.listenerBlockPlacementDenied)
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
                Quests.instance.islandQuests.find { quest -> quest.type == QuestGoal.PLACE_BLOCKS && quest.id == currentQuest }
                    ?: return
            // Use XMaterial to parse the material, if null, try to use native material just in case.
            val material = event.block.type.toString()

            // Check if the material we just processed is the targetQuest's material instead of just checking if the quest is equal.
            if (material == targetQuest.goalParameter) {
                // Increment that quest data by 1 :)
                island.addQuestData(targetQuest.id)
                island.sendTeamQuestProgress(targetQuest, event.player)
                // Check if quest is complete :D
                if (targetQuest.isComplete(island.getQuestCompletedAmount(targetQuest.id))) {
                    island.completeQuest(iPlayer, targetQuest)
                }
            }

        }


        // Anti-abuse for skyblock.
        event.block.setMetadata(
            "skyblock-placed-by-player", FixedMetadataValue(SkyblockX.skyblockX, true)
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


        // Check if they have an island or co-op island, if not, deny.
        if (!iPlayer.hasCoopIsland() && !iPlayer.hasIsland() && !iPlayer.inBypass) {
            iPlayer.message(Message.instance.listenerActionDeniedCreateAnIslandFirst)
            event.isCancelled = true
            return
        }

        // Check if they can use the block on the island, or co-op island.
        if (!canUseBlockAtLocation(iPlayer, event.block.location)) {
            iPlayer.message(Message.instance.listenerBlockPlacementDenied)
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
                Quests.instance.islandQuests.find { quest -> quest.type == QuestGoal.BREAK_BLOCKS && quest.id == currentQuest }
                    ?: return

            // Use XMaterial to parse the material, if null, try to use native material just in case.
            val material = event.block.type.toString()
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
        if (!Config.instance.islandOreGeneratorEnabled || event.face == BlockFace.DOWN || isNotInSkyblockWorld(event.block.world)) return

        // Im looking at this later I remember that i needed a task to make this work, IDK why and I dont have time to fix,
        // Future ProSavage: Fix this pls, tasks have overhead we do not need.
        Bukkit.getScheduler().runTask(SkyblockX.skyblockX, Runnable {
            run {
                val level = getIslandFromLocation(event.block.location)?.upgrades?.get(UpgradeType.GENERATOR) ?: 0
                if (event.toBlock.type == Material.COBBLESTONE) event.toBlock.location.block.type =
                    (SkyblockX.generatorAlgorithm[level] ?: error("Generator level was not found in config.")).choose().parseMaterial()!!
            }
        })


    }

}