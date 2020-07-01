package net.savagelabs.savagepluginx.persist.container

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.File

interface PersistentContainer {

    val name: String

    @JsonIgnore
    fun getFile(): File
}