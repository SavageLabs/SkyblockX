package net.savagellc.savageskyblock.command

import net.savagellc.savageskyblock.core.IPlayer
import net.savagellc.savageskyblock.core.getIPlayer
import net.savagellc.savageskyblock.persist.Message
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.NumberFormatException
import java.util.*

class CommandInfo(val commandSender: CommandSender, val args: ArrayList<String>, val aliasUsed: String) {


    var player: Player? = if (commandSender is Player) commandSender else null
    var iPlayer: IPlayer? = if (commandSender is Player) getIPlayer(commandSender) else null


    fun isBypassing(): Boolean {
        return iPlayer!!.inBypass
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
        commandSender.sendMessage(message)
    }




}