package io.illyria.skyblockx.quest

import io.illyria.skyblockx.core.IPlayer
import io.illyria.skyblockx.core.Island
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Message
import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem
import org.bukkit.Bukkit
import org.bukkit.Location

data class Quest(
    val id: String,
    val name: String,
    val guiDisplayItem: SerializableItem,
    val guiDisplayIndex: Int,
    val type: QuestGoal,
    val goalParameter: String,
    val amountTillComplete: Int,
    val oneTime: Boolean,
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

fun sendQuestOrderMessage(island: Island) {
    val quest = Config.islandQuests.find { quest -> quest.id == Config.questOrder[island.currentQuestOrderIndex] }

    if (quest == null) {
        island.messageAllOnlineIslandMembers(Message.questOrderNoNextQuestWasFound)
        return
    }

    island.messageAllOnlineIslandMembers(String.format(Message.nextQuestMessage, quest.name))

}