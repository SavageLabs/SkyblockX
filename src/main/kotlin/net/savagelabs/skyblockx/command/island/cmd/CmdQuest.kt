package net.savagelabs.skyblockx.command.island.cmd


import net.savagelabs.savagepluginx.command.Command
import net.savagelabs.skyblockx.command.SCommandInfo
import net.savagelabs.skyblockx.command.SCommandRequirements
import net.savagelabs.skyblockx.command.SCommandRequirementsBuilder
import net.savagelabs.skyblockx.core.Permission
import net.savagelabs.skyblockx.gui.IslandQuestGUI
import net.savagelabs.skyblockx.persist.Message

class CmdQuest : Command<SCommandInfo, SCommandRequirements>() {

    init {
        aliases.add("quest")
        aliases.add("quests")

        commandRequirements = SCommandRequirementsBuilder()
            .withPermission(Permission.QUEST)
            .asIslandMember(true)
            .build()
    }


    override fun perform(info: SCommandInfo) {
        IslandQuestGUI().showGui(info.player!!)
    }


    override fun getHelpInfo(): String {
        return Message.instance.commandQuestHelp
    }
}