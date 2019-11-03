package io.illyria.skyblockx.command

import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.hasPermission
import io.illyria.skyblockx.persist.Message
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class CommandRequirements(val permission: Permission?, var asPlayer: Boolean, var asIslandMember: Boolean, var asLeader: Boolean) {


    fun computeRequirements(info: io.illyria.skyblockx.command.CommandInfo, informIfNot: Boolean = true): Boolean {
        // Check if the commandSender is a player
        if (asPlayer && !info.isPlayer()) {
            if (informIfNot) {
                info.message(Message.commandRequirementsNotAPlayer)
            }
            return false
        }



        // If the console sends it then its OP
        if (info.commandSender is ConsoleCommandSender) {
            return true
        }

        // Assume executor is player since we checked if they're ConsoleCommandSender above.
        info.commandSender as Player
        if (permission != null && !hasPermission(info.commandSender, permission)) {
            if (informIfNot) {
                info.message(
                    String.format(
                        Message.commandRequirementsPlayerDoesNotHavePermission,
                        permission.getFullPermissionNode()
                    )
                )
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

        if (asLeader && info.iPlayer!!.getIsland()!!.getOwnerIPlayer() != info.iPlayer) {
            if (informIfNot) {
                info.message(Message.commandRequirementsNotAnIslandLeader)
            }
            return false
        }

        // Congrats the command is valid.
        return true


    }
}



