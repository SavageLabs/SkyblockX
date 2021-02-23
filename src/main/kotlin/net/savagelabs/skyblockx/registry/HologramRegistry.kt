package net.savagelabs.skyblockx.registry

import com.oop.inteliframework.hologram.Hologram
import com.oop.inteliframework.hologram.HologramController
import net.savagelabs.skyblockx.SkyblockX
import java.util.concurrent.Executors.newScheduledThreadPool

/**
 * This inline class contains the identifier bound to a hologram in the registry.
 */
inline class HologramIdentifier(val name: String)

/**
 * This object is the registry for present holograms.
 */
object HologramRegistry {
    /**
     * [HashMap] the map containing all present holograms by their identifiers.
     */
    private val registry: HashMap<HologramIdentifier, Hologram> by lazyOf(hashMapOf())

    /**
     * [HologramController] the controller for all holograms.
     */
    private val controller: HologramController by lazyOf(
        HologramController
            .builder()
            .plugin(SkyblockX.skyblockX)
            .executorService(newScheduledThreadPool(2))
            .build()
    )

    /**
     * Register a new hologram freely by identifier.
     *
     * @param identifier [HologramIdentifier] the hologram's accessibility id.
     * @param hologram   [Hologram] the hologram to be registered.
     * @throws IllegalStateException if a hologram by this identifier already exists.
     */
    @Throws(IllegalStateException::class)
    fun register(identifier: HologramIdentifier, hologram: Hologram) {
        if (registry.containsKey(identifier)) {
            throw IllegalStateException("Hologram with identifier $identifier already exists")
        }

        controller.registerHologram(hologram)
        registry[identifier] = hologram
    }

    /**
     * Unregister a hologram by it's identifier.
     *
     * @param identifier [HologramIdentifier] the hologram's accessibility id.
     * @return [Boolean] whether or not this removal was a success.
     */
    fun unregister(identifier: HologramIdentifier): Boolean {
        val removed = registry.remove(identifier) ?: return false
        removed.remove()
        return true
    }

    /**
     * Unregister all holograms from the registry.
     */
    fun unregisterAll() {
        val iterator = registry.values.iterator()
        while (iterator.hasNext()) {
            iterator.next().remove()
            iterator.remove()
        }
    }

    /**
     * Get a hologram by it's identifier.
     *
     * @param identifier [HologramIdentifier] the identifier that is bound to the corresponding hologram.
     * @return [Hologram] the registered hologram otherwise null.
     */
    fun byIdentifier(identifier: HologramIdentifier): Hologram? = registry[identifier]
}