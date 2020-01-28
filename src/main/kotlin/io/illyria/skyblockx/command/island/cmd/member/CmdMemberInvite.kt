package io.illyria.skyblockx.command.island.cmd.member

import io.illyria.skyblockx.Globals
import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.persist.Message
import me.rayzr522.jsonmessage.JSONMessage

class CmdMemberInvite : SCommand() {


    init {
        aliases.add("invite")

        requiredArgs.add(Argument("player", 0, PlayerArgument()))

        commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).build()
    }


    override fun perform(info: CommandInfo) {
        val island = info.island!!
        if (island.memberLimit <= island.getIslandMembers().size) {
            info.message(String.format(Message.commandMemberInviteLimit, island.memberLimit))
            return
        }
        val playerToInvite = info.getArgAsPlayer(0) ?: return
        if (playerToInvite == info.player) {
            info.message(Message.genericCannotReferenceYourSelf)
            return
        }
        if (island.members.contains(playerToInvite.name)) {
            info.message(Message.commandMemberAlreadyPartOfIsland)
            return
        }
        island.inviteMember(info.getArgAsIPlayer(0)!!)
        info.message(String.format(Message.commandMemberInviteSuccess, playerToInvite.name))
        JSONMessage.create(color(String.format(Message.commandMemberInviteMessage, info.player?.name)))
            .tooltip(color("&7Click to paste &f\"/is join ${info.player!!.name}\""))
            .runCommand("/is join ${info.player!!.name}")
            .send(info.getArgAsPlayer(0)!!)
    }

    override fun getHelpInfo(): String {
        return Message.commandMemberInviteHelp
    }

}

class CmdInvite : SCommand() {
    init {
        aliases.add("invite")
        requiredArgs.add(Argument("player", 0, PlayerArgument()))
        commandRequirements =
            CommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).build()
    }

    override fun perform(info: CommandInfo) {
        Globals.islandBaseCommand.subCommands.find { command -> command is CmdMember }
            ?.subCommands?.find { subcommand -> subcommand is CmdMemberInvite }?.perform(info)
    }

    override fun getHelpInfo(): String {
        return Message.commandMemberInviteHelp
    }
}

