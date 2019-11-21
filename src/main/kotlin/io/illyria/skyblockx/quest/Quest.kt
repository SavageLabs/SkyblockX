package io.illyria.skyblockx.quest

import io.illyria.skyblockx.core.IPlayer
import io.illyria.skyblockx.core.Island
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.persist.Message
import io.illyria.skyblockx.persist.Quests
import me.rayzr522.jsonmessage.JSONMessage
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
    val actionsOnActivation: QuestActions,
    val actionsOnCompletion: QuestActions
) {


    fun isComplete(amount: Int): Boolean {
        return amountTillComplete <= amount
    }


    fun giveRewards(iPlayer: IPlayer) {
        actionsOnCompletion.executeActions(QuestActions.QuestContext(iPlayer, iPlayer.getIsland()!!, this))
    }

}

class QuestActions(val actions: List<String>) {
    fun executeActions(context: QuestContext) {
        actions.forEach { action ->
            run {
                when (action.split("(")[0].toLowerCase()) {
                    "message" -> doMessage(context, getActionParams(context, action))
                    "title" -> doTitle(context, getActionParams(context, action))
                    "executecommand" -> doCommand(context, getActionParams(context, action))
                    "actionbar" -> doActionbarMessage(context, getActionParams(context, action))
                    else -> pass
                }
            }
        }
    }

    fun getActionParams(context: QuestContext, rawAction: String): List<String> {
        val statement = rawAction.split("(", limit = 2)[1].replace(")", "")
        return statement.split(":::").toList().map { line -> parseQuestPlaceholders(context, line) }.toList()
    }

    fun doMessage(context: QuestContext, actionParams: List<String>) {
        actionParams.forEach { messageParam -> context.contextIPlayer.message(messageParam) }
    }
    fun doTitle(context: QuestContext, actionParams: List<String>) {
        // Not using new method with the time cuz 1.8 :/
        context.contextIPlayer.getPlayer()?.sendTitle(actionParams[0] ?: "", actionParams[1] ?: "")
    }
    fun doCommand(context: QuestContext, actionParams: List<String>) {
        actionParams.forEach { commandParam -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandParam) }
    }
    fun doActionbarMessage(context: QuestContext, actionParams: List<String>) {
        actionParams.forEach { actionbarMessage -> JSONMessage.create(color(actionbarMessage)).actionbar(context.contextIPlayer.getPlayer()) }
    }


    fun parseQuestPlaceholders(context: QuestContext, line: String): String {
        return line
            .replace("{player}", context.contextIPlayer.name)
            .replace("{uuid}", context.contextIPlayer.uuid)
            .replace("{quest-name}", context.quest.name)
            .replace("{quest-amount-till-complete}", "${context.quest.amountTillComplete}")

    }

    class QuestContext(val contextIPlayer: IPlayer, val contextIsland: Island, val quest: Quest)

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