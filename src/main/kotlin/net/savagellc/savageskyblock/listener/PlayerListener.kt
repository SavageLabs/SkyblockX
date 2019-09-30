package net.savagellc.savageskyblock.listener

import net.prosavage.baseplugin.XMaterial
import net.savagellc.savageskyblock.core.Permission
import net.savagellc.savageskyblock.core.color
import net.savagellc.savageskyblock.core.getIPlayer
import net.savagellc.savageskyblock.core.hasPermission
import net.savagellc.savageskyblock.persist.Config
import net.savagellc.savageskyblock.persist.Message
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
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
    fun onPlayerFall(event: EntityDamageEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.FALL || event.entity !is Player) {
            return
        }

        val iPlayer = getIPlayer(event.entity as Player)
        if (iPlayer.falling) {
            iPlayer.falling = false
            event.isCancelled = true
        }

    }


    @EventHandler
    fun onPlayerVoidDamage(event: EntityDamageEvent) {
        if (!Config.preventFallingDeaths || event.cause != EntityDamageEvent.DamageCause.VOID || event.entity !is Player) {
            return
        }

        val player = event.entity as Player
        val iPlayer = getIPlayer(player)
        iPlayer.falling = true
        player.sendMessage(color(Message.listenerVoidDeathPrevented))
        if (iPlayer.hasIsland()) {
            player.teleport(iPlayer.getIsland()!!.getIslandSpawn())
        } else {
            player.teleport(Bukkit.getWorld("world")!!.spawnLocation)
        }
        event.isCancelled = true
    }


    @EventHandler
    fun onBucketInteract(event: PlayerInteractEvent) {
        if (event.item == null
            || event.clickedBlock == null
            || event.clickedBlock!!.type != XMaterial.OBSIDIAN.parseMaterial()
            || event.item!!.type != XMaterial.BUCKET.parseMaterial()
        ) {
            return
        }


        if (!hasPermission(event.player, Permission.OBSIDIANTOLAVA)) {
            event.player.sendMessage(
                color(
                    String.format(
                        Message.genericActionRequiresPermission,
                        Permission.OBSIDIANTOLAVA.getFullPermissionNode()
                    )
                )
            )
            return
        }

        event.clickedBlock!!.type = Material.AIR
        event.player.setItemInHand(XMaterial.LAVA_BUCKET.parseItem())
        event.player.sendMessage(color(Message.listenerObsidianBucketLava))
        event.isCancelled = true

    }


}