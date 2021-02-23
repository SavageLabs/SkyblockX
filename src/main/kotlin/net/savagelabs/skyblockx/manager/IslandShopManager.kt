package net.savagelabs.skyblockx.manager

import com.oop.inteliframework.hologram.Hologram
import com.oop.inteliframework.hologram.builder.HologramBuilder
import com.oop.inteliframework.hologram.rule.RadiusRule
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.Island.ChestShop
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.getIPlayerByUUID
import net.savagelabs.skyblockx.exception.ShopException
import net.savagelabs.skyblockx.hooks.VaultHook
import net.savagelabs.skyblockx.persist.Config
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.*
import org.bukkit.block.data.type.WallSign
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * This enumeration is used to identify responses during
 * purchases, sells etc in shops.
 */
enum class ChestShopResponse {
    SUCCESS,
    PLAYER_NOT_IN_STOCK,
    SHOP_NOT_IN_STOCK,
    PLAYER_INSUFFICIENT_FUNDS,
    SHOP_INSUFFICIENT_FUNDS,
    SHOP_NO_SPACE
}

/**
 * This manager handles everything related to island shops.
 */
object IslandShopManager {
    /**
     * [Int] the packing size of the locations.
     */
    private const val LOCATION_PACK_SIZE: Long = 65536

    /**
     * Encode a location for chest shop storage.
     *
     * @param location [Location] the location to encode.
     * @return [Long]
     */
    fun encode(location: Location): Long = with (location) {
        blockX + (blockY * LOCATION_PACK_SIZE) + blockZ * LOCATION_PACK_SIZE * LOCATION_PACK_SIZE
    }

    /**
     * Check if an inventory has enough of a specific material.
     *
     * @param inventory [Inventory] the inventory to search through.
     * @param material [Material] the material we'd like to find
     * @param amount [Int] the amount of this specific material to check if they have.
     * @return [Boolean]
     */
    private fun hasItems(inventory: Inventory?, material: Material?, amount: Int): Boolean {
        if (inventory == null || material == null) {
            return false
        }

        var amountFound = 0
        for (item in inventory.contents) {
            if (item == null || item.type != material) {
                continue
            }
            amountFound += item.amount
        }

        return amountFound >= amount
    }

    /**
     * Check if a chest shop has the amount needed of it's material in storage.
     *
     * @param inventory [Inventory] the inventory to search.
     * @param shop [ChestShop] the chest shop to verify.
     * @return [Boolean]
     */
    fun hasItems(inventory: Inventory, shop: ChestShop): Boolean = this.hasItems(inventory, shop.material.parseMaterial(), shop.amount.toInt())

    /**
     * Make a player purchase from a shop.
     *
     * @param islandPlayer [IPlayer] the player involved in this purchase.
     * @param shop [ChestShop] the shop of which the player is buying from.
     * @return [ChestShopResponse]
     */
    fun purchase(islandPlayer: IPlayer, shop: ChestShop): ChestShopResponse {
        // player needs to be online
        val player = islandPlayer.getPlayer() ?: throw ShopException("Player cannot be offline during purchase")

        // make sure vault is hooked
        this.checkForVault()

        // we don't want to continue if the player has insufficient funds
        val price = shop.price.toDouble()
        if (!VaultHook.hasEnough(islandPlayer, price)) {
            return ChestShopResponse.PLAYER_INSUFFICIENT_FUNDS
        }

        // make sure shop has items
        val inventory = shop.getInventory() ?: throw ShopException("Inventory cannot be null during purchase")
        if (!this.hasItems(inventory, shop)) {
            return ChestShopResponse.SHOP_NOT_IN_STOCK
        }

        // work inventory
        this.workInventory(inventory, shop)

        // take money from play and if success, continue
        if (VaultHook.takeFrom(islandPlayer, price)?.transactionSuccess() == true && VaultHook.giveTo(islandPlayer, price)?.transactionSuccess() == true) {
            // give the player their items and if full, drop them
            val material = shop.material.parseMaterial() ?: throw ShopException("Shop material cannot be null during purchase")
            with (player) {
                this.inventory.addItem(ItemStack(material, shop.amount.toInt())).forEach { (_, item) ->
                    val amount = item.amount
                    val ofStack = amount / 64
                    val overflow = amount % 64

                    repeat(ofStack) {
                        world.dropItemNaturally(location, ItemStack(material, 64))
                    }

                    if (overflow > 0) world.dropItemNaturally(location, ItemStack(material, overflow))
                }
            }
        }

        // success
        return ChestShopResponse.SUCCESS
    }

    /**
     * Make a player sell their specific materialized items to a shop.
     *
     * @param islandPlayer [IPlayer] the player whom is selling.
     * @param
     */
    fun sell(islandPlayer: IPlayer, shop: ChestShop): ChestShopResponse {
        // player needs to be online
        val player = islandPlayer.getPlayer() ?: throw ShopException("Player cannot be offline during retail")

        // make sure vault is hooked
        this.checkForVault()

        // we don't want to continue if the seller has insufficient funds
        val seller = getIPlayerByUUID(shop.owner) ?: throw ShopException("Seller's IPlayer object cannot be null during retail")
        val price = shop.price.toDouble()

        if (!VaultHook.hasEnough(seller, price)) {
            return ChestShopResponse.SHOP_INSUFFICIENT_FUNDS
        }

        // make sure the player has the corresponding items
        val material = shop.material.parseMaterial() ?: throw ShopException("Material cannot be null during retail")
        val amount = shop.amount.toInt()
        if (!this.hasItems(player.inventory, material, amount)) {
            return ChestShopResponse.PLAYER_NOT_IN_STOCK
        }

        // make sure the shop has space for items
        val inventory = shop.getInventory() ?: throw ShopException("Inventory cannot be null during retail")
        if (!this.shopHasSpace(inventory, material, shop)) {
            return ChestShopResponse.SHOP_NO_SPACE
        }

        // work inventory
        this.workInventory(player.inventory, shop)

        // give player their money and take from seller and if success, give shop it's items
        if (VaultHook.giveTo(islandPlayer, price)?.transactionSuccess() == true && VaultHook.takeFrom(seller, price)?.transactionSuccess() == true) {
            inventory.addItem(ItemStack(material, amount))
        }

        // success
        return ChestShopResponse.SUCCESS
    }

    /**
     * Work the inventory of shop or player.
     *
     * @param inventory [Inventory] the inventory to work.
     * @param shop [ChestShop] the shop where this event occurred.
     */
    private fun workInventory(inventory: Inventory, shop: ChestShop) {
        // necessity
        val size = inventory.size - 1
        val type = shop.material.parseMaterial() ?: throw ShopException("Material cannot be null during inventory work")

        // information about this process
        var amountToRemove = shop.amount.toInt()

        // loop over all items
        for (index in size downTo 0) {
            // break this loop if the amount has successfully been taken from the inventory
            if (amountToRemove <= 0) {
                break
            }

            // fetch the item and continue if the slot is empty or the type is incorrect
            val item = inventory.getItem(index)
            if (item == null || item.type != type) {
                continue
            }

            // calculate the amount of that index there will be left
            val left = with (item.amount) {
                if (this > amountToRemove) {
                    val precise = this - amountToRemove
                    amountToRemove = 0
                    return@with precise
                }

                amountToRemove -= this
                return@with 0
            }

            // set the amount in inventory
            inventory.setItem(index, if (left <= 0) null else {
                item.amount = left; item
            })
        }
    }

    /**
     * Check if a shop has enough space to buy from a player.
     *
     * @param inventory [Inventory] the inventory of the shop to search.
     * @param material [Material] the type of item to check for.
     * @param shop [ChestShop] the shop to check amount in.
     * @return [Boolean]
     */
    private fun shopHasSpace(inventory: Inventory, material: Material, shop: ChestShop): Boolean {
        var space = 0

        for (item in inventory.contents) {
            if (item == null) {
                space += 64
                continue
            }

            if (item.type != material) {
                continue
            }

            val amount = item.amount
            if (amount == 64) {
                continue
            }

            space += 64 - amount
        }

        return space >= shop.amount
    }

    /**
     * Get the inventory of a chest shop.
     *
     * @return [Inventory]
     */
    private fun ChestShop.getInventory(): Inventory? = with (this.location.block.getSignDirectionalBlock()) {
        (this?.state as? Chest)?.inventory
    }

    /**
     * Get attached face from sign if the relative is a chest.
     *
     * @return [BlockFace]
     */
    internal fun Block.getSignDirectionalBlock(): Block? {
        if (!type.name.contains("WALL_SIGN")) {
            return null
        }

        val preciseState = this.state
        val attachedFace: BlockFace = try {
            (preciseState.data as org.bukkit.material.Sign).attachedFace
        } catch (ex: Exception) {
            (preciseState.blockData as WallSign).facing.oppositeFace
        }

        val relative = getRelative(attachedFace)
        return if (relative.type != Material.CHEST) null else relative
    }

    /**
     * Check if a chest already has a shop.
     *
     * @param island [Island] the island to check.
     * @return [Boolean]
     */
    internal fun Block.chestHasShop(island: Island): Boolean {
        if (this.type != Material.CHEST) {
            return false
        }

        val chest = this.state as Chest
        val holder = chest.inventory.holder

        if (holder is DoubleChest) {
            val right = holder.rightSide as Chest
            val left = holder.leftSide as Chest
            return island.chestShops.any { it.value.chestLocation == right.location || it.value.chestLocation == left.location }
        }

        return island.chestShops.any { it.value.chestLocation == this.location }
    }

    /**
     * Build a hologram.
     *
     * @param location [Location] the position of where this hologram will be placed.
     * @param shop [ChestShop] the shop related to this hologram.
     * @return [Hologram]
     */
    internal fun buildHologram(location: Location, shop: ChestShop): Hologram {
        val hologram = HologramBuilder().refreshRate(Config.instance.chestShopHologramRefreshRate)
        val player = shop.islandPlayerOfOwner ?: return hologram.build().also { it.location = location }

        val material = shop.material.toString()
        val price = shop.price.toString()
        val amount = shop.amount.toString()
        val type = shop.type

        hologram.addView {
            // add all lines
            it.addLines { lines ->
                // provide item if enabled
                if (Config.instance.chestShopHologramShowPreviewItem) {
                    lines.displayItem { shop.material.parseItem() }
                }

                // provide all text
                val formatToUse = if (type == "BUY") Config.instance.chestShopHologramFormatBuy else Config.instance.chestShopHologramFormatSell
                for (line in formatToUse) {
                    lines.displayText(color(line
                        .replace("{player}", player.name)
                        .replace("{material}", material)
                        .replace("{price}", price)
                        .replace("{amount}", amount)
                        .replace("{type}", type)
                    ))
                }
            }

            // add the view distance rule if enabled
            val viewDistance = Config.instance.chestShopHologramViewDistance
            if (viewDistance != -1.0) {
                it.addRule(RadiusRule(Config.instance.chestShopHologramViewDistance))
            }
        }

        return hologram.build().also { it.location = location }
    }

    /**
     * Check for vault presence.
     */
    private fun checkForVault() {
        if (!VaultHook.isHooked()) {
            throw ShopException("Vault/Economy provider could not be found, install and restart.")
        }
    }
}