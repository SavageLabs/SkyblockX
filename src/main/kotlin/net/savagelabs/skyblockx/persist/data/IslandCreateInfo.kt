package net.savagelabs.skyblockx.persist.data

import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem

data class IslandCreateInfo(
    val name: String,
    val requirementPermission: String,
    val guiIndex: Int,
    val item: SerializableItem,
    val structureFile: String,
    val netherFile: String
)