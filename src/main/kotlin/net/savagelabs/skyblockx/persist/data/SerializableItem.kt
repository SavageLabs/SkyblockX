package net.savagelabs.skyblockx.persist.data

import com.cryptomorin.xseries.XMaterial
import net.prosavage.baseplugin.ItemBuilder
import org.bukkit.inventory.ItemStack

class SerializableItem(
    var material: XMaterial,
    var name: String,
    var lore: List<String>,
    var amt: Int
) {

    fun buildItem(): ItemStack {
        return ItemBuilder(material.parseItem()).name(name).lore(lore).amount(amt).glowing(false).build()
    }

}