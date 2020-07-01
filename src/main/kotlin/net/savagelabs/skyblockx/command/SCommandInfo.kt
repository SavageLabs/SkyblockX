package net.savagelabs.skyblockx.command

import net.savagelabs.savagepluginx.command.CommandInfo
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*


class SCommandInfo(commandSender: CommandSender, args: ArrayList<String>, aliasUsed: String) : CommandInfo(commandSender, args,
    aliasUsed
) {
    var iPlayer: IPlayer? = if (commandSender is Player) getIPlayer(commandSender) else null
    var island: Island? = if (commandSender is Player && iPlayer != null) iPlayer!!.getIsland() else null

    fun isBypassing(): Boolean {
        return iPlayer!!.inBypass
    }

    fun getArgAsIPlayer(index: Int, cannotReferenceYourSelf: Boolean = true, informIfNot: Boolean = true): IPlayer? {
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
}