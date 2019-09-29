package net.savagellc.savageskyblock.persist.data

import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem

data class IslandCreateInfo(val requirementPermission: String, val guiIndex: Int, val item: SerializableItem, val structureFile: String)