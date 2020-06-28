package net.savagelabs.savagepluginx.persist

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.savagelabs.savagepluginx.SavagePluginX

object PersistEngine {

    val jsonMapper = jacksonObjectMapper()
    val yamlMapper = let {
        val mapper = ObjectMapper(YAMLFactory())
    }






    private fun resolveDataFolder() {
        val created = SavagePluginX.INSTANCE.dataFolder.mkdirs()
        if (created) SavagePluginX.INSTANCE.logger.info("Created DataFolder.")
    }


    fun saveConfig(configFile: ConfigFile) {
        resolveDataFolder()

    }



}