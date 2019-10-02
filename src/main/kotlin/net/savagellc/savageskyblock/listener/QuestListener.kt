package net.savagellc.savageskyblock.listener

import net.prosavage.baseplugin.XMaterial
import net.savagellc.savageskyblock.core.getIPlayer
import net.savagellc.savageskyblock.persist.Config
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent


class QuestListener : Listener {

    @EventHandler
    fun onMineBlockQuest(event: BlockBreakEvent) {

        val iplayer = getIPlayer(event.player)

        // Return for invalid calls.
        if (!iplayer.hasIsland() || iplayer.getIsland()!!.currentQuest == null) {
            return
        }

        // store the quest.
        val currentQuest = iplayer.getIsland()!!.currentQuest

        val find = Config.islandQuests.find { quest -> quest.name == currentQuest } ?: return



        val xMaterial = XMaterial.matchXMaterial(event.block.type)


    }


}