package net.savagellc.savageskyblock

import net.prosavage.baseplugin.BasePlugin
import net.savagellc.savageskyblock.command.BaseCommand
import net.savagellc.savageskyblock.goal.Quest
import net.savagellc.savageskyblock.goal.QuestGoal
import net.savagellc.savageskyblock.listener.*
import net.savagellc.savageskyblock.persist.Config
import net.savagellc.savageskyblock.persist.Data
import net.savagellc.savageskyblock.persist.Message
import net.savagellc.savageskyblock.world.VoidWorldGenerator
import org.bukkit.WorldCreator
import java.util.stream.Collectors


class SavageSkyblock : BasePlugin() {

    override fun onEnable() {
        super.onEnable()
        Globals.savageSkyblock = this
        this.getCommand("savageskyblock")!!.setExecutor(BaseCommandTesting())
        this.getCommand("is")!!.setExecutor(BaseCommand())
        WorldCreator(Config.skyblockWorldName).generator(VoidWorldGenerator()).createWorld()
        Config.load()
        Data.load()
        this.sortQuests()
        Message.load()
        registerListeners(DataListener(), SEditListener(), BlockListener(), PlayerListener(), QuestListener())
        logger.info("Loaded ${Data.IPlayers.size} players")
        logger.info("Loaded ${Data.islands.size} islands")
    }

    override fun onDisable() {
        super.onDisable()
        Config.save()
        Data.save()
        Message.save()
    }

    fun sortQuests() {
        Globals.blockQuests = Config.islandQuests.stream()
            .filter { quest: Quest? -> quest != null && quest.questGoal == QuestGoal.MINE_BLOCKS }
            .collect(Collectors.toList())
       Globals.mobKillingQuests = Config.islandQuests.stream().filter { quest: Quest? -> quest != null && quest.questGoal == QuestGoal.KILL_MOBS }
            .collect(Collectors.toList())


        Globals.craftQuests = Config.islandQuests.stream()
            .filter { quest: Quest? -> quest != null && quest.questGoal == QuestGoal.CRAFT }
            .collect(Collectors.toList())

    }


}