package net.savagelabs.skyblockx.hooks

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.getIPlayer
import org.bukkit.entity.Player
import java.text.DecimalFormat
import java.util.stream.Collectors.joining
import kotlin.math.floor


class PlacholderAPIIntegration : PlaceholderExpansion() {

    val decimalFormat = DecimalFormat()

    override fun getIdentifier(): String {
        return "skyblockx"
    }

    override fun getPlugin(): String {
        return "SkyblockX"
    }

    override fun getAuthor(): String {
        return "ProSavage"
    }

    override fun getVersion(): String {
        return SkyblockX.skyblockX.description.version
    }

    override fun onPlaceholderRequest(player: Player, s: String): String? {
        val iPlayer = getIPlayer(player)
        return when (s) {
            "island_owner" -> iPlayer.getIsland()?.ownerTag ?: "N/A"
            "island_member_amount", "island_member_count" -> (iPlayer.getIsland()?.getIslandMembers(false)?.size?.plus(1)
                ?: 0).toString()
            "island_member_online_amount", "island_member_online_count" -> (iPlayer.getIsland()?.getIslandMembers(false)
                ?.filter { member -> member.isOnline() }?.size?.plus(
                    if (iPlayer.getIsland()?.getOwnerIPlayer()?.isOnline() == true) 1 else 0
                ) ?: 0).toString()
            "island_member_list" -> iPlayer.getIsland()?.getIslandMembers(false)?.stream()!!.map { member -> member.name }
                .collect(joining(", ")) ?: "N/A"
            "island_worth" -> SkyblockX.islandValues?.map?.get(iPlayer.getIsland()?.islandID)?.worth?.toString() ?: "0"
            "island_worth_formatted" -> decimalFormat.format(
                SkyblockX.islandValues?.map?.get(iPlayer.getIsland()?.islandID)?.worth ?: 0
            )
            "island_worth_rounded" -> floor(
                SkyblockX.islandValues?.map?.get(iPlayer.getIsland()?.islandID)?.worth ?: 0.0
            ).toString()
            "island_level" -> (iPlayer.getIsland()?.getLevel()?.toInt() ?: 0).toString()
            else -> null
        }
    }


}