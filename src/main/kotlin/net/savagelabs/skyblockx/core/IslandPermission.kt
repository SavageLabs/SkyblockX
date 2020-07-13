package net.savagelabs.skyblockx.core

import com.cryptomorin.xseries.XMaterial
import net.savagelabs.savagepluginx.item.ItemBuilder
import net.savagelabs.skyblockx.gui.wrapper.GUICoordinate
import net.savagelabs.skyblockx.persist.Config
import org.bukkit.inventory.ItemStack

enum class IslandPermission {

    HOME_GO,
    HOME_REMOVE,
    HOME_SET,
    MEMBER_LIST,
    MEMBER_MANAGE,
    MEMBER_KICK,
    MEMBER_INVITE,
    MEMBER_PROMOTE,
    MEMBER_DEMOTE,
    ISLAND_VISIT_STATE,
    ISLAND_BIOME,
    SELF_BORDER,
    ISLAND_CALCULATE,
    CHEST_OPEN,
    CHEST_TAKE,
    CHEST_PUT,
    ISLAND_COOP,
    ISLAND_GO,
    ISLAND_MENU,
    ISLAND_RENAME,
    ISLAND_NAME,
    COOP_REMOVE,
    GO_SET,
    ISLAND_WORTH,
    SPAWNER_PLACE,
    SPAWNER_MINE,
    BUILD,
    PLACE,
    INTERACT,
    PERMS_VIEW,
    PERMS_EDIT,
    ISLAND_CHAT,
    ISLAND_PAYPAL,
    ISLAND_SETPAYPAL;

    // The only reason I made this a method and not a variable is because I didn't know if the config class would be initialized before the variable :/
    val data: Data
        get() {
            return Config.instance.islandPermissions[this]!!
        }

    data class Data(val internalName: String, val default: Rank, val disabled: Boolean, val enabled: Item, val disable: Item)

    data class Item(
        var material: XMaterial,
        var name: String,
        var lore: List<String>,
        var glowing: Boolean,
        var amount: Int,
        var guiCoordinate: GUICoordinate
    ) {

        fun buildItem(): ItemStack {
            return ItemBuilder(material.parseItem()!!).name(name).lore(lore).amount(amount).glowing(glowing).build()
        }

    }

}

fun generateDefaultPermissionMap(): HashMap<IslandPermission, IslandPermission.Data> {
    val map = hashMapOf<IslandPermission, IslandPermission.Data>()

    var column = 0
    var row = 0
    for(i in IslandPermission.values().indices) {
        val permission = IslandPermission.values()[i]

        map[permission] = IslandPermission.Data(
            permission.name.toLowerCase(),
            Rank.MEMBER,
            false,
            IslandPermission.Item(
                XMaterial.GREEN_STAINED_GLASS_PANE,
                permission.name.split("_").joinToString(" ").toLowerCase().capitalize(),
                listOf("Default enabled description"),
                true,
                1,
                GUICoordinate(column, row)
            ),
            IslandPermission.Item(
                XMaterial.RED_STAINED_GLASS_PANE,
                permission.name.split("_").joinToString(" ").toLowerCase().capitalize(),
                listOf("Default disabled description"),
                false,
                1,
                GUICoordinate(column, row)
            ))

        if (++column > 8) {
            row++
            column = 0
        }
    }

    return map
}

fun getDefaultPermissionMap(): HashMap<Rank, MutableSet<IslandPermission>> {
    val map = hashMapOf<Rank, MutableSet<IslandPermission>>()

    for (rank in Rank.values()) {
        if (!map.containsKey(rank)) {
            map[rank] = mutableSetOf()
        }
    }

    for ((permission, data) in Config.instance.islandPermissions) {
        for (i in data.default.weight..2) {
            map[getByWeight(i)]!!.add(permission)
        }
    }

    return map
}