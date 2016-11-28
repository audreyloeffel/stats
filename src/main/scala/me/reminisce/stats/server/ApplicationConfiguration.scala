package me.reminisce.stats.server

import scala.util.Properties._


object ApplicationConfiguration {

  val serverPort = envOrElse("STATS_PORT", "7777").toInt
  val hostName = envOrElse("STATS_HOST", "localhost")

  val mongoHost = envOrElse("MONGODB_HOST", hostName)
  val mongodbName = envOrElse("REMINISCE_STATS_MONGO_DB", "reminisce_stats")

}
