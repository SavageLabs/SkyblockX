package net.savagelabs.skyblockx.listener

import com.cryptomorin.xseries.XMaterial
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.canUseBlockAtLocation
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.isNotInSkyblockWorld
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.persist.Quests
import net.savagelabs.skyblockx.quest.QuestGoal
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.metadata.FixedMetadataValue

object BlockListener : Listener {
	@EventHandler
	fun onBlockPlace(event: BlockPlaceEvent) {
		// FUTURE CONTRIBUTIONS: Attempt to split checks into small blocks.
		if (isNotInSkyblockWorld(event.blockPlaced.world)) {
			return
		}

		// We're gonna need this.
		val iPlayer = event.player.getIPlayer()

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
		val xMaterial = XMaterial.matchXMaterial(event.block.type)
		val placementLimit = Config.instance.blockPlacementLimit.getOrDefault(xMaterial, -1)

		// Make sure the island has not met the placement limit of the corresponding material.
		if (island != null && placementLimit != -1) {
			val boost = island.placementLimitBoost.getOrDefault(xMaterial, 0)
			val placementCount = island.blocksPlaced.getOrDefault(xMaterial, -1)

			if (placementCount != -1 && placementCount >= (placementLimit + boost)) {
				iPlayer.message(Message.instance.placementLimitReached, xMaterial.toString())
				event.isCancelled = true
				return
			}

			island.blocksPlaced[xMaterial] = placementCount + 1
		}

		// Quest checking block.
		if (island != null && island.currentQuest != null) {
			// Assert non-null because the if check for this block will trigger.
			val currentQuest = island.currentQuest!!

			// Find the quest that the island has activated.
			val targetQuest = Quests.instance.islandQuests.find { quest ->
				quest.type == QuestGoal.PLACE_BLOCKS && quest.id == currentQuest
			} ?: return

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
		val iPlayer = event.player.getIPlayer()

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
		val island = iPlayer.getIsland() ?: return

		// Remove from placement
		val xMaterial = XMaterial.matchXMaterial(event.block.type)
		val old = island.blocksPlaced.getOrDefault(xMaterial, -1)

		if (old != -1) when (old) {
			1 -> island.blocksPlaced.remove(xMaterial)
			else -> island.blocksPlaced[xMaterial] = old - 1
		}

		// make sure the player has an island and the quest ain't null
		if (island.currentQuest == null) {
			return
		}

		// Assert non-null because the if check for this block will trigger.
		val currentQuest = island.currentQuest

		// Find the quest that the island has activated.
		val targetQuest = Quests.instance.islandQuests.find { quest ->
			quest.type == QuestGoal.BREAK_BLOCKS && quest.id == currentQuest
		} ?: return

		// Use XMaterial to parse the material, if null, try to use native material just in case.
		val material = event.block.type.toString()

		// Check if the material we just processed is the targetQuest's material instead of just checking if the quest is equal.
		if (material != targetQuest.goalParameter || event.block.hasMetadata("skyblock-placed-by-player")) {
			return
		}

		// Increment that quest data by 1 :)
		island.addQuestData(targetQuest.id, 1)
		island.sendTeamQuestProgress(targetQuest, event.player)

		// Check if quest is complete :D
		if (targetQuest.isComplete(island.getQuestCompletedAmount(targetQuest.id))) {
			island.completeQuest(iPlayer, targetQuest)
		}
	}
}