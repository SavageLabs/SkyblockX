package net.savagelabs.skyblockx.upgrade

import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.gui.wrapper.GUIItem
import net.savagelabs.skyblockx.persist.Config.Companion.instance
import org.bukkit.event.Event
import org.bukkit.event.Listener

/**
 * This function is used to throw an exception if, and ONLY if the preview items
 * of an internal Upgrade is absent in the configuration fetched from.
 *
 * @param type [String] the type of loading that was attempted but failed.
 * @return [Nothing]
 * @throws IllegalStateException
 */
internal fun Upgrade<*>.errorByPreview(type: String): Nothing =
		error("The internal upgrade '$id' failed to load $type from config.")

/**
 * Attempt to fetch preview items from corresponding upgrade section in the configuration.
 * If the upgrade's info per level base is absent, this function will throw an exception.
 *
 * @return [Map]
 * @throws IllegalStateException
 */
internal fun Upgrade<*>.levelItemsOrErrorByPreview(): Map<Int, UpgradeLevelInfo> =
		instance.upgrades[id]?.upgradeInfoPerLevel ?: errorByPreview("preview items")

/**
 * Attempt to fetch max level item from corresponding upgrade section in the configuration.
 * If the upgrade's max level is absent, this function will throw an exception.
 *
 * @return [GUIItem]
 * @throws IllegalStateException
 */
internal fun Upgrade<*>.maxLevelItemOrErrorByPreview(): GUIItem =
		instance.upgrades[id]?.maxLevelItem ?: errorByPreview("max level item")

/**
 * This abstraction class is used to create both internal and external upgrades
 * to later register them.
 *
 * Please note that this ID will be used for registration, with that said..
 * choose wisely to avoid registration collisions.
 *
 * @param id [String] the id of this upgrade.
 */
abstract class Upgrade<EventType : Event> constructor(val id: String) {
	/**
	 * [Map] this map contains all levels and their information.
	 */
	abstract val preview: Map<Int, UpgradeLevelInfo>

	/**
	 * [GUIItem] this gui item is the max level item shown in the upgrade GUI.
	 */
	abstract val maxLevelItem: GUIItem

	/**
	 * [Listener] this listener will be reassigned with the Upgrade's own
	 * instance of an EventExecutor & Listener to register custom events
	 * based on this Upgrade without any issues whatsoever. Do yourself a favour
	 * and don't reassign this.
	 */
	@PublishedApi internal open var listener: Listener? = null

	/**
	 * This function is invoked during the time of a player leveling up this upgrade.
	 * So use this function to handle data etc. for your upgrade.
	 *
	 * @param player [IPlayer] the IPlayer instance of the player whom is commencing this levelup.
	 * @param island [Island] the Island instance of the player whom is commencing this levelup.
	 * @param level [Int] the level that this Island will be leveling up to for this Upgrade.
	 */
	abstract fun commence(player: IPlayer, island: Island, level: Int)

	/**
	 * This function will be invoked when anything triggers the corresponding event.
	 * Triggered only if the event type is not "Event", and of course not abstract.
	 *
	 * @param event [EventType] the type of event that this Upgrade has specified.
	 */
	open fun onEvent(event: EventType) {}

	/**
	 * Get the price of a level for this upgrade by the level's input.
	 *
	 * @param level   [Int] the level to check price for.
	 * @param default [Double] the default value to be used if a price is absent on a level.
	 * @return [Double]
	 */
	fun priceOf(level: Int, default: Double = 0.0): Double =
			instance.upgrades[this.id]?.upgradeInfoPerLevel?.get(level)?.price ?: default
}