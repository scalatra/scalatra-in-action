package org.scalatra.book.chapter10

import slick.driver.H2Driver.api._
import Tables._

import scala.concurrent.ExecutionContext

object ClimbingRoutesRepository {

  def allAreas: DBIO[Seq[Area]] = areas.result

  // create an area and return its id
  def createArea(name: String, location: String, latitude: Double, longitude: Double, description: String): DBIO[Int] = {

    val areaInsertQuery = areas
      .map(r => (r.name, r.location, r.latitude, r.longitude, r.description))
      .returning(areas.map(_.id))

    areaInsertQuery += (name, location, latitude, longitude, description)

  }

  // update a route
  def updateRoute(routeId: Int, routeName: String, description: String): DBIO[Int] = {

    val routeUpdateQuery = routes
      .filter(_.id === routeId)
      .map(r => (r.routeName, r.description))

    routeUpdateQuery.update(routeName, description)

  }

  def deleteRoute(route: Route): DBIO[Int] = {
    // routes.filter(_.id === route.id).delete
    ???
  }

  def findRoute(routeId: Int): DBIO[Option[Route]] = {
    routes.filter(_.id === routeId).result.headOption
  }

  def areaWithRoutesByAreaId(areaId: Int)(implicit ec: ExecutionContext): DBIO[Option[(Area, Seq[Route])]] = {

    //    val joinQuery = for {
    //      a <- areas if a.id === areaId
    //      r <- routes if r.areaId === a.id
    //    } yield (a, r)


    //    for {
    //      res <- (areas.filter(_.id === areaId) joinLeft routes)
    //    } yield {
    //      res groupBy (_._1) map { case (a, xs) => (a, xs.flatMap(_._2)) }
    //    }


    // query a Seq[(Area, Option[Route])] UU (nested relations are not possible, need to be built on the query result)
    // query composition
    val areaById = areas filter(_.id === areaId)
    val joinQuery = areaById joinLeft routes on (_.id === _.areaId)  // on clause

    // aggregate the result
    joinQuery.result map { xs =>
      xs.groupBy(_._1).map { case (a, xs) => (a, xs.flatMap(_._2)) }.headOption
    }

  }

}
