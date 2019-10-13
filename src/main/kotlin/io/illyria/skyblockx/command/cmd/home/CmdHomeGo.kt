package io.illyria.skyblockx.command.cmd.home

import io.illyria.skyblockx.persist.Message

class CmdHomeGo : io.illyria.skyblockx.command.SCommand() {

    init {
        aliases.add("go")

        requiredArgs.add(Argument("home-name", 0, HomeArgument()))

        commandRequirements = io.illyria.skyblockx.command.CommandRequirementsBuilder().asIslandMember(true).build()
    }

    override fun perform(info: io.illyria.skyblockx.command.CommandInfo) {
        val home = info.args[0]
        if (!info.island!!.hasHome(home)) {
            info.message(Message.commandHomeDoesNotExist)
            return
        }

        info.player!!.teleport(info.island!!.getHome(home)!!.getLocation())
        info.message(String.format(Message.commandHomeGoSuccess, home))
    }

    override fun getHelpInfo(): String {
        return Message.commandHomeGoHelp
    }


}