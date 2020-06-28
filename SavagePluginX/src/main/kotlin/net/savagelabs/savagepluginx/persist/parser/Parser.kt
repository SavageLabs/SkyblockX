package net.savagelabs.savagepluginx.persist.parser

import kotlin.reflect.KClass

interface Parser {

    fun <T: Any> deserialize(content: String, dataClass: KClass<T>): T

    fun <T: Any> serialize(instance: T): String
}