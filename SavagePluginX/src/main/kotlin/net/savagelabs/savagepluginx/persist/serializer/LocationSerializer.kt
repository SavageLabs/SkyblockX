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
import org.bukkit.World
import java.io.IOException


class LocationSerializer : StdSerializer<Location>(Location::class.java) {
    override fun serialize(value: Location, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()

        gen.writeStringField("world", value.world?.name)

        gen.writeNumberField("x", value.x)
        gen.writeNumberField("y", value.y)
        gen.writeNumberField("z", value.z)

        gen.writeEndObject()
    }

}

class LocationDeserializer : StdDeserializer<Location>(Location::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Location {
        val node = p.codec.readTree<JsonNode>(p)
        val worldName = node.get("world").toString()
        val x = node.get("x").numberValue().toDouble()
        val y = node.get("y").numberValue().toDouble()
        val z = node.get("z").numberValue().toDouble()

        return Location(Bukkit.getWorld(worldName), x, y, z)
    }

}