package net.savagellc.savageskyblock.command

import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.core.hasPermission
import net.savagellc.savageskyblock.persist.Message
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class CommandRequirements(val permission: Permission?, var asPlayer: Boolean, var asIslandMember: Boolean) {


    fun computeRequirements(info: CommandInfo, informIfNot: Boolean = true): Boolean {
        // Check if the commandSender is a player
        if (asPlayer && !info.isPlayer()) {
            if (informIfNot) {
                info.message(Message.commandRequirementsNotAPlayer)
            }
            return false
        }

        // If the permission is null then anyone can execute
        if (permission == null) {
            return true
        }

        // If the console sends it then its OP
        if (info.commandSender is ConsoleCommandSender) {
            return true
        }

        // Assume executor is player since we checked if they're ConsoleCommandSender above.
        info.commandSender as Player
        if (!hasPermission(info.commandSender, permission)) {
            if (informIfNot) {
                info.message(String.format(Message.commandRequirementsPlayerDoesNotHavePermission))
            }
            return false
        }

        // Check if the player's got an island

        if (asIslandMember) {
            if (info.iPlayer == null || !info.iPlayer!!.hasIsland()) {
                if (informIfNot) {
                    info.message(Message.commandRequirementsNotAnIslandMember)
                }
                return false
            }
        }

        // Congrats the command is valid.
        return true


    }
}



