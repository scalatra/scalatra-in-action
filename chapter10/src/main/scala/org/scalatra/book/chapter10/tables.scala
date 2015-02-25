package org.scalatra.book.chapter10

import slick.driver.JdbcDriver.simple._

object Tables {

  class Areas(tag: Tag) extends Table[Area](tag, "AREAS") {
    def id            = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def name          = column[String]("NAME")
    def location      = column[String]("LOCATION")
    def latitude      = column[Double]("LATITUDE")
    def longitude     = column[Double]("LONGITUDE")
    def description   = column[String]("DESCRIPTION")

    def * = (id, name, location, longitude, latitude, description) <> (Area.tupled, Area.unapply)
  }
  val areas = TableQuery[Areas]

  class Routes(tag: Tag) extends Table[Route](tag, "ROUTES") {
    def id            = column[Int]("ID", O.PrimaryKey, O.AutoInc)
    def areaId        = column[Int]("AREAID")
    def mountainName  = column[Option[String]]("MOUNTAINNAME")
    def routeName     = column[String]("ROUTENAME")
    def description   = column[String]("DESCRIPTION")
    def latitude      = column[Double]("LATITUDE")
    def longitude     = column[Double]("LONGITUDE")

    def * = (id, areaId, mountainName, routeName, latitude, longitude, description) <> (Route.tupled, Route.unapply)

    def area = foreignKey("FK_ROUTE_AREA", areaId, areas)(_.id)
  }
  val routes = TableQuery[Routes]

}