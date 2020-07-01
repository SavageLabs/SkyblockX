package net.savagelabs.savagepluginx.persist.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


fun createInventoryModule(): SimpleModule {
    val module = SimpleModule()
    return module.addSerializer(Inventory::class.java, InventorySerializer())
}


class InventoryDeserializer : StdDeserializer<Inventory>(Inventory::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Inventory? {
            val node = p.codec.readTree<JsonNode>(p)
            val contents = fromBase64(node.get("contents").toString())
            return contents
    }

    fun fromBase64(data: String?): Inventory? {
        try {
            val inputStream = ByteArrayInputStream(Base64Coder.decodeLines(data))
            val dataInput = BukkitObjectInputStream(inputStream)
            val inventory = Bukkit.getServer().createInventory(null, dataInput.readInt())

            // Read the serialized inventory
            for (i in 0 until inventory.size) {
                inventory.setItem(i, dataInput.readObject() as ItemStack)
            }
            dataInput.close()
            return inventory
        } catch (e: java.lang.Exception) {
        }
        return null
    }

}


class InventorySerializer : StdSerializer<Inventory>(Inventory::class.java) {
    override fun serialize(value: Inventory, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()

        gen.writeStringField("contents", toBase64(value))

        gen.writeEndObject()
    }

    fun toBase64(inventory: Inventory): String? {
        return try {
            val outputStream = ByteArrayOutputStream()
            val dataOutput = BukkitObjectOutputStream(outputStream)

            // Write the size of the inventory
            dataOutput.writeInt(inventory.size)

            // Save every element in the list
            for (i in 0 until inventory.size) {
                dataOutput.writeObject(inventory.getItem(i))
            }

            // Serialize that array
            dataOutput.close()
            Base64Coder.encodeLines(outputStream.toByteArray())
        } catch (e: Exception) {
            throw IllegalStateException("Cannot into itemstacksz!", e)
        }
    }
}