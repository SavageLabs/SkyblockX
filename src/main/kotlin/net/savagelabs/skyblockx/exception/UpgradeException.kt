package net.savagelabs.skyblockx.exception

/**
 * This exception is for Upgrade based failures such as registering and
 * unregistering upgrades via the [net.savagelabs.skyblockx.manager.UpgradeManager] object.
 */
class UpgradeException constructor(
        override val message: String
) : RuntimeException(message)