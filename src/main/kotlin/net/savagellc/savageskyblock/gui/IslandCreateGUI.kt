package net.savagellc.savageskyblock.gui

import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem
import net.savagellc.savageskyblock.persist.Config

class IslandCreateGUI(val islandSchematics: Map<String, SerializableItem>) :
    BaseGUI(Config.islandCreateGUITitle, Config.islandCreateGUIBackgroundItem, Config.islandCreateGUIRows)