package net.savagelabs.savagepluginx.persist.engine

import net.savagelabs.savagepluginx.persist.container.PersistentContainer
import net.savagelabs.savagepluginx.persist.parser.JSONJacksonParser
import net.savagelabs.savagepluginx.persist.parser.YAMLJacksonParser
import java.nio.charset.StandardCharsets

object FlatDataManager : PersistentEngine {

    override fun <T : PersistentContainer> read(persistentContainer: T): T? {
        resolveDataFolder()
        return JSONJacksonParser.deserialize(persistentContainer.getFile().readText(StandardCharsets.UTF_8), persistentContainer::class.java)
    }

    override fun save(persistentContainer: PersistentContainer) {
        resolveDataFolder()
        val serializedConfigContent = JSONJacksonParser.serialize(persistentContainer)
        persistentContainer.getFile()
            .writeText(serializedConfigContent, StandardCharsets.UTF_8)
    }

}