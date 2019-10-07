package net.savagellc.savageskyblock.listener

import net.prosavage.baseplugin.XMaterial
import net.savagellc.savageskyblock.core.*
import net.savagellc.savageskyblock.persist.Config
import net.savagellc.savageskyblock.persist.Message
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
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
            || event.entity.location.world?.name != Config.skyblockWorldName) {
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
        if (event.clickedBlock?.type != XMaterial.OBSIDIAN.parseMaterial()
            && event.item?.type != XMaterial.BUCKET.parseMaterial()) {
            if (!hasPermission(event.player, Permission.OBSIDIANTOLAVA)) {
                iPlayer.message(String.format(Message.genericActionRequiresPermission, Permission.OBSIDIANTOLAVA.getFullPermissionNode()))
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