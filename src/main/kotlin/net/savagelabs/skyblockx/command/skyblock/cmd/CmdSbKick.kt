package net.savagelabs.skyblockx.command.skyblock.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.PlayerArgument
import net.savagelabs.skyblockx.command.*
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.getIPlayerByName
import net.savagelabs.skyblockx.core.getIPlayerByUUID
import net.savagelabs.skyblockx.persist.Message

class CmdSbKick : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("kick")

        requiredArgs.add(Argument("player-to-kick", 0, PlayerArgument()))

        commandRequirements = SCommandRequirementsBuilder().withPermission(Permission.ADMIN_KICKFROMISLAND).build()
    }


    override fun perform(info: SCommandInfo) {
        val iPlayerByName = getIPlayerByName(info.args[0])
        if (iPlayerByName?.getIsland() == null) {
            info.message(Message.instance.genericPlayerNotAnIslandMember)
            return
        }

        // They're not the owner so we process removing the member.
        val island = iPlayerByName.getIsland()!!
        if (island.getOwnerIPlayer() != iPlayerByName) {
            if (!island.getIslandMembers().contains(iPlayerByName)) {
                info.message(Message.instance.commandMemberKickNotFound)
                return
            }

            island.kickMember(iPlayerByName.name)
        } else {
            // Theyre an island owner if we're here.
            iPlayerByName.unassignIsland()
            if (island.getAllMemberUUIDs().isEmpty()) {
                island.delete()
                info.message(Message.instance.commandSkyblockKickIslandDeleted)
                return
            }
            val firstMember = island.getAllMemberUUIDs().toList()[0]

            // just in case
            println(firstMember)
            val iPlayerByUUID = getIPlayerByUUID(firstMember)
            iPlayerByUUID!!.assignIsland(island)
            island.ownerTag = iPlayerByUUID.name
            island.ownerUUID = iPlayerByUUID.uuid
            info.message(String.format(Message.instance.commandSkyblockKickMemberKickedOwner, iPlayerByUUID.name))
        }
        info.message(String.format(Message.instance.commandSkyblockKickMemberKicked, iPlayerByName.name))




    }

    override fun getHelpInfo(): String {
        return Message.instance.commandSkyblockKickHelp
    }
}