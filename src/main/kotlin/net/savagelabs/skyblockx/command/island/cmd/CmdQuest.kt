package net.savagelabs.skyblockx.command.island.cmd


import net.savagelabs.skyblockx.command.CommandRequirementsBuilder
import net.savagelabs.skyblockx.command.SCommand
import io.illyria.skyblockx.core.Permission
import io.illyria.skyblockx.gui.IslandQuestGUI
import io.illyria.skyblockx.persist.Message

class CmdQuest : _root_ide_package_.net.savagelabs.skyblockx.command.SCommand() {

    init {
        aliases.add("quest")
        aliases.add("quests")

        commandRequirements = _root_ide_package_.net.savagelabs.skyblockx.command.CommandRequirementsBuilder().withPermission(Permission.QUEST).asIslandMember(true).build()
    }


    override fun perform(info: _root_ide_package_.net.savagelabs.skyblockx.command.CommandInfo) {
        IslandQuestGUI().showGui(info.player!!)
    }


    override fun getHelpInfo(): String {
        return Message.commandQuestHelp
    }
}