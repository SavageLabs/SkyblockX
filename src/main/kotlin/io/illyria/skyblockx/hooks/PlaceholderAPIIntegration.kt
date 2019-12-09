package io.illyria.skyblockx.hooks

import io.illyria.skyblockx.Globals
import io.illyria.skyblockx.core.getIPlayer
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import java.text.DecimalFormat
import java.util.stream.Collectors.joining


class PlacholderAPI : PlaceholderExpansion() {

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
            "island_member_amount", "island_member_count" -> iPlayer.getIsland()?.getAllMembers()?.size.toString() ?: "0"
            "island_member_list" -> iPlayer.getIsland()?.getAllMembers()?.stream()!!.collect(joining(", ")) ?: "N/A"
            "island_worth" -> Globals.islandValues?.map?.get(iPlayer.getIsland()?.islandID)?.worth.toString() ?: "0"
            "island_worth_formatted" -> decimalFormat.format(Globals.islandValues?.map?.get(iPlayer.getIsland()?.islandID)?.worth ?: 0)
            else -> null
        }
    }


}