package net.savagelabs.skyblockx.command.island.cmd

import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.*
import net.savagelabs.skyblockx.core.IslandPermission
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.persist.Message

class CmdName : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("name")
        aliases.add("tag")

        commandRequirements = SCommandRequirementsBuilder().withPermission(Permission.RENAME).withIslandPermission(IslandPermission.ISLAND_NAME).asIslandMember(true).build()
    }

    override fun perform(info: SCommandInfo) {
        info.message(Message.instance.commandNameSuccess, info.island!!.islandName)
    }

    override fun getHelpInfo(): String {
        return Message.instance.commandNameHelp
    }


}