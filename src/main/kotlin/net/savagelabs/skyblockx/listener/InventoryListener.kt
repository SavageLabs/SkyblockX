package net.savagelabs.skyblockx.listener

import net.savagelabs.skyblockx.core.IslandPermission
import net.savagelabs.skyblockx.core.getIPlayer
import net.savagelabs.skyblockx.event.IslandChestMoveItemEvent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.*

class InventoryListener : Listener {

    // This whole class contains extremely messy code and can be majorly improved :/

    @EventHandler
    fun onChestOpen(event: InventoryOpenEvent) {
        val iPlayer = getIPlayer(event.player as Player)
        if (!iPlayer.hasIsland()) {
            return
        }

        if (event.inventory != iPlayer.getIsland()!!.inventory) {
            return
        }

        if (!iPlayer.hasIslandPermission(IslandPermission.CHEST_OPEN)) {
            event.isCancelled = true
            iPlayer.messageNoPermission(IslandPermission.CHEST_OPEN)
        }

    }

    @EventHandler
    fun onChestMove(event: IslandChestMoveItemEvent) {
        if (event.action == IslandChestMoveItemEvent.Action.DRAG) {
            if (!event.iPlayer.hasIslandPermission(IslandPermission.CHEST_PUT)) {
                event.iPlayer.messageNoPermission(IslandPermission.CHEST_PUT)
                event.isCancelled = true
            }
            return
        }

        if (event.moveActionList.size > 1) {
            if (!event.iPlayer.hasIslandPermission(IslandPermission.CHEST_PUT)) {
                event.iPlayer.messageNoPermission(IslandPermission.CHEST_PUT)
                event.isCancelled = true
            } else if (!event.iPlayer.hasIslandPermission(IslandPermission.CHEST_TAKE)) {
                event.iPlayer.messageNoPermission(IslandPermission.CHEST_TAKE)
                event.isCancelled = true
            }
            return
        }

        if (event.moveActionList[0].type == InventoryMoveAction.Type.PUT) {
            if (!event.iPlayer.hasIslandPermission(IslandPermission.CHEST_PUT)) {
                event.iPlayer.messageNoPermission(IslandPermission.CHEST_PUT)
                event.isCancelled = true
            }
            return
        }

        if (!event.iPlayer.hasIslandPermission(IslandPermission.CHEST_TAKE)) {
            event.iPlayer.messageNoPermission(IslandPermission.CHEST_TAKE)
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onChestClick(event: InventoryClickEvent) {
        val iPlayer = getIPlayer(event.whoClicked as Player)
        if (!iPlayer.hasIsland()) {
            return
        }

        val moveActionList = mutableListOf<InventoryMoveAction>()
        if (event.clickedInventory != iPlayer.getIsland()!!.inventory) {
            if (event.whoClicked.openInventory.topInventory == iPlayer.getIsland()!!.inventory) {
                if (event.action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                    val item = event.currentItem!!.type
                    val amount = event.currentItem!!.amount
                    moveActionList.add(InventoryMoveAction(InventoryMoveAction.Type.PUT, item, amount))
                }

                if (moveActionList.isNotEmpty()) {
                    val chestEvent = IslandChestMoveItemEvent(iPlayer, iPlayer.getIsland()!!, IslandChestMoveItemEvent.Action.CLICK, moveActionList)
                    Bukkit.getPluginManager().callEvent(chestEvent)
                    if (chestEvent.isCancelled) {
                        event.isCancelled = true
                    }
                }
            }
            return
        }

        // Please don't judge me, I was very tired when I was doing this

        var done = true
        when (event.action) {
            InventoryAction.DROP_ONE_SLOT -> {
                val item = event.currentItem!!.type
                val amount = event.currentItem!!.amount - 1
                moveActionList.add(InventoryMoveAction(InventoryMoveAction.Type.TAKE, item, amount))
            }
            InventoryAction.DROP_ALL_SLOT -> {
                val item = event.currentItem!!.type
                val amount = event.currentItem!!.amount
                moveActionList.add(InventoryMoveAction(InventoryMoveAction.Type.TAKE, item, amount))
            }
            InventoryAction.HOTBAR_MOVE_AND_READD -> {
                val item = event.currentItem!!.type
                val amount = event.currentItem!!.amount
                moveActionList.add(InventoryMoveAction(InventoryMoveAction.Type.TAKE, item, amount))
            }
            InventoryAction.HOTBAR_SWAP -> {
                if (event.currentItem!!.type != Material.AIR) {
                    val item = event.currentItem!!.type
                    val amount = event.currentItem!!.amount
                    moveActionList.add(InventoryMoveAction(InventoryMoveAction.Type.TAKE, item, amount))
                } else {
                    val hotbarItem = event.whoClicked.inventory.getItem(event.hotbarButton)
                    if (hotbarItem != null) {
                        val item = hotbarItem.type
                        val amount = hotbarItem.amount
                        moveActionList.add(InventoryMoveAction(InventoryMoveAction.Type.PUT, item, amount))
                    }
                }
            }
            InventoryAction.MOVE_TO_OTHER_INVENTORY -> {
                val item = event.currentItem!!.type
                val amount = event.currentItem!!.amount
                moveActionList.add(InventoryMoveAction(InventoryMoveAction.Type.TAKE, item, amount))
            }
            InventoryAction.PICKUP_ALL -> {
                val item = event.currentItem!!.type
                val amount = event.currentItem!!.amount
                moveActionList.add(InventoryMoveAction(InventoryMoveAction.Type.TAKE, item, amount))
            }
            InventoryAction.PICKUP_HALF -> {
                val item = event.currentItem!!.type
                val tempAmount = event.currentItem!!.amount
                val amount: Int
                if (tempAmount % 2 == 1) {
                    amount = tempAmount / 2 + 1
                } else {
                    amount = tempAmount / 2
                }
                moveActionList.add(InventoryMoveAction(InventoryMoveAction.Type.TAKE, item, amount))
            }
            InventoryAction.PICKUP_ONE -> {
                val item = event.currentItem!!.type
                val amount = 1
                moveActionList.add(InventoryMoveAction(InventoryMoveAction.Type.TAKE, item, amount))
            }
            InventoryAction.PLACE_ALL -> {
                val item = event.cursor!!.type
                val amount = event.cursor!!.amount
                moveActionList.add(InventoryMoveAction(InventoryMoveAction.Type.PUT, item, amount))
            }
            InventoryAction.PLACE_ONE -> {
                val item = event.cursor!!.type
                val amount = 1
                moveActionList.add(InventoryMoveAction(InventoryMoveAction.Type.PUT, item, amount))
            }
            InventoryAction.PLACE_SOME -> {
                val item = event.currentItem!!.type
                val amount = event.currentItem!!.amount
                moveActionList.add(InventoryMoveAction(InventoryMoveAction.Type.TAKE, item, amount))
            }
            InventoryAction.SWAP_WITH_CURSOR -> {
                val takeItem = event.currentItem!!.type
                val takeAmount = event.currentItem!!.amount
                moveActionList.add(InventoryMoveAction(InventoryMoveAction.Type.TAKE, takeItem, takeAmount))
                val putItem = event.cursor!!.type
                val putAmount = event.cursor!!.amount
                moveActionList.add(InventoryMoveAction(InventoryMoveAction.Type.PUT, putItem, putAmount))
            }
            InventoryAction.COLLECT_TO_CURSOR -> {}
            InventoryAction.NOTHING -> {}
            else -> done = false
        }

        if (done && moveActionList.isNotEmpty()) {
            val chestEvent = IslandChestMoveItemEvent(iPlayer, iPlayer.getIsland()!!, IslandChestMoveItemEvent.Action.CLICK, moveActionList)
            Bukkit.getPluginManager().callEvent(chestEvent)
            if (chestEvent.isCancelled) {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onChestDrag(event: InventoryDragEvent) {
        val iPlayer = getIPlayer(event.whoClicked as Player)
        if (!iPlayer.hasIsland()) {
            return
        }

        if (event.inventory != iPlayer.getIsland()!!.inventory) {
            return
        }

        val moveActionList = mutableListOf<InventoryMoveAction>()
        for ((_, item) in event.newItems) {
            moveActionList.add(InventoryMoveAction(InventoryMoveAction.Type.PUT, item.type, item.amount))
        }

        if (moveActionList.isNotEmpty()) {
            val chestEvent = IslandChestMoveItemEvent(iPlayer, iPlayer.getIsland()!!, IslandChestMoveItemEvent.Action.DRAG, moveActionList)
            Bukkit.getPluginManager().callEvent(chestEvent)
            if (chestEvent.isCancelled) {
                event.isCancelled = true
            }
        }
    }
}

data class InventoryMoveAction(val type: Type, val item: Material, val amount: Int) {
    enum class Type {
        TAKE,
        PUT
    }
}