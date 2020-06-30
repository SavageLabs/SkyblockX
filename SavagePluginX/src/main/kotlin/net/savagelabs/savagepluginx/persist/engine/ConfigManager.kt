package net.savagelabs.savagepluginx.persist.engine

import net.savagelabs.savagepluginx.persist.container.PersistentContainer
import net.savagelabs.savagepluginx.persist.parser.YAMLJacksonParser
import java.nio.charset.StandardCharsets

object ConfigManager : PersistentEngine {

    override fun <T: PersistentContainer> read(persistentContainer: T): T?  {
        resolveDataFolder()
        return YAMLJacksonParser.deserialize(persistentContainer.getFile().readText(StandardCharsets.UTF_8), persistentContainer::class.java)
    }

    override fun save(persistentContainer: PersistentContainer) {
        resolveDataFolder()
        val serializedConfigContent = YAMLJacksonParser.serialize(persistentContainer)
        persistentContainer.getFile()
            .writeText(serializedConfigContent, StandardCharsets.UTF_8)
    }

}