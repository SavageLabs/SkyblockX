package net.savagelabs.skyblockx.command

import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.persist.Message
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

    fun getArgAsIPlayer(index: Int, cannotReferenceYourSelf: Boolean = true, informIfNot: Boolean = true): IPlayer? {
        val player = Bukkit.getPlayer(args[index])
        if (player == null) {
            if (informIfNot) {
                message(Message.commandParsingPlayerDoesNotExist)
            }
            return null
        }
        if (cannotReferenceYourSelf && player == this.player) {
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

    fun getArgAsBoolean(index: Int, informIfNot: Boolean = true): Boolean? {
        val arg = args[index].toLowerCase()
        if (arg.equals("true") || arg.equals("1")) return true
        if (arg.equals("false") || arg.equals("0")) return false
        if (informIfNot) message(Message.commandParsingArgIsNotBoolean)
        return null
    }

    fun isPlayer(): Boolean {
        return player != null
    }

    fun message(message: String, withPrefix: Boolean = true) {
        commandSender.sendMessage(color((if (withPrefix) Message.messagePrefix else "") + message))
    }

    fun message(message: String, vararg args: String, withPrefix: Boolean = true) {
        message(String.format(message, *args), withPrefix)
    }


}