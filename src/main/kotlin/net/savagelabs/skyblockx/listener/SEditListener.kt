package net.savagelabs.skyblockx.listener

import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.sedit.Position
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object SEditListener : Listener {
	@EventHandler
	fun choosePosition(event: PlayerInteractEvent) {
		val clickedBlock = event.clickedBlock ?: return
		if (event.action != Action.RIGHT_CLICK_BLOCK) {
			return
		}

		val iPlayer = (event.player).getIPlayer()
		if (!iPlayer.choosingPosition) {
			return
		}

		val location = clickedBlock.location
		val isOne = iPlayer.chosenPosition == Position.POSITION1
		if (isOne) iPlayer.pos1 = location else iPlayer.pos2 = location

		event.player.sendMessage(
			color(
				Message.instance.messagePrefix +
					color(
						String.format(
							Message.instance.skyblockEditPositionSet,
							(if (isOne) 1 else 2).toString(),
							"${location.x}, ${location.y}, ${location.z} in ${location.world?.name ?: "Unknown"}"
						)
					)
			)
		)

		iPlayer.choosingPosition = false
		event.isCancelled = true
	}
}