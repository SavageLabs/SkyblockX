package net.savagelabs.skyblockx.command.argument

import net.savagelabs.savagepluginx.command.ArgumentType
import net.savagelabs.skyblockx.core.Rank
import org.bukkit.entity.Player

class RankArgument : ArgumentType {
    override fun getPossibleValues(player: Player): List<String> {
        return Rank.values().map { rank -> rank.data.identifier.toLowerCase() }
    }
}