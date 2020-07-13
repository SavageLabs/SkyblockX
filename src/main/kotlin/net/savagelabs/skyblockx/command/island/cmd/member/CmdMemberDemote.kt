package net.savagelabs.skyblockx.command.island.cmd.member

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.*
import net.savagelabs.skyblockx.command.argument.MemberArgument
import net.savagelabs.skyblockx.command.island.IslandBaseCommand
import net.savagelabs.skyblockx.core.IslandPermission
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.getIPlayerByName
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.Bukkit

class CmdMemberDemote : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("demote")

        requiredArgs.add(Argument("island-member", 0, MemberArgument()))
        commandRequirements =
                SCommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).withIslandPermission(IslandPermission.MEMBER_DEMOTE).build()
    }

    override fun perform(info: SCommandInfo) {
        val island = info.island!!
        if (island.getIslandMembers().isEmpty()) {
            info.message(Message.instance.commandMemberNoMembers)
            return
        }


        val playerNameToDemote = info.args[0]
        if (playerNameToDemote == info.player!!.name) {
            info.message(Message.instance.genericCannotReferenceYourSelf)
            return
        }

        if (!info.island!!.getIslandMembers().contains(getIPlayerByName(playerNameToDemote))) {
            info.message(Message.instance.commandMemberPromoteNotFound)
            return
        }

        val toDemote = getIPlayerByName(playerNameToDemote)!!

        val promoterRank = info.iPlayer!!.islandRank!!
        val toRank = toDemote.islandRank!!.previous()

        if (toRank == null) {
            info.message(Message.instance.cantDemoteLower.replace("{target}", playerNameToDemote))
            return
        }

        if (toRank.weight >= promoterRank.weight) {
            info.message(Message.instance.cantDemoteHigher.replace("{target}", playerNameToDemote))
        }

        toDemote.islandRank = toRank
        info.island!!.messageAllOnlineIslandMembers(Message.instance.memberDemoteMessage
                .replace("{demoter}", info.player!!.name)
                .replace("{demoted}", playerNameToDemote)
                .replace("{rank}", toRank.data.identifier))
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandMemberDemoteHelp
    }
}

class CmdDemote : Command<SCommandInfo, SCommandRequirements>() {
    init {
        aliases.add("demote")

        requiredArgs.add(Argument("island-member", 0, MemberArgument()))
        commandRequirements =
                SCommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).withIslandPermission(IslandPermission.MEMBER_DEMOTE).asLeader(true).build()
    }

    override fun perform(info: SCommandInfo) {
        // Execute command go just to make a shorthand version for /is member kick <member>.
        IslandBaseCommand.instance.subCommands.find { command -> command is CmdMember }
                ?.subCommands?.find { command -> command is CmdMemberDemote }?.perform(info)
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandMemberKickHelp
    }

}