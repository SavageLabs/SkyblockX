package net.savagelabs.skyblockx.upgrade

import net.savagelabs.skyblockx.gui.wrapper.GUIItem
import net.savagelabs.skyblockx.persist.Config.Companion.instance

/**
 * This function is used to throw an exception if, and ONLY if the preview items
 * of an internal Upgrade is absent in the configuration fetched from.
 *
 * @param type [String] the type of loading that was attempted but failed.
 * @return [Nothing]
 * @throws IllegalStateException
 */
internal fun errorByPreview(id: String, type: String): Nothing =
    error("The internal upgrade '$id' failed to load $type from config.")

/**
 * Attempt to fetch preview items from corresponding upgrade section in the configuration.
 * If the upgrade's info per level base is absent, this function will throw an exception.
 *
 * @param id [String] identifier to fetch levels by.
 * @return [Map]
 * @throws IllegalStateException
 */
internal fun levelItemsOrErrorByPreview(id: String): Map<Int, UpgradeLevelInfo> =
    instance.upgrades.find { it.id == id }?.levels ?: errorByPreview(id, "preview items")

/**
 * Attempt to fetch max level item from corresponding upgrade section in the configuration.
 * If the upgrade's max level is absent, this function will throw an exception.
 *
 * @param id [String] identifier to fetch max level item by.
 * @return [GUIItem]
 * @throws IllegalStateException
 */
internal fun maxLevelItemOrErrorByPreview(id: String): GUIItem =
    instance.upgrades.find { it.id == id }?.maxLevelItem ?: errorByPreview(id, "max level item")

/**
 * This class contains data about a customizable upgrade.
 */
data class Upgrade constructor(
	val id: String,
	val typeId: String,
	val parameter: String,
	val levels: Map<Int, UpgradeLevelInfo> = levelItemsOrErrorByPreview(id),
	val maxLevelItem: GUIItem = maxLevelItemOrErrorByPreview(id)
)