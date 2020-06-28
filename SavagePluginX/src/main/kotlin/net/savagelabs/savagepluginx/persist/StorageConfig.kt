package net.savagelabs.savagepluginx.persist

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class StorageConfig : ConfigFile {


    override val name = "storage-config"

    val useMongoDB = false
    val mongoDBConnectionString = "mongodb://localhost:27017"



}