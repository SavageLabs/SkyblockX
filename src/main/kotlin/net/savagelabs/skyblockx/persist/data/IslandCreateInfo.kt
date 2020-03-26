package net.savagelabs.skyblockx.persist.data

data class IslandCreateInfo(
    val name: String,
    val requirementPermission: String,
    val guiIndex: Int,
    val item: net.savagelabs.skyblockx.persist.data.SerializableItem,
    val structureFile: String,
    val netherFile: String
)