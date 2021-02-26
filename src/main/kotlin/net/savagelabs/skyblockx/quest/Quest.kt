package net.savagelabs.skyblockx.quest

import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.persist.data.SerializableItem

/**
 * This class holds data of a quest.
 */
class QuestContext(val contextIPlayer: IPlayer, val contextIsland: Island, val quest: Quest)

/**
 * This class contains all data needed from a quest, including functionality.
 */
data class Quest(
		val id: String,
		val name: String,
		val guiDisplayItem: SerializableItem,
		val goalId: String,
		val goalParameter: String,
		val amountTillComplete: Int,
		val oneTime: Boolean,
		val actionsOnActivation: List<String>,
		val actionsOnCompletion: List<String>
) {
	/**
	 * Get whether or not this quest has been completed by current amount.
	 *
	 * @param amount [Int] the current amount to check via.
	 */
	fun isComplete(amount: Int): Boolean = amountTillComplete <= amount

	/**
	 * Trigger activation or completion actions.
	 *
	 * @param isActivation [Boolean] whether or not this call should trigger the activation actions.
	 * @param islandPlayer [IPlayer] the player related to this trigger.
	 */
	fun trigger(isActivation: Boolean, islandPlayer: IPlayer) {
		val context = QuestContext(islandPlayer, islandPlayer.getIsland() ?: return, this)
		QuestActionHelper.execute(if (isActivation) actionsOnActivation else actionsOnCompletion, context)
	}
}