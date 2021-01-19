package net.savagelabs.skyblockx.manager

import net.savagelabs.skyblockx.exception.UpgradeException
import net.savagelabs.skyblockx.upgrade.Upgrade
import net.savagelabs.skyblockx.upgrade.impl.GeneratorUpgrade
import net.savagelabs.skyblockx.upgrade.impl.HomeUpgrade
import net.savagelabs.skyblockx.upgrade.impl.SizeUpgrade
import net.savagelabs.skyblockx.upgrade.impl.TeamUpgrade
import net.savagelabs.skyblockx.upgrade.listenTo
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * This object is used to manage all incoming and outgoing upgrades
 * both internally and externally.
 */
object UpgradeManager {
    /**
     * [HashMap] map containing all upgrades.
     */
    @PublishedApi internal val cached: HashMap<String, Upgrade<Event>> = hashMapOf()

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
     * Unregister all upgrades.
     */
    internal fun unregisterAll() {
        val keys = cached.keys.toTypedArray()
        for (key in keys) this.unregister(key)
    }

    /**
     * Register a new Upgrade by instance.
     *
     * @param upgrade [Upgrade] the new Upgrade instance to register.
     * @throws UpgradeException this exception is thrown if the Upgrade is already existent or the event type is abstract.
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(UpgradeException::class)
    inline fun <reified Type : Event> register(upgrade: Upgrade<Type>) = upgrade.run {
        // throw exception if an Upgrade already exists by this id
        if (cached.putIfAbsent(this.id, this as Upgrade<Event>) != null) {
            throw UpgradeException("The upgrade '$id' has already been registered")
        }

        // make sure the event type is NOT abstract
        val isNameEvent = Type::class.java.simpleName == "Event"
        if (Type::class.isAbstract && !isNameEvent) {
            throw UpgradeException("The upgrade '$id' has specified an abstract event as Type, please fix")
        }

        // register the Upgrade's listener if it's not based off of "Event" because then we'll ignore
        if (!isNameEvent) {
            this.listener = listenTo<Type> { upgrade.onEvent(this) }
        }
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
    fun byId(id: String): Upgrade<Event>? = cached[id]

    /**
     * Get all upgrades in a read only set.
     *
     * @return [Set]
     */
    fun getAll() = cached.values.toSet()
}