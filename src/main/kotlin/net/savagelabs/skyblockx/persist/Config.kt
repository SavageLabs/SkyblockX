package net.savagelabs.skyblockx.persist

import com.cryptomorin.xseries.XMaterial
import com.fasterxml.jackson.annotation.JsonIgnore
import net.savagelabs.savagepluginx.persist.container.ConfigContainer
import net.savagelabs.skyblockx.gui.wrapper.GUICoordinate
import net.savagelabs.skyblockx.gui.wrapper.GUIItem
import net.savagelabs.skyblockx.persist.data.SerializableItem
import net.savagelabs.skyblockx.persist.data.WeightedItem
import net.savagelabs.skyblockx.upgrade.Upgrade
import net.savagelabs.skyblockx.upgrade.UpgradeLevelInfo
import org.bukkit.Bukkit
import org.bukkit.block.Biome
import java.util.*


class Config(@JsonIgnore override val name: String = "config") :
	ConfigContainer {

	companion object {
		lateinit var instance: Config
	}


	var defaultWorld = Bukkit.getWorlds().first().name

	val useDatabase = false
	val mongoDbConnectionString = "mongodb://localhost:27017"
	val mongoDBDatabaseName = "SkyblockX"

	@JsonIgnore
	val skyblockPermissionPrefix = "skyblockx"

	var islandStartSizeInBlocks = 75
	var islandMaxSizeInBlocks = 500

	var islandPaddingSizeInBlocks = 50

	var upgrades = setOf(
		Upgrade(
			"GENERATOR_ONE",
			"GENERATOR",
			"",
			mapOf(
				1 to UpgradeLevelInfo(
					10000.0,
					"",
					GUIItem(
						SerializableItem(
							XMaterial.IRON_ORE,
							"&f&lGenerator Upgrade",
							listOf(
								"&7Cobblestone: 50%",
								"&fIron Ore: 10%",
								"&3Coal Ore: 40%",
								"",
								"&7Upgrade To Lv. 1",
								"&7Cost: &a&l$10,000"
							),
							1
						),
						GUICoordinate(1, 1)
					)
				),
				2 to UpgradeLevelInfo(
					25000.0,
					"",
					GUIItem(
						SerializableItem(
							XMaterial.IRON_ORE,
							"&f&lGenerator Upgrade",
							listOf(
								"&7Cobblestone: 30%",
								"&eGold Ore: 20%",
								"&fIron Ore: 20%",
								"&3Coal Ore: 20%",
								"&9Lapis Lazuli: 10%",
								"&bDiamond Ore: 10%",
								"",
								"&7Upgrade To Lv. 2",
								"&7Cost: &a&l$25,000"
							),
							2
						),
						GUICoordinate(1, 1)
					)
				),
				3 to UpgradeLevelInfo(
					50000.0,
					"",
					GUIItem(
						SerializableItem(
							XMaterial.EMERALD_ORE,
							"&f&lGenerator Upgrade",
							listOf(
								"&7Cobblestone: 10%",
								"&eGold Ore: 20%",
								"&fIron Ore: 20%",
								"&3Coal Ore: 10%",
								"&9Lapis Lazuli: 10%",
								"&eEmerald Ore: 20%",
								"&bDiamond Ore: 10%",
								"",
								"&7Upgrade To Lv. 3",
								"&7Cost: &a&l$50,000"
							),
							3
						),
						GUICoordinate(1, 1)
					)
				)
			),
			GUIItem(
				SerializableItem(
					XMaterial.DIAMOND_ORE,
					"&f&lMax Generator Level",
					listOf(
						"&7Max Upgrade Level Reached"
					),
					1
				),
				GUICoordinate(1, 1)
			)
		),
		Upgrade(
			"ISLAND_SIZE_ONE",
			"ISLAND_SIZE",
			"",
			mapOf(
				1 to UpgradeLevelInfo(
					10000.0,
					"100",
					GUIItem(
						SerializableItem(
							XMaterial.COAL_BLOCK,
							"&7Island Size Lv. 1",
							listOf(
								"&7150x150 Island",
								"",
								"&7Upgrade To Lv. 1",
								"&7Cost: &a&l$10,000"
							),
							1
						),
						GUICoordinate(2, 1)
					)
				),
				2 to UpgradeLevelInfo(
					250000.0,
					"150",
					GUIItem(
						SerializableItem(
							XMaterial.LAPIS_BLOCK,
							"&7Island Size Lv. 2",
							listOf(
								"&7150x150 Island",
								"",
								"&7Upgrade to Lv. 2",
								"&7Cost: &a&l$25,000"
							),
							2
						),
						GUICoordinate(2, 1)
					)
				),
				3 to UpgradeLevelInfo(
					500000.0,
					"250",
					GUIItem(
						SerializableItem(
							XMaterial.IRON_BLOCK,
							"&7Island Size Lv. 3",
							listOf(
								"&7250x250 Island",
								"",
								"&7Upgrade to Lv. 3",
								"&7Cost: &a&l$50,000"
							),
							3
						),
						GUICoordinate(2, 1)
					)
				),
				4 to UpgradeLevelInfo(
					1000000.0,
					"500",
					GUIItem(
						SerializableItem(
							XMaterial.DIAMOND_BLOCK,
							"&7Island Size Lv. 4",
							listOf(
								"&7500x500 Island",
								"",
								"&7Upgrade to Lv. 3",
								"&7Cost: &a&l$100,000"
							),
							4
						),
						GUICoordinate(2, 1)
					)
				)
			),
			GUIItem(
				SerializableItem(
					XMaterial.BEDROCK,
					"&f&lMax Border Level",
					listOf(
						"&7Max Upgrade Level Reached"
					),
					1
				),
				GUICoordinate(2, 1)
			)
		),
		Upgrade(
			"MAX_HOMES_ONE",
			"MAX_HOMES",
			"",
			mapOf(
				1 to UpgradeLevelInfo(
					10000.0,
					"1",
					GUIItem(
						SerializableItem(
							XMaterial.RED_BED,
							"&f&lIsland Home Upgrade Lv. 1",
							listOf(
								"&7Add one extra home.",
								"",
								"&7Upgrade to Lv 1",
								"&7Cost: &a&l$10,000"
							),
							1
						),
						GUICoordinate(3, 1)
					)
				),
				2 to UpgradeLevelInfo(
					25000.0,
					"2",
					GUIItem(
						SerializableItem(
							XMaterial.GREEN_BED,
							"&f&lIsland Home Upgrade Lv. 2",
							listOf(
								"&7Add two extra homes.",
								"",
								"&7Upgrade to Lv 2",
								"&7Cost: &a&l$25,000"
							),
							2
						),
						GUICoordinate(3, 1)
					)
				),
				3 to UpgradeLevelInfo(
					50000.0,
					"1",
					GUIItem(
						SerializableItem(
							XMaterial.BLUE_BED,
							"&f&lIsland Home Upgrade Lv. 3",
							listOf(
								"&7Add one extra home.",
								"",
								"&7Upgrade to Lv 3",
								"&7Cost: &a&l$50,000"
							),
							3
						),
						GUICoordinate(3, 1)
					)
				)
			),
			GUIItem(
				SerializableItem(
					XMaterial.CYAN_BED,
					"&f&lMax Home Level",
					listOf(
						"&7Max Upgrade Level Reached"
					),
					1
				),
				GUICoordinate(3, 1)
			)
		),
		Upgrade(
			"TEAM_SIZE_ONE",
			"TEAM_SIZE",
			"",
			mapOf(
				1 to UpgradeLevelInfo(
					10000.0,
					"1",
					GUIItem(
						SerializableItem(
							XMaterial.PLAYER_HEAD,
							"&f&lIsland Team Upgrade Lv. 1",
							listOf(
								"&7Add one extra member.",
								"",
								"&7Upgrade to Lv 1",
								"&7Cost: &a&l$10,000"
							),
							1
						),
						GUICoordinate(4, 1)
					)
				),
				2 to UpgradeLevelInfo(
					25000.0,
					"2",
					GUIItem(
						SerializableItem(
							XMaterial.PLAYER_HEAD,
							"&f&lIsland Team Upgrade Lv. 2",
							listOf(
								"&7Add two extra members.",
								"",
								"&7Upgrade to Lv 2",
								"&7Cost: &a&l$25,000"
							),
							2
						),
						GUICoordinate(4, 1)
					)
				),
				3 to UpgradeLevelInfo(
					50000.0,
					"1",
					GUIItem(
						SerializableItem(
							XMaterial.PLAYER_HEAD,
							"&f&lIsland Team Upgrade Lv. 3",
							listOf(
								"&7Add one extra member.",
								"",
								"&7Upgrade to Lv 3",
								"&7Cost: &a&l$50,000"
							),
							3
						),
						GUICoordinate(4, 1)
					)
				)
			),
			GUIItem(
				SerializableItem(
					XMaterial.PLAYER_HEAD,
					"&f&lMax Team Level",
					listOf(
						"&7Max Upgrade Level Reached"
					),
					1
				),
				GUICoordinate(4, 1)
			)
		),
		Upgrade(
			"PLACEMENT_LIMIT_SPAWNER",
			"PLACEMENT_LIMIT",
			"SPAWNER",
			mapOf(
				1 to UpgradeLevelInfo(
					15000.0,
					"5",
					GUIItem(
						SerializableItem(
							XMaterial.SPAWNER,
							"&f&lSpawner Upgrade",
							listOf(
								"&7Boost: &a5",
								"",
								"&7Upgrade To Lv. 1",
								"&7Cost: &a&l$15,000"
							),
							1
						),
						GUICoordinate(5, 1)
					)
				)
			),
			GUIItem(
				SerializableItem(
					XMaterial.SPAWNER,
					"&f&lMax Spawner Upgrade",
					listOf("&7Max Level Reached"), 1
				),
				GUICoordinate(5, 1)
			)
		)
	)

	var generatorUpgrades = hashMapOf(
		0 to arrayListOf(
			WeightedItem(XMaterial.COBBLESTONE, 7),
			WeightedItem(XMaterial.COAL_ORE, 2),
			WeightedItem(XMaterial.IRON_ORE, 1)
		),
		1 to arrayListOf(
			WeightedItem(XMaterial.COBBLESTONE, 5),
			WeightedItem(XMaterial.IRON_ORE, 1),
			WeightedItem(XMaterial.COAL_ORE, 4)
		),
		2 to arrayListOf(
			WeightedItem(XMaterial.COBBLESTONE, 2),
			WeightedItem(XMaterial.GOLD_ORE, 2),
			WeightedItem(XMaterial.IRON_ORE, 2),
			WeightedItem(XMaterial.COAL_ORE, 2),
			WeightedItem(XMaterial.EMERALD_ORE, 1),
			WeightedItem(XMaterial.LAPIS_ORE, 1),
			WeightedItem(XMaterial.DIAMOND_ORE, 1)
		),
		3 to arrayListOf(
			WeightedItem(XMaterial.COBBLESTONE, 1),
			WeightedItem(XMaterial.GOLD_ORE, 2),
			WeightedItem(XMaterial.IRON_ORE, 2),
			WeightedItem(XMaterial.COAL_ORE, 1),
			WeightedItem(XMaterial.LAPIS_ORE, 1),
			WeightedItem(XMaterial.EMERALD_ORE, 2),
			WeightedItem(XMaterial.DIAMOND_ORE, 1)
		)
	)

	var openIslandMenuOnBaseCommand = true

	var skyblockWorldName = "skyblockx"
	var skyblockWorldNameNether = "skyblockx_nether"
	var skyblockWorldNameEnd = "skyblockx_end"

	var preventFallingDeaths = true

	var useFallingDeathCommands = false

	var fallingDeathPreventionCommands = listOf(
		"eco take {player} 100"
	)

	var skyblockDeathTeleport = true

	var levelIncrementFactor = 25

	// Equals to 6 hours, 60 sec in 1 min, and 60 min in an hour, times 6.
	var islandResetCoolDownSeconds = 60L * 60L * 6L

	var islandDeleteClearInventory = true

	var islandDeleteClearEnderChest = true

	var chestRows = mapOf(1 to 3, 2 to 4, 3 to 5, 4 to 5, 5 to 6)

	var defaultMaxCoopPlayers = 3

	var defaultMaxIslandHomes = 3

	var helpGeneratorPageEntries = 10

	var defaultIslandMemberLimit = 3

	var islandOreGeneratorEnabled = true

	var skyblockWorldBiome = Biome.PLAINS

	var islandTopIslandCalculationSpeedIntervalMilis = 2000L

	var islandTopLineFormat = "&b{rank}&7. &7{name} &b(\${amount})&7."

	var useIslandTopHeadMessage = true

	var islandTopHeadMessage = "&b&lTop Islands &7&o((By block Value))"

	var useIslandTopHeaderBar = true

	var barLength = 50

	var islandTopbarElement = "&8&m="

	var islandTopTooltip = listOf(
		"&7Leader: &b{leader}",
		"&7Dirt: &b{DIRT}",
		"&7Diamond Block: &b{DIAMOND_BLOCK}",
		"&7Grass Block: &b{GRASS_BLOCK}",
		"&7Gold Block: &b{GOLD_BLOCK}",
		"&7Iron Block: &b{IRON_BLOCK}",
		"&7Lapis Block: &b{LAPIS_BLOCK}",
		"&7Coal Block: &b{COAL_BLOCK}"
	)

	var commandTopPageSize = 5

	var islandNameEnforceLength = true
	var islandNameMinLength = 4
	var islandNameMaxLength = 12
	var islandNameEnforceAlphaNumeric = true

	var disableMobDamageWhenIslandVisitor = true

	// amt x ticks/sec x sec/min: 15 min
	var islandTopCalcPeriodTicks = 15 * 20 * 60

	var _islandTopChunkLoadDelayComment =
		"50 miliseconds is equal to ONE tick. Make this higher if you are lagging, and lower if you want speed."
	var islandTopChunkLoadDelayInMiliseconds = 50L

	var islandTopManualCalcCooldownMiliseconds = 1 * 1000 * 60 * 5

	var autoCalcIslands = true

	var useShopGuiPlusHookOnCalcIfPresent = true

	var islandTopBroadcastMessage = true

	var islandTopBroadcastMessageStart = "&7Starting IslandTOP Calculation."

	var islandTopBroadcastMessageEnd = "&7Finished Calculation of %1\$s islands in %2\$s."

	// amt x ticks/sec x sec/min: 30 min
	var islandSaveTaskPeriodTicks = 30 * 20 * 60

	var islandSaveBroadcastMessage = true

	var islandSaveBroadcastMessageStart = "&7Saving Data.instance..."

	var islandSaveBroadcastMessageEnd = "&7Finished Saving data ( %1\$s )."

	var numberFormatLocale: Locale = Locale.US

	val showMemberManagerGUI = true

	var removeBlocksOnIslandDelete = false

	var blockPlacementLimit = mapOf(
		XMaterial.SPAWNER to 5
	)

	var chatFormat = "&bIsland Chat &r{player}: {message}"

	var chestShopMaximumAmount = 64
	var chestShopSignFormat = listOf("", "{player}", "Right Click", "")
	var chestShopUseHologram = true
	var chestShopHologramYOffset = 1.0
	var chestShopHologramRefreshRate = 20L
	var chestShopHologramViewDistance = 10.0
	var chestShopHologramShowPreviewItem = true
	var chestShopHologramFormatBuy = listOf(
			"&6{player}",
			"&f",
			"&7Material: &e{material}",
			"&7Price: &e\${price}",
			"&7Amount: &e{amount}",
			"&f",
			"&7&oRight click the sign to &6&l&oBUY"
	)
	var chestShopHologramFormatSell = listOf(
			"&6{player}",
			"&f",
			"&7Material: &e{material}",
			"&7Price: &e\${price}",
			"&7Amount: &e{amount}",
			"&f",
			"&7&oRight click the sign to &6&l&oSELL"
	)
}