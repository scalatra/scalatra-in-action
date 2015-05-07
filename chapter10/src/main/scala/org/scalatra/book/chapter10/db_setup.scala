package org.scalatra.book.chapter10

import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits._

import slick.jdbc.ResultSetAction
import slick.jdbc.GetResult._

import Tables._

object DbSetup {

  // test if a table exists
  def testTableExists(tableName: String): DBIO[Boolean] = {
    def localTables: DBIO[Vector[String]] =
      ResultSetAction[(String,String,String, String)](_.conn.getMetaData().getTables("", "", null, null)).map { ts =>
        ts.filter(_._4.toUpperCase == "TABLE").map(_._3).sorted
      }

    localTables map (_.exists(_.toLowerCase == tableName.toLowerCase))
  }

  val insertAreas: DBIO[Option[Int]] = {
    areas ++= Seq(
      Area(1, "Sicilia / Sicily", "Italy, Europe", 37.61423, 13.93616, "With more than 25000 square km Sicily is the largest island of the Mediterranean."),
      Area(2, "Southwest Absaroka Range", "Wyoming, United States, North America", 43.75461, 110.04421, "The Wyoming Absarokas are a vast, broad range, and their character and composition (although most of the range is volcanic in origin) vary in different sections of the range."),
      Area(3, "Alaska Coast Range Region", "Alaska, United States, North America", 58.76820, 133.94531, "The Alaska Coast Range extends from the British Columbia Coast Range of Canada in the South to the Saint Elias Range and Icefield Range in the North."),
      Area(4, "Yosemite National Park", "California, United States, North America", 37.51, 119.3404, "Yosemite National Park is a United States National Park spanning eastern portions of Tuolumne, Mariposa and Madera counties in the central eastern portion of the U.S. state of California."),
      Area(5, "Magic Wood", "Switzerland, Europe", 46.3347, 9.2612, "Hidden in the forest of the Avers valley there are some of the best boulder problems we have ever seen.")
    )
  }

  val insertRoutes: DBIO[Option[Int]] = {
    routes ++= Seq(
      Route(1, 1, Some("Rocca Busambra"), "Crest Traverse", 37.85590, 13.40040, "The crest traverse is a deserving, though somewhat strenuous and exposed route to the summit of the mountain."),
      Route(2, 2, Some("Citadel Mountain"), "Bobcat-Houlihan", 44.33550, 109.567, "For mountaineers who like solitude, adventure, and remote peaks that are rarely (if ever) climbed, Wyomings Absaroka Range is heaven."),
      Route(3, 3, Some("Alaska Coast Range Trail"), "Hike to Devils Punchbowl", 58.76820, 133.94531, "This is a great hike for fit hikers with 5-6 hours of time in Skagway."),
      Route(4, 3, Some("Alaska Coast Range Trail"), "Chilkoot Trail", 58.76820, 133.94531, "The trail is typically hiked from south to north, i.e. from Alaska into British Columbia. Skagway, Alaska is the logical jumping off point for hikers on the Chilkoot."),
      Route(5, 4, None, "Midnight Lightning", 37.7418, 119.602, "Midnight Lightning is a bouldering problem on the Columbia Boulder in Camp 4 of Yosemite National Park."),
      Route(6, 5, None, "Downunder", 46.3347, 9.2612, "Boulder, 7C+ at Sofasurfer"),
      Route(7, 5, None, "The Wizard", 46.3347, 9.2612, "Boulder, 7C at Beachbloc"),
      Route(8, 5, None, "Master of a cow", 46.3347, 9.2612, "Boulder, 7C+ at Man of a cow")
    )
  }

  // create schema if it not exists
  val createDatabase: DBIO[Unit] = {

    val createDatabase0: DBIO[Unit] = for {
      _ <- (routes.schema ++ areas.schema).create
      _ <- insertAreas
      _ <- insertRoutes
    } yield ()

    for {
      exists <- testTableExists("areas")
      _ <- if (!exists) createDatabase0 else DBIO.successful()
    } yield ()

  }

  // drop schema if it exists
  val dropDatabase: DBIO[Unit] = {
    testTableExists("areas") flatMap {
      case true => (routes.schema ++ areas.schema).drop
      case false => DBIO.successful()
    }
  }

}
