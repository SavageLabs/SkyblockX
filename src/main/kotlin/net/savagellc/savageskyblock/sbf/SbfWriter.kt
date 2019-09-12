package net.savagellc.savageskyblock.sbf

import net.savagellc.savageskyblock.sbf.SbfContainer
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.zip.GZIPOutputStream

class SbfWriter(val sbfContainer: SbfContainer) {

    fun write(f:File) {
        val stream = DataOutputStream(GZIPOutputStream(FileOutputStream(f)))
        stream.writeUTF(sbfContainer.version)
        stream.writeInt(sbfContainer.offsetx)
        stream.writeInt(sbfContainer.offsety)
        stream.writeInt(sbfContainer.offsetz)
        val blockTypes = Vector<String>()
        for(block in sbfContainer.blocks) {
            if(!blockTypes.contains(block.type)) {
                blockTypes.add(block.type)
            }
        }
        stream.writeInt(blockTypes.size)
        for(bType in blockTypes) {
            stream.writeUTF(bType)
        }
        stream.writeInt(sbfContainer.blocks.size)
        for(block in sbfContainer.blocks) {
            stream.writeInt(blockTypes.indexOf(block.type))
            stream.writeDouble(block.x)
            stream.writeDouble(block.y)
            stream.writeDouble(block.z)
        }
        stream.writeInt(sbfContainer.chests.size)
        for(chest in sbfContainer.chests) {
            stream.writeDouble(chest.x)
            stream.writeDouble(chest.y)
            stream.writeDouble(chest.z)
            stream.writeInt(chest.direction)
            stream.writeInt(chest.size)
            stream.writeInt(chest.items.size)
            for(item in chest.items) {
                stream.writeInt(item.slot)
                stream.writeInt(item.amount)
                stream.writeUTF(item.type)
            }
        }

        stream.flush()
        stream.close()
    }
}