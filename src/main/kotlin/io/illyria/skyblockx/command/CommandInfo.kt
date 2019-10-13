package io.illyria.skyblockx.command

import io.illyria.skyblockx.core.IPlayer
import io.illyria.skyblockx.core.Island
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.core.getIPlayer
import io.illyria.skyblockx.persist.Message
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class CommandInfo(val commandSender: CommandSender, val args: ArrayList<String>, val aliasUsed: String) {


    var player: Player? = if (commandSender is Player) commandSender else null
    var iPlayer: IPlayer? = if (commandSender is Player) getIPlayer(commandSender) else null
    var island: Island? = if (commandSender is Player && iPlayer != null) iPlayer!!.getIsland() else null


    fun isBypassing(): Boolean {
        return iPlayer!!.inBypass
    }

    fun getArgAsPlayer(index: Int, informIfNot: Boolean = true): Player? {
        val player = Bukkit.getPlayer(args[index])
        if (player == null) {
            if (informIfNot) {
                message(Message.commandParsingPlayerDoesNotExist)
            }
            return null
        }
        return player
    }

    fun getArgAsIPlayer(index: Int, informIfNot: Boolean = true): IPlayer? {
        val player = Bukkit.getPlayer(args[index])
        if (player == null) {
            if (informIfNot) {
                message(Message.commandParsingPlayerDoesNotExist)
            }
            return null
        }
        if (player == this.player) {
            if (informIfNot) {
                message(Message.commandParsingPlayerIsYou)
            }
            return null
        }
        return getIPlayer(player)
    }


    fun getArgAsInt(index: Int, informIfNot: Boolean = true): Int? {
        try {
            return args[index].toInt()
        } catch (exception: NumberFormatException) {
            if (informIfNot) {
                message(Message.commandParsingArgIsNotInt)
            }
        }
        return null
    }

    fun isPlayer(): Boolean {
        return player != null
    }

    fun message(message: String) {
        commandSender.sendMessage(color(message))
    }


}