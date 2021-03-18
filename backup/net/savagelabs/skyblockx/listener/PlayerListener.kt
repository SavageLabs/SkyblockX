package net.savagelabs.skyblockx.listener

import com.cryptomorin.xseries.XMaterial
import net.savagelabs.skyblockx.core.*
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.persist.Quests
import net.savagelabs.skyblockx.quest.QuestGoal
import net.savagelabs.skyblockx.quest.failsQuestCheckingPreRequisites
import net.savagelabs.skyblockx.sedit.SkyBlockEdit.pasteIsland
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.*
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.*

object PlayerListener : Listener {
	@EventHandler
	fun onPlayerCraft(event: CraftItemEvent) {
		// Exit if we aren't in skyblock world - since they cannot be on an island that way.
		if (isNotInSkyblockWorld(event.whoClicked.world)) {
			return
		}
		val iplayer = (event.whoClicked as Player).getIPlayer()
		// Fail the checks if we dont have an island, or dont have a active quest, or if we arent on our own island.
		val island = iplayer.getIsland()

		if (failsQuestCheckingPreRequisites(iplayer, island, event.whoClicked.location)) {
			return
		}

		val currentQuest = island!!.currentQuest
		// Find the quest that the island has activated, if none found, return.
		val targetQuest =
			Quests.instance.islandQuests.find { quest -> quest.type == QuestGoal.CRAFT && quest.id == currentQuest }
				?: return
		// Use XMaterial to parse the material, if null, try to use native material just in case.
		val materialCrafted = event.recipe.result.type
		// We had a quest active and it was a crafting quest, however, we didnt craft the goal item.
		if (materialCrafted.toString().toLowerCase() != targetQuest.goalParameter.toLowerCase()) {
			return
		}

		// Increment that quest data by 1 :)
		island.addQuestData(targetQuest.id, 1)
		island.sendTeamQuestProgress(targetQuest, event.whoClicked as Player)
		// Check if quest is complete :D
		if (targetQuest.isComplete(island.getQuestCompletedAmount(targetQuest.id))) {
			island.completeQuest(iplayer, targetQuest)
		}
	}


	@EventHandler
	fun onPlayerTeleport(event: PlayerTeleportEvent) {
		updateWorldBorder(event.player, event.to!!, 10L)
		if (event.cause == ENDER_PEARL && event.to != null && !isNotInSkyblockWorld(
				event.from.world!!
			) && getIslandFromLocation(event.from) != getIslandFromLocation(event.to!!)
		) {
			event.isCancelled = true
		}
	}

	@EventHandler
	fun PlayerPortalEvent.onPlayerChangeWorldEvent() {
		// make sure the from world is the overworld and it's either an end or nether portal
		val isNetherPortal = cause == NETHER_PORTAL
		if (from.world?.name != Config.instance.skyblockWorldName || (!isNetherPortal && cause != END_PORTAL)) {
			return
		}

		// necessary
		val islandPlayer = player.getIPlayer()

		// cancel
		isCancelled = true

		// prepare
		val islandFromLocation = getIslandFromLocation(from)
		val clonedFromCenter = islandFromLocation?.getIslandCenter()?.clone()?.apply {
			this.world = Bukkit.getWorld(if (isNetherPortal) {
				Config.instance.skyblockWorldNameNether
			} else {
				Config.instance.skyblockWorldNameEnd
			})
		} ?: return

		// make sure to paste island if the `island` is not created already
		when {
			isNetherPortal && !islandFromLocation.beenToNether -> {
				pasteIsland(islandFromLocation.netherFilePath.replace(".structure", ""), clonedFromCenter, null)
				islandFromLocation.beenToNether = true
			}

			!isNetherPortal && !islandFromLocation.beenToEnd -> {
				pasteIsland(islandFromLocation.endFilePath.replace(".structure", ""), clonedFromCenter, null)
				islandFromLocation.beenToEnd = true
			}
		}

		// teleport player to new location and send message
		player.teleport(clonedFromCenter, PLUGIN)
		islandPlayer.message(if (isNetherPortal) Message.instance.islandNetherTeleported else Message.instance.islandEndTeleported)
	}

	@EventHandler
	fun onPlayerFish(event: PlayerFishEvent) {
		// Event fires for all other times the rod is thrown, so we need to check the state of the event, along with the world it self right after.
		if (event.state != PlayerFishEvent.State.CAUGHT_FISH || event.caught !is Item || isNotInSkyblockWorld(event.hook.world)) {
			return
		}

		val iplayer = (event.player).getIPlayer()

		val island = iplayer.getIsland()
		// Check if we even have an island, have a quest, and check the HOOK's position instead of the player, since we dont want people fishing in others islands for edge cases.
		if (failsQuestCheckingPreRequisites(iplayer, island, event.hook.location)) {
			return
		}

		val currentQuest = island!!.currentQuest
		// Find the quest that the island has activated, if none found, return.
		val targetQuest =
			Quests.instance.islandQuests.find { quest -> quest.type == QuestGoal.FISHING && quest.id == currentQuest }
				?: return
		// Use the FISH caught and parse for the version that we need it for.
		val fishNeededForQuest = XMaterial.valueOf(targetQuest.goalParameter)
		if (fishNeededForQuest != XMaterial.matchXMaterial((event.caught!! as Item).itemStack)) {
			return
		}

		// this just increments quest Data.instance.
		island.addQuestData(targetQuest.id)
		island.sendTeamQuestProgress(targetQuest, event.player)

		if (targetQuest.isComplete(island.getQuestCompletedAmount(targetQuest.id))) {
			island.completeQuest(iplayer, targetQuest)
		}
	}

	@EventHandler
	fun onPlayerInventoryClick(event: InventoryClickEvent) {
		if (isNotInSkyblockWorld(event.whoClicked.world)
			// Slot -999 is not in the inventory so return :P
			|| event.slot == -999
		) {
			return
		}

		// Smelting Quests POG.
		if (event.inventory.type == InventoryType.FURNACE && event.slot == 2 && (event.action == InventoryAction.MOVE_TO_OTHER_INVENTORY || event.action == InventoryAction.PICKUP_ALL || event.action == InventoryAction.PICKUP_ALL) && event.view.bottomInventory != event.clickedInventory) {
			val iPlayer = (event.whoClicked as Player).getIPlayer()

			val island = iPlayer.getIsland()

			if (failsQuestCheckingPreRequisites(iPlayer, island, event.whoClicked.location)) {
				return
			}

			val currentQuest = island!!.currentQuest
			val targetQuest =
				Quests.instance.islandQuests.find { quest -> quest.type == QuestGoal.SMELT && quest.id == currentQuest }
					?: return

			val materialSmelted =
				XMaterial.matchXMaterial(event.currentItem!!.type).name


			if (materialSmelted != targetQuest.goalParameter) {
				return
			}
			island.addQuestData(targetQuest.id, event.currentItem!!.amount)
			island.sendTeamQuestProgress(targetQuest, event.whoClicked as Player)

			if (targetQuest.isComplete(island.getQuestCompletedAmount(targetQuest.id))) {
				island.completeQuest(iPlayer, targetQuest)
			}
			return
		}

		// This means they repaired an item.
		if (event.inventory.type == InventoryType.ANVIL && event.slot == 2 && event.view.bottomInventory != event.clickedInventory) {
			val iPlayer = (event.whoClicked as Player).getIPlayer()

			val island = iPlayer.getIsland()

			if (failsQuestCheckingPreRequisites(iPlayer, island, event.whoClicked.location)) {
				return
			}

			val currentQuest = island!!.currentQuest
			val targetQuest =
				Quests.instance.islandQuests.find { quest -> quest.type == QuestGoal.REPAIR && quest.id == currentQuest }
					?: return

			val materialToRepair = event.currentItem?.type

			if (materialToRepair.toString() != targetQuest.goalParameter) {
				return
			}

			island.addQuestData(targetQuest.id)
			island.sendTeamQuestProgress(targetQuest, event.whoClicked as Player)

			if (targetQuest.isComplete(island.getQuestCompletedAmount(targetQuest.id))) {
				island.completeQuest(iPlayer, targetQuest)
			}
			return
		}
	}


	@EventHandler
	fun onPlayerEnchant(event: EnchantItemEvent) {
		if (isNotInSkyblockWorld(event.enchanter.world)) {
			return
		}

		val iPlayer = (event.enchanter).getIPlayer()

		val island = iPlayer.getIsland()

		// Island checking hehe.
		if (failsQuestCheckingPreRequisites(iPlayer, island, event.enchantBlock.location)) {
			return
		}

		val currentQuest = island!!.currentQuest

		val targetQuest =
			Quests.instance.islandQuests.find { quest -> quest.type == QuestGoal.ENCHANT && quest.id == currentQuest }
				?: return


		val enchantNeeded = Enchantment.getByName(targetQuest.goalParameter.split("=")[0])
		val enchantLevel = Integer.parseInt(targetQuest.goalParameter.split("=")[1])


		if (!(event.enchantsToAdd.containsKey(enchantNeeded) && event.enchantsToAdd[enchantNeeded] == enchantLevel)) {
			return
		}


		island.addQuestData(targetQuest.id)
		island.sendTeamQuestProgress(targetQuest, event.enchanter)

		if (targetQuest.isComplete(island.getQuestCompletedAmount(targetQuest.id))) {
			island.completeQuest(iPlayer, targetQuest)
		}
	}


	@EventHandler
	fun onPlayerChat(event: AsyncPlayerChatEvent) {
		val iPlayer = event.player.getIPlayer()
		if (!iPlayer.hasIsland() || !iPlayer.isUsingIslandChat) return
		event.isCancelled = true
		iPlayer.getIsland()!!.messageAllOnlineIslandMembers(
			Config.instance.chatFormat.replace("{player}", event.player.name).replace("{message}", event.message)
		)
	}

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		// FUTURE CONTRIBUTORS: TRY TO SPLIT CHECKS INTO SMALLER BLOCKS.
		val block = event.clickedBlock
		if (block == null || isNotInSkyblockWorld(block.world)) {
			return
		}

		// necessity
		val location = block.location
		val iPlayer = (event.player).getIPlayer()

		// Check if they have an island or co-op island, if not, deny.
		if (!iPlayer.hasCoopIsland() && !iPlayer.hasIsland() && !iPlayer.inBypass) {
			iPlayer.message(Message.instance.listenerActionDeniedCreateAnIslandFirst)
			event.isCancelled = true
			return
		}

		// Check if they can use the block on the island, or co-op island.
		if (!canUseBlockAtLocation(iPlayer, location)) {
			iPlayer.message(Message.instance.listenerPlayerCannotInteract)
			event.isCancelled = true
			return
		}

		// Obsidian to Lava Bucket Check
		if (block.type == XMaterial.OBSIDIAN.parseMaterial() && event.item?.type == XMaterial.BUCKET.parseMaterial()) {
			if (!hasPermission(event.player, Permission.OBSIDIANTOLAVA)) {
				iPlayer.message(
					String.format(
						Message.instance.genericActionRequiresPermission,
						Permission.OBSIDIANTOLAVA.getFullPermissionNode()
					)
				)
				return
			}
			event.clickedBlock!!.type = Material.AIR
			// Have to use setItemInHand for pre-1.13 support.
			event.player.setItemInHand(XMaterial.LAVA_BUCKET.parseItem())
			iPlayer.message(Message.instance.listenerObsidianBucketLava)
			event.isCancelled = true
			return
		}
	}
}