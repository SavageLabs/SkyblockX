package io.illyria.skyblockx.listener

import io.illyria.skyblockx.core.*
import io.illyria.skyblockx.persist.Config
import io.illyria.skyblockx.persist.Message
import io.illyria.skyblockx.quest.QuestGoal
import net.prosavage.baseplugin.XMaterial
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.event.player.PlayerInteractEvent

class PlayerListener : Listener {


    @EventHandler
    fun onPlayerTakingDamage(event: EntityDamageByEntityEvent) {
        // If they're not a player or if the entity is not in the skyblock world, we do not care.
        if (event.entity !is Player || event.entity.location.world?.name != Config.skyblockWorldName) {
            return
        }
        val iPlayer = getIPlayer(event.entity as Player)
        if (!iPlayer.isOnOwnIsland()) {
            iPlayer.message(String.format(Message.listenerPlayerDamageCancelled))
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerDamage(event: EntityDamageEvent) {
        if (!Config.preventFallingDeaths
            || event.entity !is Player
            || event.entity.location.world?.name != Config.skyblockWorldName
        ) {
            return
        }

        val player = event.entity as Player
        val iPlayer = getIPlayer(player)

        // Triggers when they fall into the void.
        if (event.cause == EntityDamageEvent.DamageCause.VOID) {
            iPlayer.falling = true
            player.sendMessage(color(Message.listenerVoidDeathPrevented))
            if (iPlayer.hasIsland()) {
                player.teleport(iPlayer.getIsland()!!.getIslandSpawn())
            } else {
                player.teleport(Bukkit.getWorld("world")!!.spawnLocation)
            }
            event.isCancelled = true
        }

        // Triggers when they fall and the VOID damage registers falling to cancel.
        if (event.cause == EntityDamageEvent.DamageCause.FALL && iPlayer.falling) {
            iPlayer.falling = false
            event.isCancelled = true
        }

    }

    @EventHandler
    fun onPlayerCraft(event: CraftItemEvent) {
        // Exit if we aren't in skyblock world - since they cannot be on an island that way.
        if (event.whoClicked.location.world?.name != Config.skyblockWorldName) {
            return
        }

        val iplayer = getIPlayer(event.whoClicked as Player)
        // Fail the checks if we dont have an island, or dont have a active quest, or if we arent on our own island.
        val island = iplayer.getIsland()
        if (!iplayer.hasIsland() || island!!.currentQuest == null || !island.containsBlock(event.whoClicked.location)) {
            return
        }

        val currentQuest = island.currentQuest
        // Find the quest that the island has activated, if none found, return.
        val targetQuest =
            Config.islandQuests.find { quest -> quest.type == QuestGoal.CRAFT && quest.name == currentQuest }
                ?: return
        // Use XMaterial to parse the material, if null, try to use native material just in case.
        val materialCrafted = XMaterial.matchXMaterial(event.recipe.result)?.name ?: event.recipe.result.type

        // We had a quest active and it was a crafting quest, however, we didnt craft the goal item.
        if (materialCrafted.toString().toLowerCase() != targetQuest.goalParameter.toLowerCase()) {
            return
        }

        // Increment that quest data by 1 :)
        island.addQuestData(targetQuest.name, 1)
        // Check if quest is complete :D
        if (targetQuest.isComplete(island.getQuestCompletedAmount(targetQuest.name))) {
            island.completeQuest(iplayer, targetQuest)
        }
    }

    @EventHandler
    fun onPlayerFish(event: PlayerFishEvent) {
        // Event fires for all other times the rod is thrown, so we need to check the state of the event, along with the world it self right after.
        if (event.state != PlayerFishEvent.State.CAUGHT_FISH || event.hook.location.world?.name != Config.skyblockWorldName) {
            return
        }

        val iplayer = getIPlayer(event.player)


        val island = iplayer.getIsland()
        // Check if we even have an island, have a quest, and check the HOOK's position instead of the player, since we dont want people fishing in others islands for edge cases.
        if (!iplayer.hasIsland() || island!!.currentQuest == null || !island.containsBlock(event.hook.location)) {
            return
        }

        val currentQuest = island.currentQuest
        // Find the quest that the island has activated, if none found, return.
        val targetQuest =
            Config.islandQuests.find { quest -> quest.type == QuestGoal.FISHING && quest.name == currentQuest }
                ?: return
        // Use the FISH caught and parse for the version that we need it for.
        val fishNeededForQuest = EntityType.valueOf(targetQuest.goalParameter)

        if (fishNeededForQuest != event.caught?.type) {
            return
        }

        // this just increments quest data.
        island.addQuestData(targetQuest.name)

        if (targetQuest.isComplete(island.getQuestCompletedAmount(targetQuest.name))) {
            island.completeQuest(iplayer, targetQuest)
        }
    }


    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        // FUTURE CONTRIBUTORS: TRY TO SPLIT CHECKS INTO SMALLER BLOCKS.

        if (event.item == null
            || event.clickedBlock == null
            || event.clickedBlock?.location?.world?.name != Config.skyblockWorldName
        ) {
            return
        }

        val iPlayer = getIPlayer(event.player)


        // Check if they have an island or co-op island, if not, deny.
        if (!iPlayer.hasCoopIsland() && !iPlayer.hasIsland()) {
            iPlayer.message(Message.listenerActionDeniedCreateAnIslandFirst)
            event.isCancelled = true
            return
        }

        // Check if they can use the block on the island, or co-op island.
        if (!canUseBlockAtLocation(iPlayer, event.clickedBlock!!.location)) {
            iPlayer.message(Message.listenerPlayerCannotInteract)
            event.isCancelled = true
            return
        }

        // Obsidian to Lava Bucket Check
        if (event.clickedBlock?.type == XMaterial.OBSIDIAN.parseMaterial()
            && event.item?.type == XMaterial.BUCKET.parseMaterial()
        ) {
            if (!hasPermission(event.player, Permission.OBSIDIANTOLAVA)) {
                iPlayer.message(
                    String.format(
                        Message.genericActionRequiresPermission,
                        Permission.OBSIDIANTOLAVA.getFullPermissionNode()
                    )
                )
                return
            }
            event.clickedBlock!!.type = Material.AIR
            event.player.setItemInHand(XMaterial.LAVA_BUCKET.parseItem())
            iPlayer.message(Message.listenerObsidianBucketLava)
            event.isCancelled = true
            return
        }


    }


}