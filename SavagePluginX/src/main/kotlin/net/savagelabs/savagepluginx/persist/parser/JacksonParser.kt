package net.savagelabs.savagepluginx.persist.parser

import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.reflect.KClass

interface JacksonParser : Parser {
    val mapper: ObjectMapper
}