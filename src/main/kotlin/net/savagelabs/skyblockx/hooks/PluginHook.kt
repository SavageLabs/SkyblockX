package net.savagelabs.skyblockx.hooks

interface PluginHook {

	val pluginName: String

	fun load()

	fun isHooked(): Boolean

}