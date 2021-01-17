package net.savagelabs.skyblockx.upgrade

import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.updateWorldBorder
import net.savagelabs.skyblockx.event.IslandUpgradeEvent
import net.savagelabs.skyblockx.hooks.VaultHook
import net.savagelabs.skyblockx.persist.Config
import org.bukkit.Bukkit

interface Upgrade {
	fun runUpgradeEffect(upgradingPlayer: IPlayer, island: Island, level: Int)

	fun getPrice(type: UpgradeType, level: Int): Double? {
		return Config.instance.upgrades[type]?.upgradeInfoPerLevel?.get(level)?.price
	}
}


object GeneratorUpgrade : Upgrade {
	override fun runUpgradeEffect(upgradingPlayer: IPlayer, island: Island, level: Int) {
		if (!upgradingPlayer.takeMoney(getPrice(UpgradeType.GENERATOR, level)!!)) return
		
		island.upgrades[UpgradeType.GENERATOR] = level
		Bukkit.getPluginManager().callEvent(IslandUpgradeEvent(island, UpgradeType.GENERATOR))
	}
}

object BorderUpgrade : Upgrade {
	override fun runUpgradeEffect(upgradingPlayer: IPlayer, island: Island, level: Int) {
		if (!upgradingPlayer.takeMoney(getPrice(UpgradeType.BORDER, level)!!)) return

		island.upgrades[UpgradeType.BORDER] = level
		island.islandSize =
			Config.instance.upgrades[UpgradeType.BORDER]?.upgradeInfoPerLevel?.get(level)?.parameter?.toIntOrNull()
				?: run {
					SkyblockX.skyblockX.logger.info("Border Upgrade failed due to the param not being an integer")
					return
				}
		island.getIslandMembers().forEach { iPlayer ->
			val player = iPlayer.getPlayer()
			if (player != null) {
				updateWorldBorder(player, player.location, 10L)
			}
		}

		Bukkit.getPluginManager().callEvent(IslandUpgradeEvent(island, UpgradeType.BORDER))
	}
}

object HomeUpgrade : Upgrade {
	override fun runUpgradeEffect(upgradingPlayer: IPlayer, island: Island, level: Int) {
		if (!upgradingPlayer.takeMoney(getPrice(UpgradeType.MAX_HOMES, level)!!)) return

		island.upgrades[UpgradeType.MAX_HOMES] = level
		island.homeBoost += Config.instance.upgrades[UpgradeType.MAX_HOMES]?.upgradeInfoPerLevel?.get(level)?.parameter?.toIntOrNull()
			?: run {
				SkyblockX.skyblockX.logger.info("Home Upgrade failed due to the param not being an integer")
				return
			}

		Bukkit.getPluginManager().callEvent(IslandUpgradeEvent(island, UpgradeType.MAX_HOMES))
	}
}

object TeamUpgrade : Upgrade {
	override fun runUpgradeEffect(upgradingPlayer: IPlayer, island: Island, level: Int) {
		if (!upgradingPlayer.takeMoney(getPrice(UpgradeType.TEAM_SIZE, level)!!)) return

		island.upgrades[UpgradeType.TEAM_SIZE] = level
		island.memberBoost += Config.instance.upgrades[UpgradeType.TEAM_SIZE]?.upgradeInfoPerLevel?.get(level)?.parameter?.toIntOrNull()
			?: run {
				SkyblockX.skyblockX.logger.info("Team Upgrade failed due to the param not being an integer")
				return
			}

		Bukkit.getPluginManager().callEvent(IslandUpgradeEvent(island, UpgradeType.TEAM_SIZE))
	}
}