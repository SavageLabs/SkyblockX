package net.savagellc.savageskyblock.command.cmd


import net.savagellc.savageskyblock.command.CommandInfo
import net.savagellc.savageskyblock.command.CommandRequirementsBuilder
import net.savagellc.savageskyblock.command.SCommand
import net.savagellc.savageskyblock.gui.IslandCreateGUI
import net.savagellc.savageskyblock.gui.IslandQuestGUI
import net.savagellc.savageskyblock.persist.Message

class CmdQuest : SCommand() {

    init {
        aliases.add("quest")
        aliases.add("quests")

        commandRequirements = CommandRequirementsBuilder().asIslandMember(true).build()
    }


    override fun perform(info: CommandInfo) {
        val islandQuestGUI = IslandQuestGUI()
        islandQuestGUI.makeGUI(info.iPlayer!!)
        islandQuestGUI.showGui(info.player!!)

    }


    override fun getHelpInfo(): String {
       return Message.commandQuestHelp
    }
}