package net.savagelabs.savagepluginx.item

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*


class ItemBuilder(private val item: ItemStack) {
    private val meta: ItemMeta? = item.itemMeta

    @JvmOverloads
    constructor(material: Material?, amount: Int = 1, durability: Int = 0) : this(
        ItemStack(
            material!!,
            amount,
            durability.toShort()
        )
    )

    fun durability(durability: Short): ItemBuilder {
        item.durability = durability
        return this
    }

    fun lore(vararg lore: String): ItemBuilder {
        val arrayList = ArrayList<String>()
        for (line in lore) {
            arrayList.add(ChatColor.translateAlternateColorCodes('&', line!!))
        }
        meta!!.lore = arrayList
        return this
    }

    fun lore(lore: List<String>): ItemBuilder {
        meta!!.lore = color(lore)
        return this
    }

    fun name(name: String): ItemBuilder {
        meta!!.setDisplayName(ChatColor.translateAlternateColorCodes('&', name!!))
        return this
    }

    fun build(): ItemStack {
        item.itemMeta = meta
        return item
    }

    fun amount(amount: Int): ItemBuilder {
        item.amount = amount
        return this
    }

    fun glowing(status: Boolean): ItemBuilder {
        if (status) {
            meta!!.addItemFlags(ItemFlag.HIDE_ENCHANTS)
            meta.addEnchant(Enchantment.DURABILITY, 1, true)
        } else {
            meta!!.removeItemFlags(ItemFlag.HIDE_ENCHANTS)
            meta.removeEnchant(Enchantment.DURABILITY)
        }
        return this
    }

    fun addLineToLore(line: String): ItemBuilder {
        val lore = meta!!.lore
        lore!!.add(ChatColor.translateAlternateColorCodes('&', line!!))
        meta.lore = lore
        return this
    }

    companion object {
        fun color(string: List<String>): List<String> {
            val colored: MutableList<String> = ArrayList()
            for (line in string) {
                colored.add(ChatColor.translateAlternateColorCodes('&', line!!))
            }
            return colored
        }
    }

}