package org.scalatra.book.chapter10

import org.scalatra.{ActionResult, NotFound, Ok}
import slick.dbio.DBIO
import slick.driver.H2Driver.api._
import Tables._

import scala.concurrent.ExecutionContext

import scalaz._, Scalaz._

object ClimbingRoutesDAO {

  def allAreas: DBIO[Seq[Area]] = areas.result

  // create an area and return it
  def createArea(name: String, location: String, latitude: Double, longitude: Double, description: String): DBIO[Area] = {

    val insertQuery = areas
      .map(r => (r.name, r.location, r.latitude, r.longitude, r.description))
      .returning(areas.map(_.id))
      .into {
        case ((name, location, latitude, longitude, description), id) =>
          Area(id, name, location, latitude, longitude, description)
      }

    insertQuery += (name, location, latitude, longitude, description)

  }

  def findRoute(routeId: Int): DBIO[Option[Route]] = {

    routes.byId(routeId).result.headOption

  }

  def findAreaWithRoutes(areaId: Int)(implicit ec: ExecutionContext): DBIO[Option[(Area, Seq[Route])]] = {

    // compose a query
    val query = areas.byId(areaId).withRoutes

    // aggregate the result
    query.result map { xs =>
      xs.groupBy(_._1).map { case (a, xs) => (a, xs.flatMap(_._2)) }.headOption
    }

  }

  def updateRoute(routeId: Int, routeName: String, description: String): DBIO[Int] = {

    routes.byId(routeId).map(r => (r.routeName, r.description)).update(routeName, description)

  }

  def deleteRoute(route: Route): DBIO[Int] = {

    routes.byId(route.id).delete

  }

  // find a route, then delete it, if it does not exist, return a NotFound
  def deleteRouteIfExists(routeId: Int)(implicit ec: ExecutionContext): DBIO[ActionResult \/ ActionResult] = {

    findRoute(routeId) flatMap {
      case Some(route) => deleteRoute(route) map (x => Ok().right)
      case None => DBIO.successful(NotFound().left)
    } transactionally

  }

}
