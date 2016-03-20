package org.scalatra.book.chapter10

import org.scalatra._
import org.scalatra.book.chapter10.ClimbingRoutesDAO._
import org.scalatra.scalate.ScalateSupport
import slick.driver.H2Driver.api._

import scala.concurrent.Future
import scalaz._, Scalaz._

class Chapter10App(db: Database) extends ScalatraServlet with ScalateSupport with ScalazSupport with FutureSupport {

  override protected implicit def executor = scala.concurrent.ExecutionContext.global

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

  // create a new area
  post("/areas") {
    new AsyncResult {
      val is = {

        // halt on an error
        val name = params.get("name") getOrElse halt(BadRequest())
        val location = params.get("location") getOrElse halt(BadRequest())
        val latitude = params.getAs[Double]("latitude") getOrElse halt(BadRequest())
        val longitude = params.getAs[Double]("longitude") getOrElse halt(BadRequest())
        val description = params.get("description") getOrElse halt(BadRequest())

        db.run(createArea(name, location, latitude, longitude, description)) map { area =>
          Found(f"/areas/${area.id}")
        }

      }
    }
  }

  // update name and description of a route
  put("/routes/:routeId") {
    new AsyncResult {
      val is = {

        // validation
        def checkRouteName(s: String): ActionResult \/ Unit = {
          if (s.length < 250) ().right else BadRequest("Route name is too long.").left
        }

        // uses scalaz \/ for parameter validation
        // being explicit about the types here

        val x: ActionResult \/ Future[ActionResult] = for {
          routeId      <- params.getAs[Int]("routeId") \/> BadRequest()
          routeName    <- params.getAs[String]("routeName") \/> BadRequest()
          description  <- params.getAs[String]("description") \/> BadRequest()
          _            <- checkRouteName(routeName)
        } yield {
          db.run(updateRoute(routeId, routeName, description)) map { c =>
            if (c == 1) Ok()
            else NotFound()
          }
        }

        // ActionResult \/ Future[ActionResult] => Future[ActionResult \/ ActionResult]
        // for sequence see scalaz.Traverse
        val y: Future[ActionResult \/ ActionResult] = x.sequence[Future, ActionResult]

        y

      }
    }
  }

  // delete a route if it exists
  delete("/routes/:routeId") {
    new AsyncResult {
      val is = {

        ((params.getAs[Int]("routeId") \/> BadRequest()) map { routeId =>

          db.run(deleteRouteIfExists(routeId))  // Future[ActionResult \/ ActionResult]
            .map(_.merge)                       // Future[ActionResult], for merge see scalaz.\/

        }).sequence[Future, ActionResult]       // ActionResult \/ Future[ActionResult] -> Future[ActionResult \/ ActionResult]

      }
    }
  }

}

