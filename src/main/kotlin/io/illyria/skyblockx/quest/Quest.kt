package io.illyria.skyblockx.quest

import io.illyria.skyblockx.core.IPlayer
import io.illyria.skyblockx.core.Island
import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem
import org.bukkit.Bukkit
import org.bukkit.Location

data class Quest(
    val name: String,
    val guiDisplayItem: SerializableItem,
    val guiDisplayIndex: Int,
    val type: QuestGoal,
    val goalParameter: String,
    val amountTillComplete: Int,
    val commandsToExecuteOnCompletion: List<String>
) {


    fun isComplete(amount: Int): Boolean {
        return amountTillComplete <= amount
    }


    fun giveRewards(iPlayer: IPlayer) {
        val player = iPlayer.getPlayer()
        for (command in commandsToExecuteOnCompletion) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.name))
        }
    }

}

fun failsQuestCheckingPreRequisites(iPlayer: IPlayer, island: Island?, location: Location): Boolean {
    return  (!iPlayer.hasIsland() || island!!.currentQuest == null || !island.containsBlock(location))
}