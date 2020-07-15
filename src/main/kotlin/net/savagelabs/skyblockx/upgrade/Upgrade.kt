package net.savagelabs.skyblockx.upgrade

import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.Island
import net.savagelabs.skyblockx.core.updateWorldBorder
import net.savagelabs.skyblockx.persist.Config


interface Upgrade {

	fun runUpgradeEffect(island: Island, level: Int)
}


object GeneratorUpgrade : Upgrade {
	override fun runUpgradeEffect(island: Island, level: Int) {
		island.upgrades[UpgradeType.GENERATOR] = level
	}
}

//class CoopUpgrade(
//    name: String,
//    item: SerializableItem
//) : Upgrade(
//    name,
//    item
//) {
//    override fun runUpgradeEffect(island: Island, level: Int) {
//        island.upgrades[UpgradeType.COOP_SIZE] = level
//        island.coopBoost = Config.instance.upgrades[UpgradeType.COOP_SIZE]?.get(level)?.toInt() ?: 0
//    }
//}

object BorderUpgrade : Upgrade {
	override fun runUpgradeEffect(island: Island, level: Int) {
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
	}

}

object HomeUpgrade : Upgrade {
	override fun runUpgradeEffect(island: Island, level: Int) {
		island.upgrades[UpgradeType.MAX_HOMES] = level
		island.homeBoost += Config.instance.upgrades[UpgradeType.MAX_HOMES]?.upgradeInfoPerLevel?.get(level)?.parameter?.toIntOrNull()
			?: run {
				SkyblockX.skyblockX.logger.info("Home Upgrade failed due to the param not being an integer")
				return
			}
	}
}

object TeamUpgrade : Upgrade {
	override fun runUpgradeEffect(island: Island, level: Int) {
		island.upgrades[UpgradeType.TEAM_SIZE] = level
		island.memberBoost += Config.instance.upgrades[UpgradeType.TEAM_SIZE]?.upgradeInfoPerLevel?.get(level)?.parameter?.toIntOrNull()
			?: run {
				SkyblockX.skyblockX.logger.info("Team Upgrade failed due to the param not being an integer")
				return
			}
	}
}