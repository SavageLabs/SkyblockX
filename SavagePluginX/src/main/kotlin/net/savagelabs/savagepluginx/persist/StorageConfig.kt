package net.savagelabs.savagepluginx.persist

class StorageConfig : ConfigContainer {


    override val name = "storage-config"

    val useMongoDB = false
    val mongoDBConnectionString = "mongodb://localhost:27017"



}