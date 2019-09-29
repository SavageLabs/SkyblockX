package net.savagellc.savageskyblock.gui

import org.bukkit.entity.HumanEntity

interface SkyblockGui {

    fun buildGui()

    fun showGui(humanEntity: HumanEntity)

}