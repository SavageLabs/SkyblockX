package net.savagelabs.skyblockx.sbf

import java.io.*
import java.util.*
import java.util.zip.GZIPInputStream

class SbfReader {
    fun read(f: File, withoutGZIP: Boolean = false): SbfContainer {
        val stream = if (withoutGZIP)
            DataInputStream(FileInputStream(f))
        else
            DataInputStream(GZIPInputStream(FileInputStream(f)))

        val version = stream.readUTF()
        val offsetX = stream.readInt()
        val offsetY = stream.readInt()
        val offsetZ = stream.readInt()
        val typesSize = stream.readInt()
        val types = Vector<String>()
        for (x in 0 until typesSize) {
            types.addElement(stream.readUTF())
        }


        val container = SbfContainer(version, offsetX, offsetY, offsetZ)
        val am = stream.readInt()
        for (x in 0 until am) {
            val blockIndex = stream.readInt()
            val x = stream.readDouble()
            val y = stream.readDouble()
            val z = stream.readDouble()
            container.blocks.add(SbfBlock(x, y, z, types[blockIndex]))
        }
        val chestAm = stream.readInt()
        for (x in 0 until chestAm) {
            val x = stream.readDouble()
            val y = stream.readDouble()
            val z = stream.readDouble()
            val direction = stream.readInt()
            val size = stream.readInt()
            val itemsAmount = stream.readInt()
            val items = Vector<SbfChestItem>()
            for (c in 0 until itemsAmount) {
                val bSlot = stream.readInt()
                val bAmount = stream.readInt()
                val bType = stream.readUTF()
                items.add(SbfChestItem(bSlot, bAmount, bType))
            }
            container.chests.add(SbfChest(x, y, z, direction, size, items))
        }
        return container
    }
}