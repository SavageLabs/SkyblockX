package net.savagelabs.skyblockx.persist

import net.savagelabs.savagepluginx.persist.parser.JSONJacksonParser
import net.savagelabs.skyblockx.SkyblockX
import net.savagelabs.skyblockx.core.IPlayer
import net.savagelabs.skyblockx.core.Island
import org.bson.Document
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.util.KMongoConfiguration

object MongoManager {

	var initialized = false

	lateinit var client: CoroutineClient
	lateinit var database: CoroutineDatabase

	const val PLAYERS_COLLECTION_NAME = "players"
	const val ISLANDS_COLLECTION_NAME = "islands"

	fun initialize() {
		val logger = SkyblockX.skyblockX.logger
		logger.info("Setting property for Class Mapping Service")
		System.setProperty(
			"org.litote.mongo.test.mapping.service",
			"org.litote.kmongo.jackson.JacksonClassMappingTypeService"
		)
		logger.info("Registering Jackson Serializer Module.")
		JSONJacksonParser.setupJsonJacksonParser(KMongoConfiguration.bsonMapper)
		logger.info("Initializing Database Connection")
		client = KMongo.createClient(Config.instance.mongoDbConnectionString).coroutine
		database = client.getDatabase(Config.instance.mongoDBDatabaseName)
		initialized = true
		logger.info("Successfully initialized")
	}


	suspend fun load(): Data {
		if (!initialized) initialize()

		val players = database.getCollection<IPlayer>(PLAYERS_COLLECTION_NAME).find().toList()
		val islands = database.getCollection<Island>(ISLANDS_COLLECTION_NAME).find().toList()
		val data = Data()
		players.forEach { player -> data.IPlayers[player.uuid] = player }
		var highestIslandIDFound = 0
		islands.forEach { island ->
			if (island.islandID > highestIslandIDFound)  highestIslandIDFound = island.islandID
			data.islands[island.islandID] = island
		}
		data.nextIslandID = highestIslandIDFound + 1
		return data
	}

	suspend fun save(data: Data) {

		val playerCollection = database.getCollection<IPlayer>(PLAYERS_COLLECTION_NAME)
		data.IPlayers.forEach { (uuid, iplayer) ->
			// Find and replace throws a thicc boi error in kmongo
//            playerCollection.findOneAndReplace(IPlayer::uuid eq uuid, iplayer)
			val foundPlayer = playerCollection.findOne(IPlayer::uuid eq uuid)
			if (foundPlayer != null) playerCollection.updateOne(
				IPlayer::uuid eq uuid,
				iplayer
			) else playerCollection.insertOne(iplayer)
		}

		val islandCollection = database.getCollection<Island>(ISLANDS_COLLECTION_NAME)
		data.islands.filter { (_, island) -> island.syncIsland }.forEach { (islandID, island) ->
//            islandCollection.findOneAndReplace(Island::islandID eq islandID, island)
			val foundIsland = islandCollection.findOne(Island::islandID eq islandID)
			if (foundIsland != null) islandCollection.updateOne(
				Island::islandID eq islandID,
				island
			) else islandCollection.insertOne(island)
		}

	}


}