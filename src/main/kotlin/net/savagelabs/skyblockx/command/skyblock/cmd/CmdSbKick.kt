package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.getIPlayerByName
import io.illyria.skyblockx.core.getIPlayerByUUID
import io.illyria.skyblockx.persist.Message

class CmdSbKick : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("kick")

        requiredArgs.add(Argument("player-to-kick", 0, PlayerArgument()))

        commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.ADMIN_KICKFROMISLAND).build()
    }


    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        val iPlayerByName = getIPlayerByName(info.args[0])
        if (iPlayerByName?.getIsland() == null) {
            info.message(Message.genericPlayerNotAnIslandMember)
            return
        }

        // They're not the owner so we process removing the member.
        val island = iPlayerByName.getIsland()!!
        if (island.getOwnerIPlayer() != iPlayerByName) {
            if (!island.getIslandMembers().contains(iPlayerByName)) {
                info.message(Message.commandMemberKickNotFound)
                return
            }

            island.kickMember(iPlayerByName.name)
        } else {
            // Theyre an island owner if we're here.
            iPlayerByName.unassignIsland()
            if (island.getAllMemberUUIDs().isEmpty()) {
                island.delete()
                info.message(Message.commandSkyblockKickIslandDeleted)
                return
            }
            val firstMember = island.getAllMemberUUIDs().toList()[0]

            // just in case
            println(firstMember)
            val iPlayerByUUID = getIPlayerByUUID(firstMember)
            iPlayerByUUID!!.assignIsland(island)
            island.ownerTag = iPlayerByUUID.name
            island.ownerUUID = iPlayerByUUID.uuid
            info.message(String.format(Message.commandSkyblockKickMemberKickedOwner, iPlayerByUUID.name))
        }
        info.message(String.format(Message.commandSkyblockKickMemberKicked, iPlayerByName.name))




    }

    override fun getHelpInfo(): String {
        return Message.commandSkyblockKickHelp
    }
}