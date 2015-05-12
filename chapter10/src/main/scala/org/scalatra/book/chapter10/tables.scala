package org.scalatra.book.chapter10

import slick.driver.H2Driver.api._

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

  // table queries

  val areas = TableQuery[Areas]

  val routes = TableQuery[Routes]


  // add some useful queries to areas and routes queries

  implicit class AreasQueryExtensions(query: Query[Areas, Area, Seq]) {

    val insertTuple = query
      .map(r => (r.name, r.location, r.latitude, r.longitude, r.description))
      .returning(areas.map(_.id))
      .into {
      case ((name, location, latitude, longitude, description), id) =>
        Area(id, name, location, latitude, longitude, description)
    }

    val withRoutes = query joinLeft routes on (_.id === _.areaId)

    def byId(id: Int) = query.filter(_.id === id)

  }

  implicit class RoutesQueryExtensions(query: Query[Routes, Route, Seq]) {

    val lessRoutes = query.drop(3).take(10)

    val sortById = query.sortBy(_.id.desc)
    val sortByIdName = query.sortBy(r => (r.areaId.asc, r.routeName))

    val statistics = query
      .groupBy(_.areaId)
      .map { case (areaId, rs) =>
      (areaId,
        rs.length,
        rs.map(_.longitude).min,
        rs.map(_.longitude).max,
        rs.map(_.latitude).min,
        rs.map(_.latitude).max)
    }

    def byId(id: Int) = query.filter(_.id === id)
    def byName(name: String) = query.filter(_.routeName === name)

    def bySuffix(str: String) =
      query.filter(_.routeName.toLowerCase like f"%%${str.toLowerCase}")

    val distinctAreaIds = query.groupBy(_.areaId).map(_._1)

    val withAreas = query join areas on (_.areaId === _.id)

  }


}