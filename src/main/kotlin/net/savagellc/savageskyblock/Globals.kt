package net.savagellc.savageskyblock

import net.savagellc.savageskyblock.command.BaseCommand
import net.savagellc.savageskyblock.goal.Quest

object Globals {
    lateinit var savageSkyblock: SavageSkyblock
    lateinit var baseCommand: BaseCommand
    lateinit var blockQuests: List<Quest>
    lateinit var mobKillingQuests: List<Quest>
    lateinit var craftQuests: List<Quest>
}