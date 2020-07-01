package net.savagelabs.savagepluginx.persist.parser

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import net.savagelabs.savagepluginx.persist.serializer.*
import org.bukkit.Location
import org.bukkit.inventory.Inventory

object JSONJacksonParser : JacksonParser {

    override val mapper = let {
        val mapper = ObjectMapper()
        mapper.registerModule(KotlinModule())
        mapper.registerModule(SimpleModule()
            .addSerializer(Location::class.java, LocationSerializer())
            .addDeserializer(Location::class.java, LocationDeserializer())
            .addSerializer(Inventory::class.java, InventorySerializer())
            .addDeserializer(Inventory::class.java, InventoryDeserializer()))
        mapper.setVisibility(mapper.serializationConfig.defaultVisibilityChecker
            .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
            .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
            .withCreatorVisibility(JsonAutoDetect.Visibility.NONE))
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        mapper
    }

    override fun <T : Any> deserialize(content: String, dataClass: Class<T>): T {
        return mapper.readValue(content, dataClass)
    }

    override fun <T : Any> serialize(instance: T): String {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(instance)
    }
}