package net.savagelabs.skyblockx.quest

import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.isNotInSkyblockWorld
import net.savagelabs.skyblockx.persist.Quests
import org.bukkit.Location
import org.bukkit.event.Event
import org.bukkit.event.Listener

/**
 * This abstraction layer makes custom quest goals possible. It merely handles the work behind them.
 */
abstract class QuestGoal<T : Event> constructor(val id: String, val name: String) : Listener {
    /**
     * This companion object contains static functions to help with goal referencing.
     */
    companion object {
        /**
         * This constant is the default predicate for parameters.
         */
        val DEFAULT_PARAMETERS_PREDICATE: (String, String) -> Boolean = { goalParameter, toMatch -> goalParameter.equals(toMatch, ignoreCase = true) }

        /**
         * Fetch target quest by island and parameter.
         *
         * @param player         [IPlayer] the island player involved.
         * @param id             [String] the id of the quest goal related to this operation.
         * @return instance of the corresponding target [Quest] if present, otherwise null.
         */
        fun checkAndGet(player: IPlayer, id: String, parametersPredicate: (String) -> Boolean): Quest? {
            // make sure the world is a skyblock one
            if (isNotInSkyblockWorld(player.getPlayer()?.world ?: return null)) {
                return null
            }

            // fetch the island and current quest id
            val island = player.getIsland() ?: return null
            val quest = island.currentQuest ?: return null

            // fetch the target quest by id
            val target = Quests.instance.islandQuests.find { flow ->
                flow.goalId == id && flow.id == quest
            } ?: return null

            // last but not least check if the parameter(s) `are` correct
            val goalParameter = target.goalParameter
            if (!parametersPredicate(goalParameter)) {
                return null
            }

            // nice, return the corresponding
            return target
        }

        /**
         * Get whether or not a location is part of an island.
         *
         * @param island   [Island] the island to check the location for.
         * @param location [Location] the location involved in this check.
         * @return a [Boolean] correspondent to whether or not the location is part of [island].
         *
         * Almost every goal's [QuestGoal.work] function should start with this,
         * if the event is not already handled on the outside. This is just to
         * ensure that the conditions outside of [checkAndGet] are met.
         *
         * The usage is more or less:
         * `
         * if (!isLocationPartOf(island, location)) {
         *   return;
         * }
         */
        fun isLocationPartOf(island: Island, location: Location): Boolean =
                island.containsBlock(location)
    }

    /**
     * Handle the quest data when a quest was worked on through [work].
     *
     * @param island       [Island] the island involved in this quest.
     * @param quest        [Quest] the quest's data to handle for [island].
     * @param islandPlayer [IPlayer] the player involved in this process.
     */
    open fun handleQuestData(island: Island, quest: Quest, islandPlayer: IPlayer) {
        // necessity
        val questId = quest.id

        // add data and send progress
        island.addQuestData(questId, 1)
        island.sendTeamQuestProgress(quest, islandPlayer.getPlayer()!!)

        // if the quest is complete, invoke the function accordingly
        if (!quest.isComplete(island.getQuestCompletedAmount(questId))) return
        island.completeQuest(islandPlayer, quest)
    }

    /**
     * This function handles the event of which this goal is referenced with.
     */
    abstract fun T.work()
}