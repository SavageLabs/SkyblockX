package net.savagelabs.savagepluginx.persist.parser

interface Parser {

    fun <T: Any> deserialize(content: String, dataClass: Class<T>): T

    fun <T: Any> serialize(instance: T): String
}