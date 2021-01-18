package net.savagelabs.skyblockx.upgrade

import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.gui.wrapper.GUIItem
import net.savagelabs.skyblockx.persist.Config.Companion.instance
import org.bukkit.event.Listener

/**
 * This function is used to throw an exception if, and ONLY if the preview items
 * of an internal Upgrade is absent in the configuration fetched from.
 *
 * @param type [String] the type of loading that was attempted but failed.
 * @return [Nothing]
 * @throws IllegalStateException
 */
internal fun Upgrade.errorByPreview(type: String): Nothing =
		error("The internal upgrade '$id' failed to load $type from config.")

/**
 * Attempt to fetch preview items from corresponding upgrade section in the configuration.
 * If the upgrade's info per level base is absent, this function will throw an exception.
 *
 * @return [Map]
 * @throws IllegalStateException
 */
internal fun Upgrade.levelItemsOrErrorByPreview(): Map<Int, UpgradeLevelInfo> =
		instance.upgrades[id]?.upgradeInfoPerLevel ?: errorByPreview("preview items")

/**
 * Attempt to fetch max level item from corresponding upgrade section in the configuration.
 * If the upgrade's max level is absent, this function will throw an exception.
 *
 * @return [GUIItem]
 * @throws IllegalStateException
 */
internal fun Upgrade.maxLevelItemOrErrorByPreview(): GUIItem =
		instance.upgrades[id]?.maxLevelItem ?: errorByPreview("max level item")

/**
 * This interface is used to create both internal and external upgrades.
 */
interface Upgrade {
	/**
	 * [String] the id of this upgrade.
	 * Please note that this ID will be used for registration, with that said..
	 * choose wisely to avoid registration collisions.
	 */
	val id: String

	/**
	 * [Map] this map contains all levels and their information.
	 */
	val preview: Map<Int, UpgradeLevelInfo>

	/**
	 * [GUIItem] this gui item is the max level item shown in the upgrade GUI.
	 */
	val maxLevelItem: GUIItem

	/**
	 * [Listener] this listener can either be assigned with null or an instance of
	 * a class that implements Listener. The assigned listener will be registered
	 * during the same period the whole upgrade is. This property is merely for upgrade
	 * functionality with a clean base.
	 */
	val listener: Listener?

	/**
	 * This function is invoked during the time of a player leveling up this upgrade.
	 * So use this function to handle data etc. for your upgrade.
	 */
	fun commence(player: IPlayer, island: Island, level: Int)

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