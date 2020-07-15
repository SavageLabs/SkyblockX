package net.savagelabs.skyblockx.command

import net.savagelabs.savagepluginx.command.CommandRequirements
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.hasPermission
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.entity.Player


class SCommandRequirements(
    val permission: Permission?,
    var asIslandMember: Boolean,
    var asLeader: Boolean,
    asPlayer: Boolean,
    rawPermission: String?
) : CommandRequirements<SCommandInfo>(
    asPlayer,
    rawPermission
) {

    override fun checkRequirements(info: SCommandInfo, informIfNot: Boolean): Boolean {

        if (checkPlayerRequirement(info, informIfNot).not()) return false

        if (checkConsoleSenderRequirement(info)) return true

        info.commandSender as Player
        if (checkRawPermissionRequirement(info, informIfNot).not()) return false

        if (checkPermissionRequirement(info, informIfNot).not()) return false

        if (checkIslandMemberRequirement(info, informIfNot).not()) return false

        if (checkIslandLeaderRequirement(info, informIfNot).not()) return false


        return true
    }

    private fun checkIslandLeaderRequirement(info: SCommandInfo, informIfNot: Boolean = true): Boolean {
        if (asLeader && info.iPlayer!!.getIsland()!!.getLeader() != info.iPlayer) {
            if (informIfNot) {
                info.message(Message.instance.commandRequirementsNotAnIslandLeader)
            }
            return false
        }
        return true
    }


    private fun checkIslandMemberRequirement(info: SCommandInfo, informIfNot: Boolean = true): Boolean {
        // Check if the player's got an island
        if (asIslandMember) {
            if (info.iPlayer == null || !info.iPlayer!!.hasIsland()) {
                if (informIfNot) {
                    info.message(Message.instance.commandRequirementsNotAnIslandMember)
                }

                return false
            }
        }
        return true
    }

    private fun checkPermissionRequirement(info: SCommandInfo, informIfNot: Boolean = true): Boolean {
        info.commandSender as Player
        if (permission != null && !hasPermission(info.commandSender, permission)) {
            if (informIfNot) {
                info.message(
                    String.format(
                        Message.instance.commandRequirementsPlayerDoesNotHavePermission,
                        permission.getFullPermissionNode()
                    )
                )
            }
            return false
        }
        return true
    }


}

