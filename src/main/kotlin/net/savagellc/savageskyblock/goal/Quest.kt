package net.savagellc.savageskyblock.goal

import net.prosavage.baseplugin.serializer.commonobjects.SerializableItem

data class Quest(val name: String, val guiDisplayItem: SerializableItem, val guiDisplayIndex: Int, val type: QuestGoal, val goalParameter: String, var amountCompleted: Int, val amountTillComplete: Int, val commandsToExecuteOnCompletion: List<String>) {





}