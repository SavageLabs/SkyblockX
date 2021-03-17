package net.savagelabs.skyblockx.manager

import net.savagelabs.skyblockx.exception.UpgradeException
import net.savagelabs.skyblockx.upgrade.UpgradeType
import net.savagelabs.skyblockx.upgrade.impl.*
import net.savagelabs.skyblockx.upgrade.listenTo
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * This object is used to manage all incoming and outgoing upgrades/upgrade types
 * both internally and externally.
 */
object UpgradeManager {
    /**
     * [HashMap] map containing all upgrades.
     */
    @PublishedApi internal val cached: HashMap<String, UpgradeType<Event>> = hashMapOf()

    /**
     * Register default upgrades.
     */
    internal fun defaults() {
        register(GeneratorUpgradeType)
        register(HomeUpgradeType)
        register(SizeUpgradeType)
        register(TeamUpgradeType)
        register(PlacementLimitUpgradeType)
    }

    /**
     * Unregister all upgrades.
     */
    internal fun unregisterAll() {
        val keys = cached.keys.toTypedArray()
        for (key in keys) this.unregister(key)
    }

    /**
     * Register a new Upgrade Type by instance.
     *
     * @param upgradeType [UpgradeType] the new Upgrade Type instance to register.
     * @throws UpgradeException this exception is thrown if the Upgrade Type is already existent or the event type is abstract.
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(UpgradeException::class)
    inline fun <reified Type : Event> register(upgradeType: UpgradeType<Type>) = upgradeType.run {
        // throw exception if an Upgrade already exists by this id
        if (cached.putIfAbsent(this.id, this as UpgradeType<Event>) != null) {
            throw UpgradeException("The upgrade '$id' has already been registered")
        }

        // make sure the event type is NOT abstract
        val isNameEvent = Type::class.java.simpleName == "Event"
        if (Type::class.isAbstract && !isNameEvent) {
            throw UpgradeException("The upgrade '$id' has specified an abstract event as Type, please fix")
        }

        // register the Upgrade's listener if it's not based off of "Event" because then we'll ignore
        if (!isNameEvent) {
            this.listener = listenTo<Type> { upgradeType.onEvent(this) }
        }
    }

    /**
     * Unregister an existing Upgrade Type by it's id.
     *
     * @param id [String] the id that this specific Upgrade Type was registered to.
     * @throws UpgradeException this exception is thrown if the Upgrade Type doesn't exist.
     */
    @Throws(UpgradeException::class)
    fun unregister(id: String) = id.run {
        // throw exception if Upgrade is absent
        val value = cached.remove(this) ?: throw UpgradeException("The upgrade type '$id' has not been registered")

        // unregister the Upgrade's listener
        HandlerList.unregisterAll(value.listener ?: return@run)
    }

    /**
     * Get a specific Upgrade Type by it's id.
     *
     * @param id [String] identifier of the Upgrade Type to fetch.
     * @return [UpgradeType] - null if non-existent.
     */
    fun byId(id: String): UpgradeType<Event>? = cached[id]

    /**
     * Get all upgrade types in a read only set.
     *
     * @return [Set]
     */
    fun getAll() = cached.values.toSet()
}