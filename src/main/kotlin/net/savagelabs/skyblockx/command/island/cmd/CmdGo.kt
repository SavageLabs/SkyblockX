package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.core.teleportAsync
import net.savagelabs.skyblockx.persist.Message

class CmdGo : Command<SCommandInfo, SCommandRequirements>() {


    init {
        aliases.add("go")


        commandRequirements =
            SCommandRequirementsBuilder().asIslandMember(true).asPlayer(true)
                .withPermission(Permission.GO).build()
    }


    override fun perform(info: SCommandInfo) {
        val island = info.iPlayer!!.getIsland()!!
        teleportAsync(
            info.player!!,
            island.islandGoPoint!!.getLocation(),
            Runnable { info.message(Message.instance.commandGoTeleporting) })
    }


    override fun getHelpInfo(): String {
        return Message.instance.commandGoHelp
    }

}
