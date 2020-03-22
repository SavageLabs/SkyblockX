package net.savagelabs.skyblockx.command.island.cmd.member

import net.savagelabs.skyblockx.Globals
import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.core.color
import io.illyria.skyblockx.persist.Message
import me.rayzr522.jsonmessage.JSONMessage

class CmdMemberInvite : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {


    init {
        aliases.add("invite")

        requiredArgs.add(Argument("player", 0, PlayerArgument()))

        commandRequirements =
            _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).build()
    }


    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
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

class CmdInvite : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {
    init {
        aliases.add("invite")
        requiredArgs.add(Argument("player", 0, PlayerArgument()))
        commandRequirements =
            _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.MEMBER).asIslandMember(true).build()
    }

    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        _root_ide_package_.net.savagelabs.skyblockx.Globals.islandBaseCommand.subCommands.find { command -> command is _root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.member.CmdMember }
            ?.subCommands?.find { subcommand -> subcommand is _root_ide_package_.net.savagelabs.skyblockx.command.island.cmd.member.CmdMemberInvite }?.perform(info)
    }

    override fun getHelpInfo(): String {
        return Message.commandMemberInviteHelp
    }
}

