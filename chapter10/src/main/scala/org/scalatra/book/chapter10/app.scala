package org.scalatra.book.chapter10

import org.scalatra._
import org.scalatra.book.chapter10.ClimbingRoutesDAO._
import org.scalatra.scalate.ScalateSupport
import slick.driver.H2Driver.api._

import scalaz._, Scalaz._

class Chapter10App(db: Database) extends ScalatraServlet with ScalateSupport with FutureSupport {

  override protected implicit def executor = scala.concurrent.ExecutionContext.global

  println(new java.io.File( "." ).getCanonicalPath())

  before("/*") {
    contentType = "text/html"
  }

  before("/css/*") {
    contentType = "text/css"
  }

  get("/") {
    redirect("/areas")
  }

  // return all areas
  get("/areas") {
    new AsyncResult {
      val is = {

        db.run(allAreas) map { areas =>
          jade("areas.jade", "areas" -> areas)
        }

      }
    }
  }

  // create a new area
  post("/areas") {

    val name = params.get("name") getOrElse halt(BadRequest())
    val location = params.get("location") getOrElse halt(BadRequest())
    val latitude = params.getAs[Double]("latitude") getOrElse halt(BadRequest())
    val longitude = params.getAs[Double]("longitude") getOrElse halt(BadRequest())
    val description = params.get("description") getOrElse halt(BadRequest())

    db.run(createArea(name, location, latitude, longitude, description)) map { area =>
      Found(f"/areas/${area.id}")
    }

  }

  // return an area and their routes
  get("/areas/:areaId") {
    new AsyncResult {
      val is = {

        val areaId = params.getAs[Int]("areaId") getOrElse halt(BadRequest())

        db.run(findAreaWithRoutes(areaId).transactionally) map {
          case Some((area, routes)) => jade("area.jade", "area" -> area, "routes" -> routes)
          case None => NotFound()
        }

      }
    }
  }

  // update name and description of a route
  put("/routes/:routeId") {

    // validation
    def checkRouteName(s: String): ActionResult \/ Unit = {
      if (s.length < 250) ().right else BadRequest("Route name is too long.").left
    }

    // uses scalaz \/ for parameter validation
    for {
      routeId      <- params.getAs[Int]("routeId") \/> BadRequest()
      routeName    <- params.getAs[String]("routeName") \/> BadRequest()
      description  <- params.getAs[String]("description") \/> BadRequest()
      _            <- checkRouteName(routeName)
    } yield {
      db.run(updateRoute(routeId, routeName, description))
    }

  }

  // delete a route if it exists
  delete("/routes/:routeId") {

    val routeId = params.getAs[Int]("areaId") getOrElse halt(BadRequest())

    db.run(deleteRouteIfExists(routeId))

  }

}

