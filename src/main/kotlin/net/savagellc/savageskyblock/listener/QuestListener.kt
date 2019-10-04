package net.savagellc.savageskyblock.listener

import net.prosavage.baseplugin.XMaterial
import net.savagellc.savageskyblock.core.getIPlayer
import net.savagellc.savageskyblock.goal.QuestGoal
import net.savagellc.savageskyblock.persist.Config
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent


class QuestListener : Listener {

    @EventHandler
    fun onMineBlockQuest(event: BlockBreakEvent) {

        val iplayer = getIPlayer(event.player)

        // Return for invalid calls.
        if (iplayer.getIsland() == null || iplayer.getIsland()!!.currentQuest == null) {
            return
        }
        // store the quest.
        val currentQuest = iplayer.getIsland()!!.currentQuest


        val targetQuest = Config.islandQuests.find { quest -> quest.type == QuestGoal.BREAK_BLOCKS && quest.name == currentQuest} ?: return

        val material = XMaterial.matchXMaterial(event.block.type)?.name ?: event.block.type.name

        if (material != targetQuest.goalParameter) return






    }


}