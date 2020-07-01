package net.savagelabs.savagepluginx.persist.parser

import com.fasterxml.jackson.databind.ObjectMapper

interface JacksonParser : Parser {
    val mapper: ObjectMapper
}