package net.savagelabs.skyblockx.world

import com.fasterxml.jackson.annotation.JsonIgnore
import net.savagelabs.skyblockx.persist.Config
import org.bukkit.Bukkit
import org.bukkit.Location


data class Point(val x: Int, val z: Int) {

	@JsonIgnore
	fun getLocation(): Location {
		return Location(
			Bukkit.getWorld(Config.instance.skyblockWorldName),
			(Config.instance.islandMaxSizeInBlocks + Config.instance.islandPaddingSizeInBlocks + 1) * x.toDouble(),
			0.toDouble(),
			(Config.instance.islandMaxSizeInBlocks + Config.instance.islandPaddingSizeInBlocks + 1) * z.toDouble()
		)
	}

}


fun spiral(origin: Int): Point {
	var n = origin
	if (n == 0) return Point(0, 0)
	n--
	val r = kotlin.math.floor((kotlin.math.sqrt((n + 1).toDouble()) - 1) / 2) + 1
	val p = (8 * r * (r - 1)) / 2
	val en = r * 2
	val a = (1 + n - p) % (r * 8)
	var posX = 0
	var posY = 0
	when (kotlin.math.floor(a / (r * 2)).toInt()) {
		0 -> {
			posX = (a - r).toInt()
			posY = (-r).toInt()
		}
		1 -> {
			posX = r.toInt()
			posY = ((a % en) - r).toInt()
		}
		2 -> {
			posX = (r - (a % en)).toInt()
			posY = r.toInt()
		}
		3 -> {
			posX = (-r).toInt()
			posY = (r - (a % en)).toInt()
		}
	}
	return Point(posX, posY)
}


