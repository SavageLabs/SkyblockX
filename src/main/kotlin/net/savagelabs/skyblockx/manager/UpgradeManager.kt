package net.savagelabs.skyblockx.manager

import com.google.common.collect.ImmutableMap
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.exception.UpgradeException
import net.savagelabs.skyblockx.upgrade.Upgrade
import net.savagelabs.skyblockx.upgrade.impl.GeneratorUpgrade
import net.savagelabs.skyblockx.upgrade.impl.HomeUpgrade
import net.savagelabs.skyblockx.upgrade.impl.SizeUpgrade
import net.savagelabs.skyblockx.upgrade.impl.TeamUpgrade
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList

/**
 * This object is used to manage all incoming and outgoing upgrades
 * both internally and externally.
 */
object UpgradeManager {
    /**
     * [HashMap] map containing all upgrades.
     */
    private val cached: HashMap<String, Upgrade> = hashMapOf()

    /**
     * Register default upgrades.
     */
    internal fun defaults() {
        register(GeneratorUpgrade)
        register(HomeUpgrade)
        register(SizeUpgrade)
        register(TeamUpgrade)
    }

    /**
     * Register a new Upgrade by instance.
     *
     * @param upgrade [Upgrade] the new Upgrade instance to register.
     * @throws UpgradeException this exception is thrown if the Upgrade is already existent.
     */
    @Throws(UpgradeException::class)
    fun register(upgrade: Upgrade) = upgrade.run {
        // throw exception if an Upgrade already exists by this id
        if (cached.putIfAbsent(this.id, this) != null) {
            throw UpgradeException("The upgrade '$id' has already been registered")
        }

        // register the Upgrade's listener
        Bukkit.getPluginManager().registerEvents(upgrade.listener ?: return@run, SkyblockX.skyblockX)
    }

    /**
     * Unregister an existing Upgrade by it's id.
     *
     * @param id [String] the id that this specific Upgrade was registered to.
     * @throws UpgradeException this exception is thrown if the Upgrade doesn't exist.
     */
    @Throws(UpgradeException::class)
    fun unregister(id: String) = id.run {
        // throw exception if Upgrade is absent
        val value = cached.remove(this) ?: throw UpgradeException("The upgrade '$id' has not been registered")

        // unregister the Upgrade's listener
        HandlerList.unregisterAll(value.listener ?: return@run)
    }

    /**
     * Get a specific Upgrade by it's id.
     *
     * @param id [String] identifier of the Upgrade to fetch.
     * @return [Upgrade] - null if non-existent.
     */
    fun byId(id: String): Upgrade? = cached[id]

    /**
     * Get a copy of all upgrades in an immutable map.
     *
     * @return [ImmutableMap]
     */
    fun getAll() = ImmutableMap.copyOf(cached)
}