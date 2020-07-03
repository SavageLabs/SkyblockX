package net.savagelabs.skyblockx.persist.data

import com.cryptomorin.xseries.XMaterial
import com.deanveloper.skullcreator.SkullCreator
import net.savagelabs.savagepluginx.item.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack

class SerializableItem(
    var material: XMaterial,
    var name: String,
    var lore: List<String>,
    var amt: Int,
    var skullTexture: String? = null
) {

    fun buildItem(): ItemStack {
        val builder = if (skullTexture != null) {
            ItemBuilder( SkullCreator.itemFromBase64(skullTexture))
        } else {
            ItemBuilder(material.parseItem()!!)
        }
        return builder.name(name).lore(lore).amount(amt).glowing(false).build()
    }

}
