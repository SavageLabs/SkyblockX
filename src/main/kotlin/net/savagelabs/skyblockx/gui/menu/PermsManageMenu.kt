package net.savagelabs.skyblockx.gui.menu

import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.content.InventoryContents
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.IslandPermission
import net.savagelabs.skyblockx.core.Rank
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.gui.BaseMenu
import net.savagelabs.skyblockx.gui.MenuConfig
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.GUIConfig
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.persist.data.SerializableItem
import org.bukkit.entity.Player

data class PermsMenuManageConfig(
    val guiTitle: String,
    val guiBackgroundItem: SerializableItem,
    val guiRows: Int
)

class PermsManageMenu(val rank: Rank) : BaseMenu(
    true,
    MenuConfig(
        GUIConfig.instance.permsMenuManageConfig.guiTitle.replace("{rank}", rank.data.identifier),
        GUIConfig.instance.permsMenuManageConfig.guiBackgroundItem,
        GUIConfig.instance.permsMenuManageConfig.guiRows,
        listOf()
)) {

    override fun fillContents(player: Player, contents: InventoryContents) {
        val iPlayer = getIPlayer(player)

        Config.instance.islandPermissions.forEach { (permission, _) ->
            val item = chooseItem(iPlayer, permission)

            contents.set(item.guiCoordinate.row, item.guiCoordinate.column, ClickableItem.of(item.buildItem()) {
                clickAction(iPlayer, permission, contents)
                //PermsEditActionGUI(rank).showGui(context.getPlayer())
            })
        }
    }

    fun clickAction(iPlayer: IPlayer, permission: IslandPermission, contents: InventoryContents) {
        // If the permission is disabled, it won't show
        if (permission.data.disabled) {
            return
        }

        if (!iPlayer.hasIslandPermission(IslandPermission.PERMS_EDIT)) {
            iPlayer.messageNoPermission(IslandPermission.PERMS_EDIT)
            return
        }

        if (iPlayer.islandRank!!.weight <= rank.weight) {
            iPlayer.message(Message.instance.genericHigherRankMessage)
            return
        }

        if (iPlayer.getIsland()!!.getPermissionsForRank(rank).contains(permission)) {
            iPlayer.getIsland()!!.rankPermission[rank]!!.remove(permission)
            contents.set(
                permission.data.disable.guiCoordinate.row,
                permission.data.disable.guiCoordinate.column,
                ClickableItem.of(permission.data.disable.buildItem())
                { clickAction(iPlayer, permission, contents) }
            )
        } else {
            iPlayer.getIsland()!!.rankPermission[rank]!!.add(permission)
            contents.set(
                permission.data.disable.guiCoordinate.row,
                permission.data.enabled.guiCoordinate.column,
                ClickableItem.of(permission.data.enabled.buildItem())
                { clickAction(iPlayer, permission, contents) }
            )
        }

        update(iPlayer.getPlayer(), contents)
    }

    fun chooseItem(player: IPlayer, permission: IslandPermission): IslandPermission.Item {
        if (rank == Rank.OWNER) {
            return permission.data.enabled
        }

        if(player.getIsland()!!.getPermissionsForRank(rank).contains(permission)) {
            return permission.data.enabled
        }

        return permission.data.disable
    }

}