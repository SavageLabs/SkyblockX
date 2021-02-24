package net.savagelabs.skyblockx.quest

import me.rayzr522.jsonmessage.JSONMessage
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.persist.Quests
import net.savagelabs.skyblockx.persist.data.SerializableItem
import net.savagelabs.skyblockx.placeholder.impl.QuestPlaceholder
import net.savagelabs.skyblockx.util.colored
import net.savagelabs.skyblockx.util.withPlaceholders
import org.bukkit.Bukkit
import org.bukkit.Location

data class Quest(
	val id: String,
	val name: String,
	val guiDisplayItem: SerializableItem,
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

	fun executeActivationTrigger(iPlayer: IPlayer) {
		actionsOnActivation.executeActions(QuestActions.QuestContext(iPlayer, iPlayer.getIsland()!!, this))
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
					"command" -> doCommand(context, getActionParams(context, action))
					"actionbar" -> doActionbarMessage(context, getActionParams(context, action))
					else -> {
					}
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
		context.contextIPlayer.getPlayer()!!.sendTitle(color(actionParams[0]), color(actionParams[1]))
	}

	fun doCommand(context: QuestContext, actionParams: List<String>) {
		actionParams.forEach { commandParam -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandParam) }
	}

	fun doActionbarMessage(context: QuestContext, actionParams: List<String>) {
		actionParams.forEach { actionbarMessage ->
			JSONMessage.create(color(actionbarMessage)).actionbar(context.contextIPlayer.getPlayer())
		}
	}

	fun parseQuestPlaceholders(context: QuestContext, line: String): String {
		return line.withPlaceholders(QuestPlaceholder::class.java, context).colored
	}

	class QuestContext(val contextIPlayer: IPlayer, val contextIsland: Island, val quest: Quest)
}

fun failsQuestCheckingPreRequisites(iPlayer: IPlayer, island: Island?, location: Location): Boolean {
	return (!iPlayer.hasIsland() || island!!.currentQuest == null || !island.containsBlock(location))
}

fun incrementQuestInOrder(island: Island) {
	if (Quests.instance.sendNextQuestInOrderMessages) sendQuestOrderMessage(island)
	if (island.currentQuestOrderIndex == null) return
	val quest =
		Quests.instance.islandQuests.find { quest -> quest.id == Quests.instance.questOrder[island.currentQuestOrderIndex!!] }
	island.changeCurrentQuest(quest?.id)

}

fun sendQuestOrderMessage(island: Island) {
	if (island.currentQuestOrderIndex == null) return
	val quest =
		Quests.instance.islandQuests.find { quest -> quest.id == Quests.instance.questOrder[island.currentQuestOrderIndex!!] }

	if (quest == null) {
		island.messageAllOnlineIslandMembers(Message.instance.questOrderNoNextQuestWasFound)
		return
	}

	island.messageAllOnlineIslandMembers(
		String.format(
			Message.instance.nextQuestMessage,
			quest.name
		)
	)
}