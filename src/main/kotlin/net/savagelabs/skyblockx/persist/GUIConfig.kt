package net.savagelabs.skyblockx.persist

import com.cryptomorin.xseries.XMaterial
import com.fasterxml.jackson.annotation.JsonIgnore
import fr.minuskube.inv.content.SlotIterator
import net.savagelabs.savagepluginx.persist.container.ConfigContainer
import net.savagelabs.skyblockx.gui.*
import net.savagelabs.skyblockx.gui.menu.*
import net.savagelabs.skyblockx.gui.wrapper.GUICoordinate
import net.savagelabs.skyblockx.gui.wrapper.GUIItem
import net.savagelabs.skyblockx.gui.wrapper.MenuItem
import net.savagelabs.skyblockx.persist.data.IslandCreateInfo
import net.savagelabs.skyblockx.persist.data.SerializableItem
import net.savagelabs.worldborder.WorldBorderUtil

class GUIConfig(@JsonIgnore override val name: String = "GUIConfig") : ConfigContainer {

    companion object {
        lateinit var instance: GUIConfig
    }


    val islandMenuGUI = IslandMenuGUIConfig(
        "&a&lIsland Menu",
        SerializableItem(
            XMaterial.RED_STAINED_GLASS_PANE,
            "",
            listOf(),
            1
        ),
        5,
        listOf(
            MenuItem(
                SerializableItem(
                    XMaterial.GRASS_BLOCK,
                    "&aGo to your Island",
                    listOf("&e&l→&a Teleport to your Skyblock Island."),
                    1
                ),
                listOf(
                    "is go"
                ),
                GUICoordinate(4, 2)
            ),
            MenuItem(
                SerializableItem(
                    XMaterial.HOPPER,
                    "&aValue Breakdown",
                    listOf("&e&l→&a &aBreak down your island value."),
                    1
                ),
                listOf(
                    "is breakdown"
                ),
                GUICoordinate(4, 0)
            ),
            MenuItem(
                SerializableItem(
                    XMaterial.DIAMOND,
                    "&aIsland Leaderboard",
                    listOf("&e&l→&a &aView the best islands on the server."),
                    1
                ),
                listOf(
                    "is leaderboard"
                ),
                GUICoordinate(4, 1)
            ),
            MenuItem(
                SerializableItem(
                    XMaterial.GOLD_NUGGET,
                    "&eIsland Shop",
                    listOf("&e&l→&a &aBuy items from the island shop."),
                    1
                ),
                listOf(
                    "is shop"
                ),
                GUICoordinate(4, 3)
            ),
            MenuItem(
                SerializableItem(
                    XMaterial.ENDER_EYE,
                    "&aVisit Islands",
                    listOf("&e&l→&a &aCheck out other's islands!"),
                    1
                ),
                listOf(
                    "is visit"
                ),
                GUICoordinate(4, 4)
            ),
            MenuItem(
                SerializableItem(
                    XMaterial.BIRCH_SAPLING,
                    "&aIsland Biome",
                    listOf("&e&l→&a &aChange your island's biome."),
                    1
                ),
                listOf(
                    "is biome"
                ),
                GUICoordinate(2, 1)
            ),
            MenuItem(
                SerializableItem(
                    XMaterial.BEACON,
                    "&bChange Island Border",
                    listOf("&e&l→&a &aChange your border's color or turn it off."),
                    1
                ),
                listOf(
                    "is border"
                ),
                GUICoordinate(1, 2)
            ),
            MenuItem(
                SerializableItem(
                    XMaterial.RED_BED,
                    "&eManage Island Homes",
                    listOf("&e&l→&a &aView, Set, and Delete Island Homes"),
                    1
                ),
                listOf(
                    "is home"
                ),
                GUICoordinate(2, 3)
            ),
            MenuItem(
                SerializableItem(
                    XMaterial.NAME_TAG,
                    "&eManage Co-Op Players",
                    listOf("&e&l→&a &aInvite or Remove Co-Op Players"),
                    1
                ),
                listOf(
                    "is co-op"
                ),
                GUICoordinate(6, 1)
            ),
            MenuItem(
                SerializableItem(
                    XMaterial.ENDER_CHEST,
                    "&5View Island Chest",
                    listOf("&e&l→&a &aOpen the Island Chest."),
                    1
                ),
                listOf(
                    "is chest"
                ),
                GUICoordinate(7, 2)
            ),
            MenuItem(
                SerializableItem(
                    XMaterial.WRITABLE_BOOK,
                    "&eIsland Quests",
                    listOf("&e&l→&a &aManage Island Quests."),
                    1
                ),
                listOf(
                    "is quest"
                ),
                GUICoordinate(6, 3)
            )
        )
    )

    val coopMenu: CoopMenuConfig =
        CoopMenuConfig(
            "&aManage Co-Op Players",
            SerializableItem(
                XMaterial.BLACK_STAINED_GLASS_PANE,
                "",
                listOf(),
                1
            ),
            3,
            GUIItem(
                SerializableItem(
                    XMaterial.ARROW,
                    "&aNext Page →",
                    listOf(),
                    1,
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDllY2NjNWMxYzc5YWE3ODI2YTE1YTdmNWYxMmZiNDAzMjgxNTdjNTI0MjE2NGJhMmFlZjQ3ZTVkZTlhNWNmYyJ9fX0="
                ),
                GUICoordinate(6, 0)
            ),
            GUIItem(
                SerializableItem(
                    XMaterial.ARROW,
                    "&c← Previous Page",
                    listOf(),
                    1,
                    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY0Zjc3OWE4ZTNmZmEyMzExNDNmYTY5Yjk2YjE0ZWUzNWMxNmQ2NjllMTljNzVmZDFhN2RhNGJmMzA2YyJ9fX0="
                ),
                GUICoordinate(2, 0)
            ),
            9,
            GUICoordinate(0, 1),
            "&a{player}",
            listOf("&cClick to Remove Co-Op Status"),
            listOf(
                MenuItem(
                    SerializableItem(
                        XMaterial.NAME_TAG,
                        "&eCurrent Co-Op Players",
                        listOf("&aRemove Co-Op status from players."),
                        1,
                        skullTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZjYmFlNzI0NmNjMmM2ZTg4ODU4NzE5OGM3OTU5OTc5NjY2YjRmNWE0MDg4ZjI0ZTI2ZTA3NWYxNDBhZTZjMyJ9fX0="
                    ),
                    emptyList(),
                    GUICoordinate(4,0)
                ),
                MenuItem(
                    SerializableItem(
                        XMaterial.PAPER,
                        "&eCo-Op A Player",
                        listOf(
                            "&e&l→&a Click to view players you can add."
                        ),
                        1,
                        skullTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFkOGEzYTNiMzZhZGQ1ZDk1NDFhOGVjOTcwOTk2ZmJkY2RlYTk0MTRjZDc1NGM1MGU0OGU1ZDM0ZjFiZjYwYSJ9fX0="
                    ),
                    listOf(
                        "is coop"
                    ),
                    GUICoordinate(8, 0)
                ),
                MenuItem(
                    SerializableItem(
                        XMaterial.OAK_DOOR,
                        "&aBack to Main Menu",
                        listOf(
                            "&e&l←&a Go back to the main menu."
                        ),
                        1,
                        skullTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQ3MDdkYjQ2YTVhY2JmZWJmNjEyMzk1MzZkMjU2NDgxMzRiYjQzYjY1YzE2NzE2YmEzMjljNmRiZjQxMiJ9fX0="
                    ),
                    listOf(
                        "is menu"
                    ),
                    GUICoordinate(0,0)
                )
            ),
            "&a{player}",
            listOf("&e&l→&a Click to remove Co-Op permissions.")
        )


    val coopMenuInviteConfig = CoopMenuInviteConfig(
        "&aAdd a new Co-Op Player",
        SerializableItem(
            XMaterial.BLACK_STAINED_GLASS_PANE,
            "",
            listOf(),
            1
        ),
        6,
        SlotIterator.Type.HORIZONTAL,
        GUIItem(
            SerializableItem(
                XMaterial.ARROW,
                "&aNext Page →",
                listOf(),
                1,
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDllY2NjNWMxYzc5YWE3ODI2YTE1YTdmNWYxMmZiNDAzMjgxNTdjNTI0MjE2NGJhMmFlZjQ3ZTVkZTlhNWNmYyJ9fX0="
            ),
            GUICoordinate(6, 0)
        ),
        GUIItem(
            SerializableItem(
                XMaterial.ARROW,
                "&c← Previous Page",
                listOf(),
                1,
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY0Zjc3OWE4ZTNmZmEyMzExNDNmYTY5Yjk2YjE0ZWUzNWMxNmQ2NjllMTljNzVmZDFhN2RhNGJmMzA2YyJ9fX0="
            ),
            GUICoordinate(2, 0)
        ),
        45,
        GUICoordinate(0, 1),
        "&c{player}",
        listOf(
            "&e&l→ &aClick to Co-Op."
        ),
        listOf(
            MenuItem(
                SerializableItem(
                    XMaterial.NAME_TAG,
                    "&eAll Online Players",
                    listOf("&eGive people Co-Op Access."),
                    1,
                    skullTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZjYmFlNzI0NmNjMmM2ZTg4ODU4NzE5OGM3OTU5OTc5NjY2YjRmNWE0MDg4ZjI0ZTI2ZTA3NWYxNDBhZTZjMyJ9fX0="
                ),
                emptyList(),
                GUICoordinate(4,0)
            ),
            MenuItem(
                SerializableItem(
                    XMaterial.PAPER,
                    "&eManage Co-Op Players",
                    listOf(
                        "&e&l→&a Click to manage."
                    ),
                    1,
                    skullTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFkOGEzYTNiMzZhZGQ1ZDk1NDFhOGVjOTcwOTk2ZmJkY2RlYTk0MTRjZDc1NGM1MGU0OGU1ZDM0ZjFiZjYwYSJ9fX0="
                ),
                listOf(
                    "is coop manage"
                ),
                GUICoordinate(8, 0)
            ),
            MenuItem(
                SerializableItem(
                    XMaterial.OAK_DOOR,
                    "&aBack to Main Menu",
                    listOf(
                        "&e&l←&a Go back to the main menu."
                    ),
                    1,
                    skullTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQ3MDdkYjQ2YTVhY2JmZWJmNjEyMzk1MzZkMjU2NDgxMzRiYjQzYjY1YzE2NzE2YmEzMjljNmRiZjQxMiJ9fX0="
                ),
                listOf(
                    "is menu"
                ),
                GUICoordinate(0,0)
            )
        )

    )

    val borderMenuConfig = BorderMenuConfig(
        "&aChange Border Color",
        SerializableItem(
            XMaterial.BLACK_STAINED_GLASS_PANE,
            "",
            emptyList(),
            1
        ),
        3,
        hashMapOf(
            WorldBorderUtil.Color.BLUE to GUIItem(
                SerializableItem(
                    XMaterial.LIGHT_BLUE_STAINED_GLASS,
                    "&bBlue Border",
                    listOf(
                        "&e&l→&a Set your border color to blue"
                    ),
                    1,
                    skullTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjNmNWY2OWI5NGI2NGNkNTJhY2MyZTg3ZDg0NmY2MzUyYTRjYjA3MDU2YjE0N2UyNDhhZjZlZjlmMjc4ZWY4ZiJ9fX0="
                ),
                GUICoordinate(1, 1)
            ),
            WorldBorderUtil.Color.GREEN to GUIItem(
                SerializableItem(
                    XMaterial.LIME_STAINED_GLASS,
                    "&aGreen Border",
                    listOf(
                        "&e&l→&a Set your border color to blue"
                    ),
                    1,
                    skullTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWIyYmExNWNiYWNkNzZkOWU2NDFkZDM4NTk4MTZmMjIyYTgyYzljODhjYzUwMzVhNTBlNDcwNWJiZTczNTRlZiJ9fX0="
                ),
                GUICoordinate(3, 1)
            ),
            WorldBorderUtil.Color.RED to GUIItem(
                SerializableItem(
                    XMaterial.RED_STAINED_GLASS,
                    "&cRed Border",
                    listOf(
                        "&e&l→&a Set your border color to red"
                    ),
                    1,
                    skullTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjQ0OGE0YmFlMTY0MDJiYzk1ZmQyYzFlMWFlNmE2MzJjODQ3NWQ3MmJkZjk4NmQzNmYwYjc2YjFiNzA2NjYzYyJ9fX0="
                ),
                GUICoordinate(5, 1)
            ),
            WorldBorderUtil.Color.NONE to GUIItem(
                SerializableItem(
                    XMaterial.GLASS,
                    "&fNo Border",
                    listOf(
                        "&e&l→&a Turn your border off."
                    ),
                    1,
                    skullTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYThjODU2MzY2YzY0Nzc0YWY2MjJkZjkwY2ViMTNjYzkxNjcyNzk0ZTc0OWE2MmJkMDFjYjg3MmRhNzE2ZCJ9fX0="
                ),
                GUICoordinate(7, 1)
            )
        ),
        listOf(
            MenuItem(
                SerializableItem(
                    XMaterial.OAK_DOOR,
                    "&aBack to Main Menu",
                    listOf(
                        "&e&l←&a Go back to the main menu."
                    ),
                    1,
                    skullTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQ3MDdkYjQ2YTVhY2JmZWJmNjEyMzk1MzZkMjU2NDgxMzRiYjQzYjY1YzE2NzE2YmEzMjljNmRiZjQxMiJ9fX0="
                ),
                listOf(
                    "is menu"
                ),
                GUICoordinate(0,0)
            )
        )
    )

    val createGUIConfig = CreateMenuConfig(
        "&aCreate an Island",
        SerializableItem(
            XMaterial.BLACK_STAINED_GLASS_PANE,
            "",
            listOf(),
            1
        ),
        3,
        listOf(
            IslandCreateInfo(
                "normal",
                "skyblockx.islands.default",
                GUICoordinate(3,1),
                SerializableItem(
                    XMaterial.GRASS_BLOCK,
                    "&aBasic Island",
                    listOf("&aThis is the basic starter island", "&aComes with everything you need to get started."),
                    1
                ), "island.structure",
                "nether-island.structure"
            ),
            IslandCreateInfo(
                "bedrock",
                "skyblockx.islands.bedrock",
                GUICoordinate(6,1),
                SerializableItem(
                    XMaterial.BEDROCK,
                    "&aBedrock Island",
                    listOf("&aThis is the basic starter island", "&aComes with everything you need to get started."),
                    1
                ), "island.structure",
                "nether-island.structure"
            )
        ),
        emptyList()
    )


}