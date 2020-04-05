package net.savagelabs.skyblockx.hooks

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import net.savagelabs.skyblockx.Globals
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
        return Globals.skyblockX.server.version
    }

    override fun onPlaceholderRequest(player: Player, s: String): String? {
        val iPlayer = getIPlayer(player)
        return when (s) {
            "island_owner" -> iPlayer.getIsland()?.ownerTag ?: "N/A"
            "island_member_amount", "island_member_count" -> (iPlayer.getIsland()?.getIslandMembers()?.size?.plus(1)
                ?: 0).toString()
            "island_member_online_amount", "island_member_online_count" -> (iPlayer.getIsland()?.getIslandMembers()
                ?.filter { member -> member.isOnline() }?.size?.plus(
                if (iPlayer.getIsland()?.getOwnerIPlayer()?.isOnline() == true) 1 else 0
            ) ?: 0).toString()
            "island_member_list" -> iPlayer.getIsland()?.getIslandMembers()?.stream()!!.map { member -> member.name }
                .collect(joining(", ")) ?: "N/A"
            "island_worth" -> Globals.islandValues?.map?.get(iPlayer.getIsland()?.islandID)?.worth?.toString() ?: "0"
            "island_worth_formatted" -> decimalFormat.format(
                Globals.islandValues?.map?.get(iPlayer.getIsland()?.islandID)?.worth ?: 0
            )
            "island_worth_rounded" -> floor(
                Globals.islandValues?.map?.get(iPlayer.getIsland()?.islandID)?.worth ?: 0.0
            ).toString()
            else -> null
        }
    }


}