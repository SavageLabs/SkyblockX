package net.savagelabs.skyblockx.listener

import com.cryptomorin.xseries.XMaterial
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.core.getIslandFromLocation
import net.savagelabs.skyblockx.manager.ChestShopResponse
import net.savagelabs.skyblockx.registry.HologramIdentifier
import net.savagelabs.skyblockx.registry.HologramRegistry
import net.savagelabs.skyblockx.manager.IslandShopManager
import net.savagelabs.skyblockx.manager.IslandShopManager.buildHologram
import net.savagelabs.skyblockx.manager.IslandShopManager.chestHasShop
import net.savagelabs.skyblockx.manager.IslandShopManager.encode
import net.savagelabs.skyblockx.manager.IslandShopManager.getSignDirectionalBlock
import net.savagelabs.skyblockx.persist.Config
import net.savagelabs.skyblockx.persist.Message
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.player.PlayerInteractEvent

/**
 * This listener handles all shop related events.
 */
object ShopListener : Listener {
    /**
     * This event handles the base of creating a chest shop.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private fun SignChangeEvent.onCreation() {
        // make sure it's not cancelled...
        if (isCancelled) {
            return
        }

        // the sign's sign line has to either be [BUY] or [SELL]
        val size = lines.size
        val first = lines.elementAt(0)

        if (!first.equals("[BUY]", true) && !first.equals("[SELL]", true)) {
            return
        }

        // necessity
        val islandPlayer = player.getIPlayer()
        val chestBlock = block.getSignDirectionalBlock() ?: return
        val message = Message.instance

        // tell the player to specify a valid material
        val material = XMaterial.matchXMaterial(lines.elementAt(1).ifEmpty { "JUST_FOR_ERROR_PURPOSE" })
        if (size == 1 || !material.isPresent) {
            islandPlayer.message(message.chestShopSpecifyMaterial)
            return
        }

        // tell the player to specify a valid amount and make sure it's below limit
        val amount = lines.elementAt(2).toIntOrNull()
        if (size == 2 || amount == null || amount <= 0) {
            islandPlayer.message(message.chestShopSpecifyAmount)
            return
        }

        val maximumAmount = Config.instance.chestShopMaximumAmount
        if (amount > maximumAmount) {
            islandPlayer.message(message.chestShopAmountTooHigh, maximumAmount.toString())
            return
        }

        // tell the player to specify a valid price
        val price = lines.elementAt(3).toIntOrNull()
        if (size == 3 || price == null || price < 0) {
            islandPlayer.message(message.chestShopSpecifyPrice)
            return
        }

        // make sure it's on their island
        val location = block.location
        val island = getIslandFromLocation(location)

        if (!islandPlayer.hasIsland() || island == null || islandPlayer.getIsland() != island) {
            islandPlayer.message(message.chestShopCreationAtOtherIsland)
            return
        }

        // make sure shop doesn't exist at this location
        if (chestBlock.chestHasShop(island)) {
            islandPlayer.message(message.chestShopAlreadyExist)
            return
        }

        // format sign, set up shop and then register hologram
        val type = if (first == "[BUY]") "BUY" else "SELL"
        val exactMaterial = material.get()
        val materialName = exactMaterial.name

        for ((index, line) in Config.instance.chestShopSignFormat.withIndex()) {
            setLine(index, color(
                    line
                        .replace("{player}", player.name)
                        .replace("{material}", materialName)
                        .replace("{price}", price.toString())
                        .replace("{amount}", amount.toString())
                        .replace("{type}", type)
            ))
        }

        with (Island.ChestShop(
            location.world?.environment ?: return, location, chestBlock.location,
            islandPlayer.uuid, type, exactMaterial, amount.toShort(), price
        )) {
            island.chestShops[encode(location)] = this
            this.handleHologram()
        }

        // send the successful creation message
        islandPlayer.message(
                message.chestShopCreationSuccess
                        .joinToString("\n")
                        .format(materialName, amount, price, type.toLowerCase().capitalize()),
                true
        )
    }

    /**
     * This event handles all shop interaction.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private fun PlayerInteractEvent.onShop() {
        // make sure it's not cancelled and the clicked block is not null
        if (isCancelled || clickedBlock == null || action != Action.RIGHT_CLICK_BLOCK) {
            return
        }

        // necessity
        val relativeBlock = clickedBlock?.getSignDirectionalBlock() ?: return
        val location = relativeBlock.location
        val island = getIslandFromLocation(location) ?: return
        val shop = island.chestShops[encode(clickedBlock?.location ?: return)] ?: return
        val isBuy = shop.type == "BUY"

        // make sure the player that is clicking is not the owner
        val islandPlayer = player.getIPlayer()
        if (shop.owner == player.uniqueId) {
            islandPlayer.message(Message.instance.chestShopInteractingIsOwner)
            return
        }

        // handle purchase / retail
        val message = Message.instance
        val price = shop.price.toString()
        val amount = shop.amount.toString()
        val material = shop.material.toString()
        val playerName = player.name
        val owner = shop.islandPlayerOfOwner
        val ownerName = owner?.name ?: "Unknown"

        if (isBuy) {
            when (IslandShopManager.purchase(islandPlayer, shop)) {
                ChestShopResponse.SHOP_NOT_IN_STOCK ->
                    islandPlayer.message(message.chestShopNotInStock)
                ChestShopResponse.PLAYER_INSUFFICIENT_FUNDS ->
                    islandPlayer.message(message.chestShopPlayerInsufficientFunds, price)
                ChestShopResponse.SUCCESS -> {
                    islandPlayer.message(message.chestShopPurchased, amount, material, ownerName, price)
                    owner?.message(message.chestShopPurchasedSeller, amount, material, playerName, price)
                }
                else -> {}
            }
            return
        }

        when (IslandShopManager.sell(islandPlayer, shop)) {
            ChestShopResponse.PLAYER_NOT_IN_STOCK ->
                islandPlayer.message(message.chestShopPlayerNotInStock)
            ChestShopResponse.SHOP_NO_SPACE ->
                islandPlayer.message(message.chestShopNoSpace)
            ChestShopResponse.SHOP_INSUFFICIENT_FUNDS ->
                islandPlayer.message(message.chestShopInsufficientFunds)
            ChestShopResponse.SUCCESS -> {
                islandPlayer.message(message.chestShopSold, amount, material, ownerName, price)
                owner?.message(message.chestShopSoldSeller, playerName, amount, material, price)
            }
            else -> {}
        }
    }

    /**
     * This event handles the shop breaking pace.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private fun BlockBreakEvent.onShop() {
        // make sure it's not cancelled and that it is a sign
        val type = block.type
        if (isCancelled || !type.name.contains("WALL_SIGN") && block.type != Material.CHEST) {
            return
        }

        // necessity
        val blockLocation = block.location
        val island = getIslandFromLocation(blockLocation) ?: return

        // if it's a chest
        if (type == Material.CHEST) {
            val shop = island.chestShops.values.find { it.chestLocation == blockLocation } ?: return
            shop.destroy()
            island.chestShops.values.remove(shop)
            return
        }

        // sign
        val encoded = encode(blockLocation)
        val shop = island.chestShops[encoded] ?: return

        // handle
        shop.destroy()
        island.chestShops.remove(encoded)
    }

    /**
     * Handle a shop's hologram.
     */
    internal fun Island.ChestShop.handleHologram() {
        if (!Config.instance.chestShopUseHologram) {
            return
        }

        HologramRegistry.register(
            HologramIdentifier(this.hologramId.toString()),
            buildHologram(chestLocation.clone().add(0.5, Config.instance.chestShopHologramYOffset, 0.5), this)
        )
    }
}