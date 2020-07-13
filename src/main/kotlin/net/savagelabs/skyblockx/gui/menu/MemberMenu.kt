package net.savagelabs.skyblockx.gui.menu

import com.deanveloper.skullcreator.SkullCreator
import fr.minuskube.inv.ClickableItem
import fr.minuskube.inv.content.InventoryContents
import fr.minuskube.inv.content.SlotIterator
import net.savagelabs.savagepluginx.item.ItemBuilder
import net.savagelabs.skyblockx.core.*
import net.savagelabs.skyblockx.gui.*
import net.savagelabs.skyblockx.gui.wrapper.GUICoordinate
import net.savagelabs.skyblockx.gui.wrapper.GUIItem
import net.savagelabs.skyblockx.gui.wrapper.MenuItem
import net.savagelabs.skyblockx.persist.GUIConfig
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.persist.data.SerializableItem
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.util.*

data class MemberMenuConfig(
    val guiTitle: String,
    val guiBackgroundItem: SerializableItem,
    val guiRows: Int,
    val guiMenuItems: List<MenuItem>,
    val memberNameFormat: String,
    val memberLoreFormat: List<String>,
    val membersStartCoordinate: GUICoordinate,
    val membersPerPage: Int,
    val nextPageItem: GUIItem,
    val previousPageItem: GUIItem
)



class MemberMenu(val player: Player, val island: Island) : PagedMenu(PagedMenuConfig(
    GUIConfig.instance.memberMenuConfig.guiTitle,
    GUIConfig.instance.memberMenuConfig.guiBackgroundItem,
    GUIConfig.instance.memberMenuConfig.guiRows,
    SlotIterator.Type.HORIZONTAL,
    GUIConfig.instance.memberMenuConfig.membersStartCoordinate,
    GUIConfig.instance.memberMenuConfig.membersPerPage,
    GUIConfig.instance.memberMenuConfig.nextPageItem,
    GUIConfig.instance.memberMenuConfig.previousPageItem,
    GUIConfig.instance.memberMenuConfig.guiMenuItems
)) {
    override fun getPageItems(): List<ClickableItem> {
        val memberMenuConfig = GUIConfig.instance.memberMenuConfig
        val iPlayer = player.getIPlayer()
        return iPlayer.getIsland()!!.getIslandMembers(false).map { islandMember ->
          ClickableItem.of(
                ItemBuilder(SkullCreator.itemFromUuid(UUID.fromString(islandMember.uuid)))
                    .name(memberMenuConfig.memberNameFormat.replace("{player}", islandMember.name).replace("{rank}", islandMember.islandRank!!.data.identifier))
                    .lore(memberMenuConfig.memberLoreFormat.map { it.replace("{rank}", islandMember.islandRank!!.data.identifier) })
                    .build()
            ) {
                if (!iPlayer.hasIslandPermission(IslandPermission.MEMBER_MANAGE)) {
                    iPlayer.messageNoPermission(IslandPermission.MEMBER_MANAGE)
                    return@of
                }

                when (it.click) {
                    ClickType.LEFT -> {
                        if (!iPlayer.hasIslandPermission(IslandPermission.MEMBER_KICK)) {
                            iPlayer.messageNoPermission(IslandPermission.MEMBER_KICK)
                            return@of
                        }

                        if (islandMember.islandRank!!.weight >= iPlayer.islandRank!!.weight) {
                            iPlayer.message(Message.instance.cantKickHigher.replace("{target}", islandMember.name))
                            return@of
                        }

                        island.kickMember(islandMember.name)
                        iPlayer.message(String.format(Message.instance.commandMemberKicked, islandMember.name))
                        buildMenu(this).open(player)
                    }
                    ClickType.RIGHT -> {
                        if (!iPlayer.isLeader()) {
                            iPlayer.message(Message.instance.commandRequirementsNotAnIslandLeader)
                            return@of
                        }
                        island.promoteNewLeader(islandMember.name)
                        Bukkit.getPlayer(islandMember.name)
                            ?.sendMessage(color(Message.instance.commandMemberPromoteYouHaveBeenPromoted))
                        buildMenu(this).open(player)

                    }
                    else -> return@of
                }

            }
        }
    }



}