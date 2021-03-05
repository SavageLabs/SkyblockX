package net.savagelabs.skyblockx.hooks

import net.brcdev.shopgui.ShopGuiPlusApi
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack

/**
 * This hook holds all functionality for ShopGuiPlus.
 */
object ShopGuiHook : PluginHook {
    /**
     * [String] This name of the plugin related to this hook.
     */
    override val pluginName: String = "ShopGUIPlus"

    /**
     * [Boolean] Whether or not it's been hooked.
     */
    private val hooked: Boolean by lazyOf(Bukkit.getPluginManager().isPluginEnabled(pluginName))

    /**
     * Get the price of an item stack.
     *
     * @param itemStack [ItemStack] the item stack to fetch price of.
     * @return If present, [Double] with corresponding value otherwise null.
     */
    fun priceOf(itemStack: ItemStack): Double? =
        if (!hooked) null else ShopGuiPlusApi.getItemStackPriceBuy(itemStack).let { if (it == -1.0) null else it }

    /**
     * Load the hook. We don't need anything in this block as the API is statically accessed.
     */
    override fun load() {}

    /**
     * Get whether or not this hook is enabled.
     *
     * @return [Boolean]
     */
    override fun isHooked(): Boolean = hooked
}