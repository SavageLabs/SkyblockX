package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Argument
import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.savagepluginx.command.argument.PlayerArgument
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.teleportAsync
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.Bukkit

class CmdRemove : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("remove")
        aliases.add("expel")

        requiredArgs.add(Argument("player", 0, PlayerArgument()))

        commandRequirements =
            SCommandRequirementsBuilder().asIslandMember(true).asPlayer(true)
                .withPermission(Permission.REMOVE).build()
    }

    override fun perform(info: SCommandInfo) {
        val target = info.getArgAsIPlayer(0) ?: return
       info.iPlayer!!.attemptExpel(target)
    }


    override fun getHelpInfo(): String {
        return Message.instance.commandRemoveHelp
    }


}