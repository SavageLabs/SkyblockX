package net.savagelabs.skyblockx.command.island.cmd.member

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.*
import net.savagelabs.skyblockx.command.argument.MemberArgument
import net.savagelabs.skyblockx.command.island.IslandBaseCommand
import net.savagelabs.skyblockx.core.*
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.Bukkit

class CmdMemberPromote : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("promote")

        requiredArgs.add(Argument("island-member", 0, MemberArgument()))
        commandRequirements =
                SCommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).withIslandPermission(IslandPermission.MEMBER_PROMOTE).build()
    }

    override fun perform(info: SCommandInfo) {
        val island = info.island!!
        if (island.getIslandMembers().isEmpty()) {
            info.message(Message.instance.commandMemberNoMembers)
            return
        }


        val playerNameToPromote = info.args[0]
        if (playerNameToPromote == info.player!!.name) {
            info.message(Message.instance.genericCannotReferenceYourSelf)
            return
        }

        if (!info.island!!.getIslandMembers().contains(getIPlayerByName(playerNameToPromote))) {
            info.message(Message.instance.commandMemberPromoteNotFound)
            return
        }

        val toPromote = getIPlayerByName(playerNameToPromote)!!

        val promoterRank = info.iPlayer!!.islandRank!!
        val toRank = toPromote.islandRank!!.next()
        if (toRank == null || toRank.weight > promoterRank.weight || toRank == Rank.OWNER) {
            info.message(Message.instance.cantPromoteHigher.replace("{target}", playerNameToPromote))
            return
        }

        toPromote.islandRank = toRank
        info.island!!.messageAllOnlineIslandMembers(Message.instance.memberPromoteMessage
                .replace("{promoter}", info.player!!.name)
                .replace("{promoted}", playerNameToPromote)
                .replace("{rank}", toRank.data.identifier))
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandMemberPromoteHelp
    }
}

class CmdPromote : Command<SCommandInfo, SCommandRequirements>() {
    init {
        aliases.add("promote")

        requiredArgs.add(Argument("island-member", 0, MemberArgument()))
        commandRequirements =
                SCommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).withIslandPermission(IslandPermission.MEMBER_PROMOTE).asLeader(true).build()
    }

    override fun perform(info: SCommandInfo) {
        // Execute command go just to make a shorthand version for /is member kick <member>.
        IslandBaseCommand.instance.subCommands.find { command -> command is CmdMember }
                ?.subCommands?.find { command -> command is CmdMemberPromote }?.perform(info)
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandMemberPromoteHelp
    }

}