package net.savagelabs.skyblockx.registry.impl

import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.quest.QuestGoal
import net.savagelabs.skyblockx.quest.goals.*
import net.savagelabs.skyblockx.registry.Identifier
import net.savagelabs.skyblockx.registry.Registry
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener

/**
 * This implementation is the [Registry] for available quest goals.
 */
object QuestGoalRegistry : Registry<Identifier, QuestGoal<out Event>>() {
    /**
     * The name of this repository.
     */
    override val name: String = "Quest Goal"

    /**
     * Register a new quest goal.
     */
    override fun register(identifier: Identifier, value: QuestGoal<out Event>) {
        super.register(identifier, value)
        this.listenTo(value)
    }

    /**
     * Unregister a quest goal by it's identifier.
     */
    override fun unregister(identifier: Identifier): QuestGoal<out Event>? {
        return super.unregister(identifier).also {
            if (it == null) return@also
            HandlerList.unregisterAll(it)
        }
    }

    /**
     * Register all default quest goals.
     */
    internal fun defaults() {
        register(Identifier("BREAK_BLOCKS"), BreakBlocksGoal)
        register(Identifier("PLACE_BLOCKS"), PlaceBlocksGoal)
        register(Identifier("CRAFT"), CraftGoal)
        register(Identifier("SMELT"), SmeltGoal)
        register(Identifier("REPAIR"), RepairGoal)
        register(Identifier("ENCHANT"), EnchantGoal)
        register(Identifier("FISHING"), FishingGoal)
        register(Identifier("KLL_MOBS"), KillMobsGoal)
    }

    /**
     * Listen to a [Listener] by passed down instance.
     *
     * @param listener the [Listener] to start listening to.
     */
    private fun listenTo(listener: Listener) =
            Bukkit.getPluginManager().registerEvents(listener, SkyblockX.skyblockX)
}