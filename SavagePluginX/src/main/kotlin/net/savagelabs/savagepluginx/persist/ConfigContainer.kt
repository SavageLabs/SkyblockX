package net.savagelabs.savagepluginx.persist

import net.savagelabs.savagepluginx.SavagePluginX
import java.io.File

interface ConfigContainer {

    val name: String

    fun getFile(): File = File(SavagePluginX.INSTANCE.dataFolder, "$name.yml")

}