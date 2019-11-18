package io.illyria.skyblockx.command.cmd

import io.illyria.skyblockx.command.CommandInfo
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.persist.Message

class CmdGo : io.illyria.skyblockx.command.SCommand() {


    init {
        aliases.add("go")


        commandRequirements =
            io.illyria.skyblockx.command.CommandRequirementsBuilder().asIslandMember(true).asPlayer(true)
                .withPermission(Permission.GO).build()
    }


    override fun perform(info: CommandInfo) {
        val island = info.iPlayer!!.getIsland()!!
        info.message(Message.commandGoTeleporting)
        info.player!!.teleport(island.getIslandCenter())
    }


    override fun getHelpInfo(): String {
        return Message.commandGoHelp
    }

}
