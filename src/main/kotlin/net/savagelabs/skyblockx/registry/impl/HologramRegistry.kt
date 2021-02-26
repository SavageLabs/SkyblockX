package net.savagelabs.skyblockx.registry.impl

import com.oop.inteliframework.hologram.Hologram
import com.oop.inteliframework.hologram.HologramController
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.registry.Identifier
import net.savagelabs.skyblockx.registry.Registry
import java.util.concurrent.Executors.newScheduledThreadPool

/**
 * This implementation is the [Registry] for present holograms.
 */
object HologramRegistry : Registry<Identifier, Hologram>() {
    /**
     * [String] the name of this registry.
     */
    override val name: String = "Hologram"

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
     * @param identifier [Identifier] the hologram's accessibility id.
     * @param value      [Hologram] the hologram to be registered.
     * @throws Registry.RegistryException if a hologram by this identifier already exists or the identifier and/or hologram is null.
     */
    @Throws(RegistryException::class)
    override fun register(identifier: Identifier, value: Hologram) {
        super.register(identifier, value)
        controller.registerHologram(value)
    }

    /**
     * Unregister a hologram by it's identifier.
     *
     * @param identifier [Identifier] the hologram's accessibility id.
     * @return [Boolean] whether or not this removal was a success.
     */
    override fun unregister(identifier: Identifier): Hologram? =
            super.unregister(identifier)?.also(Hologram::remove)
}