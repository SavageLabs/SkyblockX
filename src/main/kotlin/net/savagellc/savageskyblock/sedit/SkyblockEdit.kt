package net.savagellc.savageskyblock.sedit


import net.prosavage.baseplugin.ItemBuilder
import net.prosavage.baseplugin.XMaterial
import net.savagellc.savageskyblock.Globals
import net.savagellc.savageskyblock.core.color
import net.savagellc.savageskyblock.persist.Message
import net.savagellc.savageskyblock.sbf.*
import org.bukkit.Location
import org.bukkit.block.Chest
import org.bukkit.entity.Player
import java.io.File
import java.util.*

class SkyblockEdit {

    fun saveStructure(pos1: Location, pos2: Location, player: Player, name: String) {
        if (pos1.world != pos2.world) {
            player.sendMessage(Message.skyblockEditErrorPositionsNotInSameWorld)
            return
        }
        val playerLoc = player.location
        val container = SbfContainer(
            "SBF_1",
            (pos1.x - playerLoc.x).toInt(),
            (pos1.y - playerLoc.y).toInt(),
            (pos1.z - playerLoc.z).toInt()
        )
        val world = pos1.world

        // Flip values so the loop works correctly even with negative values.
        val xStart = if (pos1.x > pos2.x) pos2.x else pos1.x
        val yStart = if (pos1.y > pos2.y) pos2.y else pos1.y
        val zStart = if (pos1.z > pos2.z) pos2.z else pos1.z
        val xEnd = if (pos1.x < pos2.x) pos2.x else pos1.x
        val yEnd = if (pos1.y < pos2.y) pos2.y else pos1.y
        val zEnd = if (pos1.z < pos2.z) pos2.z else pos1.z

        for (x in xStart.toInt() until xEnd.toInt()) {
            for (y in yStart.toInt() until yEnd.toInt()) {
                for (z in zStart.toInt() until zEnd.toInt()) {
                    val block = world!!.getBlockAt(x, y, z)
//                    if (block.type == Material.AIR) {
//                        continue
//                    }
                    val xRel = block.x - pos1.x
                    val yRel = block.y - pos1.y
                    val zRel = block.z - pos1.z
                    if (block.type == XMaterial.CHEST.parseMaterial()) {
                        val items = Vector<SbfChestItem>()
                        // Loop all slots in block.
                        val chest = block.state as Chest
                        for (slot in 0 until chest.blockInventory.size) {
                            val item = chest.blockInventory.getItem(slot) ?: continue
                            items.add(SbfChestItem(slot, item.amount, XMaterial.matchXMaterial(item.type.name)!!.name))
                        }
//                        val directionalState = chest.blockData as Directional
//                        val direction = when (directionalState.facing) {
//                            BlockFace.NORTH -> 0
//                            BlockFace.EAST -> 1
//                            BlockFace.WEST -> 2
//                            BlockFace.SOUTH -> 3
//                            else -> 0
//                        }
                        container.chests.addElement(SbfChest(xRel, yRel, zRel, 0, chest.blockInventory.size, items))
                        continue
                    }
                    container.blocks.add(SbfBlock(xRel, yRel, zRel, XMaterial.matchXMaterial(block.type.name)!!.name))
                }
            }
        }
        val structuresDir = File(Globals.savageSkyblock.dataFolder, "structures")
        structuresDir.mkdirs()
        SbfWriter(container).write(File(structuresDir, "${name}.structure"))
        player.sendMessage(color(String.format(Message.skyblockEditStructureSaved, "${name}.structure")))
    }

    fun pasteIsland(name: String, location: Location, player: Player?) {
        var file = File(File(Globals.savageSkyblock.dataFolder, "structures"), "$name.structure")
        if (!file.exists()) {
            player?.sendMessage("$name does not exist, using default file: island.structure")
            val file1 = File(Globals.savageSkyblock.dataFolder, "island.structure")
            Globals.savageSkyblock.saveResource("island.structure", false)
            file1.copyTo(File(File(Globals.savageSkyblock.dataFolder, "structures"), "island.structure"))
            file1.delete()
        }
        pasteStructure(file, location, player)
    }


    fun pasteStructure(file: File, relativeLocation: Location, player: Player?) {
        val reader = SbfReader().read(file)
        for (block in reader.blocks) {
            relativeLocation.world!!.getBlockAt(
                block.x.toInt() + relativeLocation.x.toInt() + reader.offsetx,
                block.y.toInt() + relativeLocation.y.toInt() + reader.offsety,
                block.z.toInt() + relativeLocation.z.toInt() + reader.offsetz
            ).type = XMaterial.valueOf(block.type).parseMaterial()!!
        }
        for (chest in reader.chests) {
            val blockAt = relativeLocation.world!!.getBlockAt(
                chest.x.toInt() + relativeLocation.x.toInt() + reader.offsetx,
                chest.y.toInt() + relativeLocation.y.toInt() + reader.offsety,
                chest.z.toInt() + relativeLocation.z.toInt() + reader.offsetz
            )
            blockAt.type = XMaterial.CHEST.parseMaterial()!!
            blockAt.state.update(true)


            val chestState = blockAt.state as Chest
//
//            (chestState.blockData as Directional).setFacingDirection(when (chest.direction) {
//                0 -> BlockFace.NORTH
//                1 -> BlockFace.EAST
//                2 -> BlockFace.WEST
//                3 -> BlockFace.SOUTH
//                else -> BlockFace.NORTH
//            })
            for (item in chest.items) {
                chestState.blockInventory.setItem(
                    item.slot,
                    ItemBuilder(XMaterial.valueOf(item.type).parseItem()).amount(item.amount).build()
                )
            }
        }
        player?.sendMessage(Message.commandSEPasteStructurePasted)
    }

}