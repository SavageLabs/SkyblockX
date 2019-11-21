package io.illyria.skyblockx.quest

import io.illyria.skyblockx.core.IPlayer
import io.illyria.skyblockx.core.Island
import io.illyria.skyblockx.persist.Message
import io.illyria.skyblockx.persist.Quests
import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound

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

class QuestActions(val actions: List<String>) {
    fun executeActions(contextIPlayer: IPlayer, contextIsland: Island) {
        actions.forEach { action ->
            run {
                when(action.split("(")[0].toLowerCase()) {
                    "message" -> println("do message")
                    "sendtitle" -> println("do sendtitle")
                    "executecommand" -> println("do cmd")
                    else -> pass
                }
            }
        }
    }

    fun getActionParams(rawAction: String): List<String> {
        val statement = rawAction.split("(", limit = 1)[1].replace(")","")
        return statement.split(":::").toList()
    }

    fun doMessage(actionParams: List<String>) {
        actionParams.forEach {  }
    }


    fun parseQuestPlaceholders(line: String) {
        line.replace("{player}", "")
    }

}



val pass: Unit = Unit

fun failsQuestCheckingPreRequisites(iPlayer: IPlayer, island: Island?, location: Location): Boolean {
    return (!iPlayer.hasIsland() || island!!.currentQuest == null || !island.containsBlock(location))
}

fun incrementQuestInOrder(island: Island) {
    if (Quests.sendNextQuestInOrderMessages) sendQuestOrderMessage(island)
    if (island.currentQuestOrderIndex == null) return
    val quest = Quests.islandQuests.find { quest -> quest.id == Quests.questOrder[island.currentQuestOrderIndex!!] }
    island.changeCurrentQuest(quest?.id)

}

fun sendQuestOrderMessage(island: Island) {
    if (island.currentQuestOrderIndex == null) return
    val quest = Quests.islandQuests.find { quest -> quest.id == Quests.questOrder[island.currentQuestOrderIndex!!] }

    if (quest == null) {
        island.messageAllOnlineIslandMembers(Message.messagePrefix + Message.questOrderNoNextQuestWasFound)
        return
    }

    island.messageAllOnlineIslandMembers(Message.messagePrefix + String.format(Message.nextQuestMessage, quest.name))
}