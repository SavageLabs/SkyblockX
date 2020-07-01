package net.savagelabs.savagepluginx.command

import net.savagelabs.savagepluginx.persist.BaseConfig
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

open class CommandRequirements<T: CommandInfo>(
    var asPlayer: Boolean,
    var rawPermission: String?
) {

    open fun checkRequirements(info: T, informIfNot: Boolean = true): Boolean {
        // Check if the commandSender is a player
        if (checkPlayerRequirement(info, informIfNot).not()) return false

        if (checkConsoleSenderRequirement(info)) return true

        if (checkRawPermissionRequirement(info, informIfNot).not()) return false

        return true
    }

    fun checkRawPermissionRequirement(info: T, informIfNot: Boolean = true): Boolean {
        if (rawPermission != null && !info.commandSender.hasPermission(rawPermission!!)) {
            if (informIfNot) {
                info.message(
                    String.format(
                        BaseConfig.instance.commandRequirementsPlayerDoesNotHavePermission,
                        rawPermission
                    )
                )
            }
            return false
        }
        return true
    }

    fun checkConsoleSenderRequirement(info: T): Boolean {
        // If the console sends it then its OP
        return info.commandSender is ConsoleCommandSender
    }

    fun checkPlayerRequirement(info: T, informIfNot: Boolean = true): Boolean {
        if (asPlayer && !info.isPlayer()) {
            if (informIfNot) {
                info.message(BaseConfig.instance.commandRequirementsNotAPlayer)
            }
            return false
        }
        return true
    }

}