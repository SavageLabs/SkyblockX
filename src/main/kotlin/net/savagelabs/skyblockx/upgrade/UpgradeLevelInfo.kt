package net.savagelabs.skyblockx.upgrade

import net.savagelabs.skyblockx.gui.wrapper.GUIItem

/**
 * This data class is used to store information about an Upgrade's level info.
 */
data class UpgradeLevelInfo(val price: Double, val parameter: String, val itemAtLevel: GUIItem)