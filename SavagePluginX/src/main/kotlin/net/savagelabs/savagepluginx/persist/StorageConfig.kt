package net.savagelabs.savagepluginx.persist

data class StorageConfig(val useMongoDB: Boolean = false, val mongoDBConnectionString: String = "mongodb://localhost:27017",
                    override val name: String = "storage-config"
) : ConfigContainer