package org.scalatra.book.chapter10

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport

import slick.jdbc.JdbcBackend.Database

case class Chapter10App(db: Database, repo: ClimbingRoutesRepository) extends ScalatraServlet with ScalateSupport {

  before("/*") {
    contentType = "text/html"
  }

  get("/areas") {
    db withTransaction { implicit session =>
      val areas = repo.allAreas
      jade("areas.jade", "areas" -> areas)
    }
  }

  get("/areas/:areaId") {
    val areaId = params.as[Int]("areaId")
    db withTransaction { implicit session =>
      val (area, routes) = repo.areaWithRoutesByAreaId(areaId) getOrElse halt(404) // -> tx rolls back
      jade("area.jade", "area" -> area, "routes" -> routes)
    }
  }

  post("/areas") {
    db withTransaction { implicit session =>
      val name         = params.getAs[String]("name") getOrElse halt(400)
      val location     = params.getAs[String]("location") getOrElse halt(400)
      val latitude     = params.getAs[Double]("latitude") getOrElse halt(400)
      val longitude    = params.getAs[Double]("longitude") getOrElse halt(400)
      val description  = params.getAs[String]("description") getOrElse halt(400)

      repo.createArea(name, location, latitude, longitude, description)
    }
  }

  post("/areas/:areaId/routes") {
    db withTransaction { implicit session =>
      val areaId       = params.getAs[Int]("areaId") getOrElse halt(400)
      val routeName    = params.getAs[String]("routeName") getOrElse halt(400)
      val latitude     = params.getAs[Double]("latitude") getOrElse halt(400)
      val longitude    = params.getAs[Double]("longitude") getOrElse halt(400)
      val description  = params.getAs[String]("description") getOrElse halt(400)
      val mountainName = params.getAs[String]("mountainName")

      repo.createRoute(areaId, routeName, latitude, longitude, description, mountainName)
    }
  }

  put("/routes/:routeId") {
    db withTransaction { implicit session =>
      val routeId      = params.getAs[Int]("routeId") getOrElse halt(400)
      val routeName    = params.getAs[String]("routeName") getOrElse halt(400)
      val latitude     = params.getAs[Double]("latitude") getOrElse halt(400)
      val longitude    = params.getAs[Double]("longitude") getOrElse halt(400)
      val description  = params.getAs[String]("description") getOrElse halt(400)
      val mountainName = params.getAs[String]("mountainName")

      repo.updateRoute(routeId, routeName, latitude, longitude, description, mountainName)
    }
  }

  delete("/routes/:routeId") {
    db withTransaction { implicit session =>
      val routeId = params.getAs[Int]("areaId") getOrElse halt(400)
      repo.deleteRoute(routeId)
    }
  }


  //  get("/search") {
  //    val q = params("q")
  //    db withTransaction { implicit session =>
  //      <ul>{for (r <- repo.areasByName(q)) yield <li>{r}</li>}</ul>
  //    }
  //  }

}

