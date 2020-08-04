package net.savagelabs.skyblockx.persist.data

import com.cryptomorin.xseries.XMaterial
import com.deanveloper.skullcreator.SkullCreator
import net.savagelabs.savagepluginx.item.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import java.util.*

class SerializableItem(
	var material: XMaterial,
	var name: String,
	var lore: List<String>,
	var amt: Int,
	var skullTexture: String? = null
) {

	fun buildItem(): ItemStack {
		if (Bukkit.getVersion().contains("1.8")) {
			//> 1.8 SkullCreation
			val builder = if (skullTexture != null) {
				val bytes: ByteArray = Base64.getUrlDecoder().decode(skullTexture)
				val textureUrl: String = String(bytes).substring(28).dropLast(4)

				ItemBuilder(SkullCreator.itemFromUrl(textureUrl))
			} else {
				ItemBuilder(material.parseItem()!!)
			}

			return builder.name(name).lore(lore).amount(amt).glowing(false).build()
		} else {
			//> 1.9+ SkullCreation
			val builder = if (skullTexture != null) {
				ItemBuilder(SkullCreator.itemFromBase64(skullTexture))
			} else {
				ItemBuilder(material.parseItem()!!)
			}

			return builder.name(name).lore(lore).amount(amt).glowing(false).build()
		}
	}
}
