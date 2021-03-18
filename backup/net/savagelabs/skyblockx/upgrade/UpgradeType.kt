package net.savagelabs.skyblockx.upgrade

import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.persist.Config
import org.bukkit.event.Event
import org.bukkit.event.Listener

/**
 * This abstraction layer is used to hold and handle upgrade sections both internally and externally.
 *
 * Please note that this ID will be used for registration, with that said..
 * choose wisely to avoid registration collisions.
 *
 * @param id [String] the id of this upgrade type.
 */
abstract class UpgradeType<EventType : Event> constructor(val id: String) {
    /**
     * [HashMap] this map is the registry of all upgrades; key being id and value being the Upgrade object.
     */
    val registry: HashMap<String, Upgrade> = Config.instance.upgrades
        .filter { it.typeId == id }
        .associateBy(Upgrade::id) as HashMap

    /**
     * [Listener] this listener will be reassigned with the [UpgradeType]'s own
     * instance of an EventExecutor & Listener to register custom events
     * based on this Upgrade without any issues whatsoever.
     */
    @PublishedApi
    internal open var listener: Listener? = null

    /**
     * This function is invoked during the time of a player leveling up an upgrade in this type.
     * So use this function to handle data etc. for your upgrade.
     *
     * @param player  [IPlayer] the IPlayer instance of the player whom is commencing this levelup.
     * @param island  [Island] the Island instance of the player whom is commencing this levelup.
     * @param level   [Int] the level that this Island will be leveling up to for this Upgrade.
     * @param upgrade [Upgrade] the upgrade that had been leveled up.
     */
    abstract fun commence(player: IPlayer, island: Island, level: Int, upgrade: Upgrade)

    /**
     * This function will be invoked when anything triggers the corresponding event.
     * Triggered only if the event type is not "Event", and of course not abstract.
     *
     * @param event [EventType] the type of event that this Upgrade has specified.
     */
    open fun onEvent(event: EventType) {}

    /**
     * Get the price of a level for passed down upgrade by the level's input.
     *
     * @param level   [Int] the level to fetch price for.
     * @param upgrade [Upgrade] the upgrade to fetch level info from.
     * @param default [Double] the default value to be used if a price is absent on a level.
     * @return [Double]
     */
    fun priceOf(level: Int, upgrade: Upgrade, default: Double = 0.0) =
        upgrade.levels[level]?.price ?: default
}