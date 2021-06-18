package net.savagelabs.skyblockx.listener

import com.cryptomorin.xseries.XMaterial
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.*
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.persist.Quests
import net.savagelabs.skyblockx.quest.QuestGoal
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.hanging.HangingBreakByEntityEvent
import org.bukkit.event.hanging.HangingBreakEvent
import org.bukkit.metadata.FixedMetadataValue

object BlockListener : Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	fun onBlockPlace(event: BlockPlaceEvent) {
		// make sure it's not already cancelled for plugin support
		// FUTURE CONTRIBUTIONS: Attempt to split checks into small blocks.
		if (event.isCancelled || isNotInSkyblockWorld(event.blockPlaced.world)) {
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

		// Work the placement limit.
		if (!(SkyblockX.isWildStackerPresent && xMaterial === XMaterial.SPAWNER)) {
			workPlacement(xMaterial, island, iPlayer, event)
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

	@EventHandler(priority = EventPriority.HIGHEST)
	fun onBlockBreak(event: BlockBreakEvent) {
		// make sure it's not already cancelled for plugin support
		// FUTURE CONTRIBUTIONS: Attempt to split checks into small blocks.
		if (event.isCancelled || isNotInSkyblockWorld(event.block.world)) {
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

	@EventHandler
	private fun HangingBreakByEntityEvent.onHangingBreak() {
		if (this.remover?.type != EntityType.PLAYER) {
			return
		}

		val player = this.remover as Player
		val iPlayer = player.getIPlayer()

		if (this.isCancelled || isNotInSkyblockWorld(this.entity.world)) {
			return
		}

		// Check if they have an island or co-op island, if not, deny.
		if (!iPlayer.hasCoopIsland() && !iPlayer.hasIsland() && !iPlayer.inBypass) {
			iPlayer.message(Message.instance.listenerActionDeniedCreateAnIslandFirst)
			this.isCancelled = true
			return
		}

		// Check if they can use the block on the island, or co-op island.
		if (!canUseBlockAtLocation(iPlayer, this.entity.location)) {
			iPlayer.message(Message.instance.listenerBlockPlacementDenied)
			this.isCancelled = true
		}
	}

	internal fun workPlacement(material: XMaterial, island: Island?, islandPlayer: IPlayer, event: Cancellable) {
		// Necessity.
		val placementLimit = Config.instance.blockPlacementLimit.getOrDefault(material, -1)

		// Make sure the island is not null and the placement limit is present.
		if (island == null || placementLimit == -1) {
			return
		}

		// Work placement.
		val boost = island.placementLimitBoost.getOrDefault(material, 0)
		val placementCount = island.blocksPlaced.getOrDefault(material, 0)

		if (placementCount >= (placementLimit + boost)) {
			islandPlayer.message(Message.instance.placementLimitReached, material.toString())
			event.isCancelled = true
			return
		}

		island.blocksPlaced[material] = placementCount + 1
	}
}