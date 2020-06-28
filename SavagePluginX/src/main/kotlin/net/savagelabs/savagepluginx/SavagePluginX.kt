package net.savagelabs.savagepluginx

import org.bukkit.plugin.java.JavaPlugin

class SavagePluginX : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: SavagePluginX
    }


    override fun onEnable() {
        logger.info("Running Framework Enable.")
        INSTANCE = this
    }

    override fun onDisable() {

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