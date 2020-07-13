package net.savagelabs.skyblockx.command

import net.savagelabs.savagepluginx.command.CommandInfo
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.Rank
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.persist.Data
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*


class SCommandInfo(commandSender: CommandSender, args: ArrayList<String>, aliasUsed: String) : CommandInfo(
    commandSender, args,
    aliasUsed
) {
    var iPlayer: IPlayer? = if (commandSender is Player) getIPlayer(commandSender) else null
    var island: Island? = if (commandSender is Player && iPlayer != null) iPlayer!!.getIsland() else null

    fun isBypassing(): Boolean {
        return iPlayer!!.inBypass
    }

    fun getArgAsIPlayer(index: Int, searchOffline: Boolean = false, cannotReferenceYourSelf: Boolean = true, informIfNot: Boolean = true): IPlayer? {
        if (searchOffline) {
            for (value in Data.instance.IPlayers.values) {
                if (value.name.equals(args[index], false)) {
                    if (cannotReferenceYourSelf && value == iPlayer) {
                        if (informIfNot) {
                            message(Message.instance.commandParsingPlayerIsYou)
                        }
                        return null
                    }
                    return value
                }
            }
        }
        val player = Bukkit.getPlayer(args[index])
        if (player == null) {
            if (informIfNot) {
                message(Message.instance.commandParsingPlayerDoesNotExist)
            }
            return null
        }
        if (cannotReferenceYourSelf && player == this.player) {
            if (informIfNot) {
                message(Message.instance.commandParsingPlayerIsYou)
            }
            return null
        }
        return getIPlayer(player)
    }

    fun getArgAsRank(index: Int, informIfNot: Boolean = true): Rank? {
        return Rank.values().find { rank -> args[index] == rank.data.identifier.toLowerCase() }
    }

    private fun getArgAsIPlayerUnsafe(index: Int): IPlayer? {
        val arg = args[index].toLowerCase()
        return Data.instance.IPlayers.values.find { player -> player.name.toLowerCase() == arg }
    }

    fun getArgAsIsland(index: Int, informIfNot: Boolean = true): Island? {
        val arg = args[index].toLowerCase()
        val player = getArgAsIPlayerUnsafe(index)
        if (player == null) {
            val potentialIsland = Data.instance.islands.values.find { island -> island.islandName.toLowerCase() == arg }
            if (potentialIsland != null) {
                return potentialIsland
            }

            if (informIfNot) {
                this.message(Message.instance.commandParsingArgIsNotIsland)
            }
            return null
        }

        if (!player.hasIsland() || player.getIsland() == null) {
            if (informIfNot) {
                this.message(Message.instance.commandParsingArgIsNotIsland)
            }
            return null
        }

        return player.getIsland()
    }

}