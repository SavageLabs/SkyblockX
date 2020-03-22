package net.savagelabs.skyblockx.command.island.cmd


import net.savagelabs.skyblockx.command.CommandInfo
import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.gui.IslandQuestGUI
import net.savagelabs.skyblockx.persist.Message

class CmdQuest : SCommand() {

    init {
        aliases.add("quest")
        aliases.add("quests")

        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.QUEST).asIslandMember(true).build()
    }


    override fun perform(info: CommandInfo) {
        IslandQuestGUI().showGui(info.player!!)
    }


    override fun getHelpInfo(): String {
        return Message.commandQuestHelp
    }
}