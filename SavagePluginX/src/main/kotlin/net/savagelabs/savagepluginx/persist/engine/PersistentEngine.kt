package net.savagelabs.savagepluginx.persist.engine

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import net.savagelabs.savagepluginx.SavagePluginX
import net.savagelabs.savagepluginx.persist.container.PersistentContainer
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception
import java.util.logging.Level

interface PersistentEngine {

    fun resolveDataFolder() {
        val created = SavagePluginX.INSTANCE.dataFolder.mkdirs()
        if (created) SavagePluginX.INSTANCE.logger.info("Created DataFolder.")
    }

    fun <T: PersistentContainer> read(persistentContainer: T): T?

    fun <T: PersistentContainer> readOrSave(persistentContainer: T): T {
        resolveDataFolder()
        return try {
            read(persistentContainer) ?: persistentContainer
        } catch (ex: Exception) {
            val message = when (ex) {
                is FileNotFoundException -> "The file was not found, creating new one from scratch."
                is InvalidFormatException -> "Could not read config due to invalid formatting on an option. Generating a new file.\n ${ex.message}"
                is UnrecognizedPropertyException -> "Unrecognized property was found, Generating a new config. \n ${ex.message}"
                else -> "Something went horribly wrong... Generating new config.\n ${ex.printStackTrace()}"
            }
            val file = persistentContainer.getFile()
            if (file.exists()) {
                val brokenFile = File(file.absoluteFile.absolutePath + ".broken")
                file.copyTo(brokenFile, true)
                SavagePluginX.INSTANCE.logger.log(Level.SEVERE, "Backing up broken file as ${brokenFile.path}")
            }
            SavagePluginX.INSTANCE.logger.log(Level.SEVERE, "Error with file: ${persistentContainer.name} \n $message")
            save(persistentContainer)
            persistentContainer
        }
    }

    fun save(persistentContainer: PersistentContainer)
}