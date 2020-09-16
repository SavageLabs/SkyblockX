package net.savagelabs.skyblockx.hooks

import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import net.savagelabs.skyblockx.core.IPlayer
import org.bukkit.Bukkit
import org.bukkit.plugin.RegisteredServiceProvider

object VaultHook : PluginHook {
	override val pluginName = "Vault"

	lateinit var vault: RegisteredServiceProvider<Economy>

	private fun getProvider(): Economy? {
		return if (isHooked()) vault.provider else null
	}

	fun getBalance(fPlayer: IPlayer, default: Double = 0.0): Double {
		val player = Bukkit.getOfflinePlayer(fPlayer.uuid)
		return getProvider()?.getBalance(player) ?: default
	}

	fun hasEnough(fPlayer: IPlayer, amount: Double): Boolean {
		return getBalance(fPlayer) >= amount
	}

	fun takeFrom(fPlayer: IPlayer, amount: Double): EconomyResponse? {
		val player = Bukkit.getOfflinePlayer(fPlayer.uuid)
		return getProvider()?.withdrawPlayer(player, amount)
	}

	fun giveTo(fPlayer: IPlayer, amount: Double): EconomyResponse? {
		val player = Bukkit.getOfflinePlayer(fPlayer.uuid)
		return getProvider()?.depositPlayer(player, amount)
	}

	fun format(amount: Double): String = getProvider()?.format(amount) ?: amount.toString()

	override fun load() {
		if (Bukkit.getPluginManager().getPlugin(pluginName) != null) {
			val serviceProvider = Bukkit.getServer().servicesManager.getRegistration(Economy::class.java)
			if (serviceProvider != null) vault = serviceProvider else println("&cVault was found, but economy is not enabled, please get an economy plugin like Essentials.")
		}
		if (isHooked()) println("Hooked into $pluginName.")
	}

	override fun isHooked(): Boolean {
		return this::vault.isInitialized
	}
}