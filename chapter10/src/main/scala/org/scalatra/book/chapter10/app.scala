package org.scalatra.book.chapter10

import org.scalatra._
import org.scalatra.scalate.ScalateSupport
import slick.dbio.DBIO
import scala.concurrent.{Future, ExecutionContext}

import slick.driver.H2Driver.api._

import ClimbingRoutesRepository._

case class Chapter10App(db: Database) extends ScalatraServlet with ScalateSupport with FutureSupport {

  override protected implicit def executor: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  before("/*") {
    contentType = "text/html"
  }

  get("/areas") {
    db.run(allAreas) map { areas =>
      jade("areas.jade", "areas" -> areas)
    }
  }

  post("/areas") {

    val name         = params.get("name") getOrElse halt(BadRequest())
    val location     = params.get("location") getOrElse halt(BadRequest())
    val latitude     = params.getAs[Double]("latitude") getOrElse halt(BadRequest())
    val longitude    = params.getAs[Double]("longitude") getOrElse halt(BadRequest())
    val description  = params.get("description") getOrElse halt(BadRequest())

    db.run(createArea(name, location, latitude, longitude, description)) map { areaId =>

      Found(f"/areas/$areaId")
      redirect(f"/areas/$areaId")

      // TOOD check if transaction is rolled back!
    }
  }


  get("/areas/:areaId") {
    val areaId = params.getAs[Int]("areaId") getOrElse halt(BadRequest())

    // naming
    db.run(areaWithRoutesByAreaId(areaId).transactionally) map {
      case Some((area, routes)) => jade("area.jade", "area" -> area, "routes" -> routes)
      case None => NotFound()  // does tx roll back?
    }
  }

  put("/routes/:routeId") {

    // partial update
    val routeId      = params.getAs[Int]("routeId") getOrElse halt(BadRequest())
    val routeName    = params.get("routeName") getOrElse halt(BadRequest())
    val description  = params.get("description") getOrElse halt(BadRequest())

    db.run(updateRoute(routeId, routeName, description).transactionally)

  }

  delete("/routes/:routeId") {

    val routeId = params.getAs[Int]("areaId") getOrElse halt(400)

    // composing actions
    val updateAction = findRoute(routeId) flatMap {
      case Some(route) => deleteRoute(route)
      case None => DBIO.successful(NotFound())  // ...
    }

    db.run(updateAction.transactionally)

  }

  // ----

  //  get("/search") {
  //    val q = params("q")
  //    db withTransaction { implicit session =>
  //      <ul>{for (r <- areasByName(q)) yield <li>{r}</li>}</ul>
  //    }
  //  }
}

