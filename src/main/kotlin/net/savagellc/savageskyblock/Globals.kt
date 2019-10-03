package net.savagellc.savageskyblock

import net.savagellc.savageskyblock.command.BaseCommand
import net.savagellc.savageskyblock.goal.Quest
import net.savagellc.savageskyblock.goal.QuestGoal

object Globals {
    lateinit var savageSkyblock: SavageSkyblock
    lateinit var baseCommand: BaseCommand
    lateinit var quests: MutableMap<QuestGoal, MutableList<Quest>>

}