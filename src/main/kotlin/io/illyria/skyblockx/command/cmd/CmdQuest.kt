package io.illyria.skyblockx.command.cmd


import io.illyria.skyblockx.command.CommandRequirementsBuilder
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.gui.IslandQuestGUI
import io.illyria.skyblockx.persist.Message

class CmdQuest : io.illyria.skyblockx.command.SCommand() {

    init {
        aliases.add("quest")
        aliases.add("quests")

        commandRequirements = CommandRequirementsBuilder().withPermission(Permission.QUEST).asIslandMember(true).build()
    }


    override fun perform(info: io.illyria.skyblockx.command.CommandInfo) {
        IslandQuestGUI().showGui(info.player!!)
    }


    override fun getHelpInfo(): String {
        return Message.commandQuestHelp
    }
}