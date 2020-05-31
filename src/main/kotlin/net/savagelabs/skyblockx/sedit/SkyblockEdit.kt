package net.savagelabs.skyblockx.sedit


import com.cryptomorin.xseries.XMaterial
import net.prosavage.baseplugin.ItemBuilder
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.color
import net.savagelabs.skyblockx.core.enumValueOrNull
import net.savagelabs.skyblockx.persist.Message
import net.savagelabs.skyblockx.sbf.*
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Chest
import org.bukkit.entity.Player
import java.io.File
import java.util.*


/**
 * This class is dog shit right now, I need to rewrite it to actually support more operations :P
 */
class SkyblockEdit {

    fun normalizeBlockName(xmaterialDefaultName: String): String {
        /*
        * This function is returning the right syntax of item names
        *
        * @inputName string that need to be translated
        * */
        val regex = """(Optional|optional)\[(\w+\s*)+]""".toRegex()
        if(regex.matches(xmaterialDefaultName)){
          return xmaterialDefaultName
              .toUpperCase()
              .replace("OPTIONAL", "")
              .replace(" ", "_")
              .replace("[", "")
              .replace("]", "")
        }else{
          return xmaterialDefaultName
        }
    }

    fun saveStructure(pos1: Location, pos2: Location, player: Player, name: String, skipAir: Boolean = false) {
        if (pos1.world != pos2.world) {
            player.sendMessage(Message.messagePrefix + Message.skyblockEditErrorPositionsNotInSameWorld)
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

        for (x in xStart.toInt() - 1  until xEnd.toInt() + 1) {
            for (y in yStart.toInt() - 1 until yEnd.toInt() + 1) {
                for (z in zStart.toInt() - 1 until zEnd.toInt() + 1) {
                    val block = world!!.getBlockAt(x, y, z)
                    if (skipAir && block.type == Material.AIR) {
                        continue
                    }
                    val xRel = block.x - pos1.x
                    val yRel = block.y - pos1.y
                    val zRel = block.z - pos1.z
                    if (block.type == XMaterial.CHEST.parseMaterial()) {
                        val items = Vector<SbfChestItem>()
                        // Loop all slots in block.
                        val chest = block.state as Chest
                        for (slot in 0 until chest.blockInventory.size) {
                            val item = chest.blockInventory.getItem(slot) ?: continue

                            items.add(SbfChestItem(slot, item.amount, normalizeBlockName(XMaterial.matchXMaterial(item.type.name).toString()) ))
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

                    container.blocks.add(SbfBlock(xRel, yRel, zRel, normalizeBlockName(XMaterial.matchXMaterial(block.type.name).toString()) ))
                }
            }
        }
        val structuresDir = File(SkyblockX.skyblockX.dataFolder, "structures")
        structuresDir.mkdirs()
        SbfWriter(container).write(File(structuresDir, "${name}.structure"))
        player.sendMessage(
            color(
                Message.messagePrefix + String.format(
                    Message.skyblockEditStructureSaved,
                    "${name}.structure"
                )
            )
        )
    }

    fun pasteIsland(name: String, location: Location, player: Player?) {
        var file = File(File(SkyblockX.skyblockX.dataFolder, "structures"), "$name.structure")

        if (!file.exists()) {
            player?.sendMessage("$name does not exist, using default file: island.structure")
            val fileIsland = File(SkyblockX.skyblockX.dataFolder, "island.structure")
            val fileNether = File(SkyblockX.skyblockX.dataFolder, "nether-island.structure")
            SkyblockX.skyblockX.saveResource("island.structure", false)
            SkyblockX.skyblockX.saveResource("nether-island.structure", false)
            val copytoStruct = File(File(SkyblockX.skyblockX.dataFolder, "structures"), "island.structure")
            if (!copytoStruct.exists()) fileIsland.copyTo(copytoStruct, false)
            val copyToNetherStruct = File(File(SkyblockX.skyblockX.dataFolder, "structures"), "nether-island.structure")
            if (!copyToNetherStruct.exists()) fileNether.copyTo(copyToNetherStruct, false)
            fileNether.delete()
            fileIsland.delete()
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
            ).type = enumValueOrNull<XMaterial>(block.type)?.parseMaterial() ?: Material.AIR
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
                val valueOf = XMaterial.valueOf(item.type).parseMaterial() ?: Material.valueOf(item.type)
                chestState.blockInventory.setItem(item.slot, ItemBuilder(valueOf).amount(item.amount).build())
            }
        }
        player?.sendMessage(Message.commandSEPasteStructurePasted)
    }

}