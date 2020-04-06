package net.savagelabs.skyblockx.command.island.cmd.home

import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.teleportAsync
import net.savagelabs.skyblockx.persist.Message

class CmdHomeGo : SCommand() {

    init {
        aliases.add("go")

        requiredArgs.add(Argument("home-name", 0, HomeArgument()))

        commandRequirements = CommandRequirementsBuilder().asIslandMember(true).build()
    }

    override fun perform(info: CommandInfo) {
        val home = info.args[0]
        if (!info.island!!.hasHome(home)) {
            info.message(Message.commandHomeDoesNotExist)
            return
        }

        teleportAsync(
            info.player!!,
            info.island!!.getHome(home)!!.getLocation(),
            Runnable { info.message(String.format(Message.commandHomeGoSuccess, home)) })
    }

    override fun getHelpInfo(): String {
        return Message.commandHomeGoHelp
    }


}