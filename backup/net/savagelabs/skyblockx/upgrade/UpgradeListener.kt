package net.savagelabs.skyblockx.upgrade

import net.savagelabs.skyblockx.SkyblockX
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.EventExecutor

/**
 * This event executor & listener is used to register Upgrade events fast and easy.
 */
@PublishedApi
internal class UpgradeListener constructor(
    private val listener: Event.() -> Unit
) : EventExecutor, Listener {
    override fun execute(fromListener: Listener, event: Event) = this.listener(event)
}

/**
 * Listen to an event by it's own event executor and listener.
 *
 * @param priority [EventPriority] the priority this event should be set to.
 * @param onExecution [Unit] this block will be executed during the event's call.
 * @return [UpgradeListener]
 */
@Suppress("UNCHECKED_CAST")
@PublishedApi
internal inline fun <reified Type : Event> listenTo(
    priority: EventPriority = EventPriority.NORMAL,
    noinline onExecution: Type.() -> Unit
) = UpgradeListener(onExecution as Event.() -> Unit).apply {
    Bukkit.getPluginManager().registerEvent(Type::class.java, this, priority, this, SkyblockX.skyblockX)
}