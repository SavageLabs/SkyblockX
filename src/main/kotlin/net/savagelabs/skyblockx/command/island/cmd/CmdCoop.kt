package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.PlayerArgument
import net.savagelabs.skyblockx.command.*
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

class CmdCoop : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("co-op")
        aliases.add("coop")

        requiredArgs.add(Argument("player", 0, PlayerArgument()))

        commandRequirements =
           SCommandRequirementsBuilder().asPlayer(true).asIslandMember(true)
                .withPermission(Permission.COOP).build()
    }


    override fun perform(info: SCommandInfo) {
        val target = info.getArgAsIPlayer(0) ?: return
        if (!info.iPlayer!!.getIsland()!!.canHaveMoreCoopPlayers()) {
            info.message(Message.instance.commandCoopCannotHaveMoreCoopPlayers)
            return
        }

        info.iPlayer!!.getIsland()!!.coopPlayer(info.iPlayer, target)

        target.message(String.format(Message.instance.commandCoopMessageRecipient, info.player!!.name))
        info.iPlayer!!.message(String.format(Message.instance.commandCoopInvokerSuccess, target.getPlayer().name))
    }


    override fun getHelpInfo(): String {
        return Message.instance.commandCoopHelp
    }


}