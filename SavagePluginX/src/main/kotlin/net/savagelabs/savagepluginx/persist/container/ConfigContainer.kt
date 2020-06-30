package net.savagelabs.savagepluginx.persist.container

import net.savagelabs.savagepluginx.SavagePluginX
import java.io.File

interface ConfigContainer : PersistentContainer {
    override fun getFile(): File {
        return File(SavagePluginX.INSTANCE.dataFolder, "$name.yml")
    }
}