package net.savagelabs.savagepluginx.persist

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.savagelabs.savagepluginx.SavagePluginX
import net.savagelabs.savagepluginx.persist.parser.YAMLJacksonParser
import java.nio.charset.StandardCharsets

object PersistEngine {

    val jsonMapper = jacksonObjectMapper()
    val yamlMapper = let {
        val mapper = ObjectMapper(YAMLFactory())
    }






    private fun resolveDataFolder() {
        val created = SavagePluginX.INSTANCE.dataFolder.mkdirs()
        if (created) SavagePluginX.INSTANCE.logger.info("Created DataFolder.")
    }


    fun <T: ConfigContainer> readConfig(configContainer: T): T?  {
        resolveDataFolder()
        return YAMLJacksonParser.deserialize(configContainer.getFile().readText(StandardCharsets.UTF_8), configContainer::class)
    }


    fun saveConfig(configContainer: ConfigContainer) {
        resolveDataFolder()
        val serializedConfigContent = YAMLJacksonParser.serialize(configContainer)
        configContainer.getFile()
            .writeText(serializedConfigContent, StandardCharsets.UTF_8)
    }



}