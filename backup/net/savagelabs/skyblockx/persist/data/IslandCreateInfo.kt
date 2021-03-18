package net.savagelabs.skyblockx.persist.data

import net.savagelabs.skyblockx.gui.wrapper.GUICoordinate

data class IslandCreateInfo(
	val name: String,
	val requirementPermission: String,
	val guiCoordinate: GUICoordinate,
	val item: SerializableItem,
	val structureFile: String,
	val netherFile: String,
	val endFile: String
)