package net.savagelabs.skyblockx.listener

import com.cryptomorin.xseries.XMaterial
import net.savagelabs.skyblockx.core.*
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.sedit.SkyBlockEdit
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent

object PlayerListener : Listener {
	@EventHandler
	fun onPlayerTeleport(event: PlayerTeleportEvent) {
		updateWorldBorder(event.player, event.to!!, 10L)
		if (event.cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && event.to != null && !isNotInSkyblockWorld(
				event.from.world!!
			) && getIslandFromLocation(event.from) != getIslandFromLocation(event.to!!)
		) {
			event.isCancelled = true
		}
	}

	@EventHandler
	fun onPlayerChangeWorldEvent(event: PlayerPortalEvent) {
		if (event.from.world?.name != Config.instance.skyblockWorldName || event.cause != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
			return
		}
		val iPlayer = (event.player).getIPlayer()
		event.isCancelled = true
		val islandFromLocation = getIslandFromLocation(event.from)
		val newLoc = islandFromLocation?.getIslandCenter()?.clone() ?: return
		newLoc.world = Bukkit.getWorld(Config.instance.skyblockWorldNameNether)
		if (!islandFromLocation.beenToNether) {
			SkyBlockEdit.pasteIsland(islandFromLocation.netherFilePath.replace(".structure", ""), newLoc, null)
			islandFromLocation.beenToNether = true
		}
		event.player.teleport(newLoc, PlayerTeleportEvent.TeleportCause.PLUGIN)
		iPlayer.message(Message.instance.islandNetherTeleported)
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