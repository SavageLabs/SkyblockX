package net.savagellc.savageskyblock.sbf

import java.util.*

class SbfChestItem(val slot: Int, val amount: Int, val type: String)
class SbfBlock(val x: Double, val y: Double, val z: Double, val type: String)
class SbfChest(
    val x: Double,
    val y: Double,
    val z: Double,
    val direction: Int,
    val size: Int,
    val items: List<SbfChestItem>
)

class SbfContainer(val version: String = "SBF_1", val offsetx: Int, val offsety: Int, val offsetz: Int) {
    val blocks = Vector<SbfBlock>()
    val chests = Vector<SbfChest>()

}