package net.savagelabs.skyblockx.gui.menu

import net.savagelabs.skyblockx.gui.MenuConfig
import net.savagelabs.skyblockx.gui.wrapper.MenuItem
import net.savagelabs.skyblockx.persist.data.IslandCreateInfo
import net.savagelabs.skyblockx.persist.data.SerializableItem

data class CreateMenuConfig(
    val guiTitle: String,
    val guiBackgroundItems: SerializableItem,
    val guiRows: Int,
    val islandInfo: List<IslandCreateInfo>,
    val guiMenuItems: List<MenuItem>
) : MenuConfig(
    guiTitle,
    guiBackgroundItems,
    guiRows,
    guiMenuItems
)


class CreateMenu(

)