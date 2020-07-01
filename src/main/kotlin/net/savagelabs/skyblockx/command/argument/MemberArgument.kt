package net.savagelabs.skyblockx.command.argument

import net.savagelabs.savagepluginx.command.ArgumentType
import net.savagelabs.skyblockx.core.getIPlayer
import org.bukkit.entity.Player

class MemberArgument : ArgumentType {
    override fun getPossibleValues(player: Player): List<String> {
        val iPlayer = player.getIPlayer()
        return if (iPlayer.hasIsland()) iPlayer.getIsland()!!.getIslandMembers().map { member -> member.name } else emptyList()
    }

}