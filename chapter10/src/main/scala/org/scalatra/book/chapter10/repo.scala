package org.scalatra.book.chapter10

import slick.driver.JdbcDriver
import slick.jdbc.GetResult
import slick.jdbc.{StaticQuery => Q}
import Tables._

// TODO
// - fix update and delete

trait ClimbingRoutesRepositoryPlainSQL {

  // import query language: Session type
  val driver: JdbcDriver
  import driver.simple._
  import Q.interpolation

  // type conversion
  implicit val getRouteResult =
    GetResult(r => Route(r.nextInt, r.nextInt, r.nextStringOption, r.nextString, r.nextDouble, r.nextDouble, r.nextString))

  implicit val getAreaResult =
    GetResult(r => Area(r.<<, r.<<, r.<<, r.<<, r.<<, r.<<))

  // queries
  protected val areasQ = sql"SELECT * FROM areas".as[Area]

  protected val insertAreaQ =
    (name: String, location: String, latitude: Double, longitude: Double, description: String) =>
      sql"""INSERT INTO areas (name, location, latitude, longitude, description)
            VALUES FROM ($name, $location, $latitude, $longitude, $description)"""

  // public interface
  def allAreas(implicit s: Session): List[Area] = areasQ.list

  def createArea(name: String, location: String, latitude: Double, longitude: Double, description: String)(implicit s: Session): Unit = {
    insertAreaQ(name, location, latitude, longitude, description)
  }

}

trait ClimbingRoutesRepositoryQL {

  // import query language
  val driver: JdbcDriver
  import driver.simple._

  // queries
  protected val routeInsertQ = routes
    .map(r => (r.areaId, r.routeName, r.latitude, r.longitude, r.description, r.mountainName))
    .returning(routes.map(_.id))

  protected val routeUpdateQ = Compiled { (routeId: Column[Int]) =>
    routes
      .filter(_.id === routeId)
      .map(r => (r.routeName, r.latitude, r.longitude, r.description, r.mountainName))
  }

  protected val areasWithRoutesByAreaIdQ = Compiled { (areaId: Column[Int]) =>
    for {
      a <- areas if a.id === areaId
      r <- routes if r.areaId === a.id
    } yield (a, r)
    // areaByIdQ(areaId)
  }

  // public interface
  def createRoute(areaId: Int, routeName: String, latitude: Double, longitude: Double,
                  description: String, mountainName: Option[String] = None)(implicit s: Session): Int = {
    routeInsertQ += (areaId, routeName, latitude, longitude, description, mountainName)
  }

  def updateRoute(routeId: Int, routeName: String, latitude: Double, longitude: Double,
                  description: String, mountainName: Option[String] = None)(implicit s: Session): Int = {
    // routeUpdateQ.update(routeName, latitude, longitude, description, mountainName)
    1
  }

  def deleteRoute(routeId: Int)(implicit s: Session): Boolean = {
    // routes.filter(_.id === routeId).delete
    true
  }

  def areaWithRoutesByAreaId(areaId: Int)(implicit s: Session): Option[(Area, Seq[Route])] = {
    val res: List[(Area, Route)] = areasWithRoutesByAreaIdQ(areaId).list
    res.groupBy(_._1).map { case (a, xs) => (a, xs.map(_._2)) }.headOption
  }

}

class ClimbingRoutesRepository(val driver: JdbcDriver)
  extends ClimbingRoutesRepositoryPlainSQL
  with ClimbingRoutesRepositoryQL