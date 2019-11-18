package io.illyria.skyblockx.listener

import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.core.getIPlayer
import io.illyria.skyblockx.persist.Message
import io.illyria.skyblockx.sedit.Position
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class SEditListener : Listener {


    @EventHandler
    fun choosePosition(event: PlayerInteractEvent) {
        if (event.clickedBlock == null || event.action != Action.RIGHT_CLICK_BLOCK) {
            return
        }


        val iPlayer = getIPlayer(event.player)
        if (!iPlayer.choosingPosition) {
            return
        }

        if (iPlayer.chosenPosition == Position.POSITION1) {
            iPlayer.pos1 = event.clickedBlock!!.location
            event.player.sendMessage(
                color(
                    Message.messagePrefix +
                            String.format(
                                Message.skyblockEditPositionSet,
                                1.toString(),
                                "${iPlayer.pos1!!.x}, ${iPlayer.pos1!!.y}, ${iPlayer.pos1!!.z} in ${iPlayer.pos1!!.world!!.name}"
                            )
                )
            )
            iPlayer.choosingPosition = false
        } else {
            iPlayer.pos2 = event.clickedBlock!!.location

            event.player.sendMessage(
                color(
                    Message.messagePrefix +
                            String.format(
                                Message.skyblockEditPositionSet,
                                2.toString(),
                                "${iPlayer.pos2!!.x}, ${iPlayer.pos2!!.y}, ${iPlayer.pos2!!.z} in ${iPlayer.pos2!!.world!!.name}"
                            )
                )
            )
            iPlayer.choosingPosition = false
        }
        event.isCancelled = true


    }


}
