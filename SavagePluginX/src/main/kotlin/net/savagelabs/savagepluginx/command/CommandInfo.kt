package net.savagelabs.savagepluginx.command

import net.savagelabs.savagepluginx.persist.BaseConfig
import net.savagelabs.savagepluginx.strings.color
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

open class CommandInfo(val commandSender: CommandSender, val args: ArrayList<String>, val aliasUsed: String) {


    var player: Player? = if (commandSender is Player) commandSender else null

    fun getArgAsPlayer(index: Int, informIfNot: Boolean = true): Player? {
        return Bukkit.getPlayer(args[index]) ?: run {
            if (informIfNot) message(BaseConfig.instance.commandParsingPlayerDoesNotExist)
            null
        }
    }

    fun getArgAsInt(index: Int, informIfNot: Boolean = true): Int? {
        return args.getOrNull(index)?.toIntOrNull() ?: run {
            if (informIfNot) {
                message(BaseConfig.instance.commandParsingArgIsNotInt)
            }
            null
        }
    }

    fun getArgAsLong(index: Int, informIfNot: Boolean = true): Long? {
        return args.getOrNull(index)?.toLongOrNull() ?: run {
            if (informIfNot) {
                message(BaseConfig.instance.commandParsingArgIsNotInt)
            }
            null
        }
    }

    fun getArgAsDouble(index: Int, informIfNot: Boolean = true): Double? {
        try {
            return args.getOrNull(index)?.toDouble() ?: kotlin.run {
                if (informIfNot) {
                    message(BaseConfig.instance.commandParsingArgIsNotDouble)
                }
                return null
            }
        } catch (e: Exception) {
            if (informIfNot) {
                message(BaseConfig.instance.commandParsingArgIsNotDouble)
            }
            return null
        }

    }

    fun getArgAsBoolean(index: Int, informIfNot: Boolean = true): Boolean? {
        val arg = args.getOrNull(index)?.toLowerCase() ?: run {
            if (informIfNot) message(BaseConfig.instance.commandParsingArgIsNotBoolean)
            return null
        }
        if (arg == "true" || arg == "on" || arg == "1") return true
        if (arg == "false" || arg == "off" || arg == "0") return false
        if (informIfNot) message(BaseConfig.instance.commandParsingArgIsNotBoolean)
        return null
    }

    fun isPlayer(): Boolean {
        return player != null
    }

    fun message(message: String, prefix: Boolean) {
        if (message.isEmpty()) return
        commandSender.sendMessage(color(
            if (prefix) BaseConfig.instance.commandEnginePrefix else "" + message))
    }


    fun message(message: String, vararg args: String) {
        if (message.isEmpty()) return
        message(String.format(message, *args))
    }
}