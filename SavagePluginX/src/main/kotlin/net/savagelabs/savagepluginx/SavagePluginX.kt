package net.savagelabs.savagepluginx

import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

abstract class SavagePluginX : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: SavagePluginX
    }

    abstract fun enable()
    abstract fun disable()

    override fun onEnable() {
        logger.info("Running Framework Enable.")
        INSTANCE = this
        enable()
    }

    override fun onDisable() {
        disable()
    }

    fun registerListeners(vararg listeners: Listener) {
        for (listener in listeners) {
            server.pluginManager.registerEvents(listener, INSTANCE)
        }
    }



    private fun printPluginInformation() {
        logger.info("================================================")
        logger.info("Plugin: " + description.name)
        logger.info("Version: " + description.version)
        logger.info("Author(s): " + description.authors)
        logger.info("Description: " + description.description)
        logger.info("-- Powered By SavagePluginX")
        logger.info("================================================")
    }




}