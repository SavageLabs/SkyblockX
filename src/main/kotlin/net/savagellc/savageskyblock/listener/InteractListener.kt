package net.savagellc.savageskyblock.listener

import net.prosavage.baseplugin.XMaterial
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class InteractListener : Listener {



    @EventHandler
    fun onBucketInteract(event: PlayerInteractEvent) {
        if (event.item == null
            || event.clickedBlock == null
            || event.clickedBlock!!.type != XMaterial.OBSIDIAN.parseMaterial()
            || event.item!!.type != XMaterial.BUCKET.parseMaterial()) {
           return
        }

        event.clickedBlock!!.type = Material.AIR
        event.player.setItemInHand(XMaterial.LAVA_BUCKET.parseItem())
        event.isCancelled = true
    }


}