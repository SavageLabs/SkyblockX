package net.savagelabs.savagepluginx.persist.parser

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule

object YAMLJacksonParser : JacksonParser {
    override val mapper = let {
        val mapper = ObjectMapper(YAMLFactory())
        mapper.registerModule(KotlinModule())
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
        mapper
    }

    override fun <T : Any> deserialize(content: String, dataClass: Class<T>): T {
        return mapper.readValue(content, dataClass)
    }

    override fun <T : Any> serialize(instance: T): String {
        return mapper.writeValueAsString(instance)
    }
}
