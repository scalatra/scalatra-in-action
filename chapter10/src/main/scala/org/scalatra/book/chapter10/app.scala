package org.scalatra.book.chapter10

import org.scalatra._
import org.scalatra.scalate.ScalateSupport
import slick.dbio.DBIO
import scala.concurrent.{Future, ExecutionContext}

import slick.jdbc.JdbcBackend.Database

import scalaz._, Scalaz._

case class Chapter10App(db: Database, repo: ClimbingRoutesRepository) extends ScalatraServlet with ScalateSupport with FutureSupport {

  override protected implicit def executor: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  // be able to handle scalaz' \/
  override def renderPipeline: RenderPipeline = ({
    case \/-(r) => r
    case -\/(l) => l
  }: RenderPipeline) orElse super.renderPipeline


  before("/*") {
    contentType = "text/html"
  }

  get("/areas") {

    // run database action loading the areas
    val res: Future[Seq[Area]] = db.run(repo.allAreas)

    // map the result rendering a template
    // TODO request/response are bound to thread
    res map { areas =>
      jade("areas.jade", "areas" -> areas)
    }

  }

  // join
  //  get("/areas/:areaId") {
  //    val areaId = params.as[Int]("areaId")
  //    db withTransaction { implicit session =>
  //      val (area, routes) = repo.areaWithRoutesByAreaId(areaId) getOrElse halt(404) // -> tx rolls back
  //      jade("area.jade", "area" -> area, "routes" -> routes)
  //    }
  //  }

  post("/areas") {
    val name         = params.get("name") getOrElse halt(BadRequest())
    val location     = params.get("location") getOrElse halt(BadRequest())
    val latitude     = params.getAs[Double]("latitude") getOrElse halt(BadRequest())
    val longitude    = params.getAs[Double]("longitude") getOrElse halt(BadRequest())
    val description  = params.get("description") getOrElse halt(BadRequest())

    db.run(repo.createArea(name, location, latitude, longitude, description))
  }

  post("/areas/:areaId/routes") {

    // using scalaz \/
    for {
      areaId <- params.getAs[Int]("areaId") \/> BadRequest()
      routeName <- params.get("routeName") \/> BadRequest()
      latitude <- params.getAs[Double]("latitude") \/> BadRequest()
      longitude <- params.getAs[Double]("longitude") \/> BadRequest()
      description <- params.get("description") \/> BadRequest()
      mountainName = params.get("mountainName")
    } yield {
      db.run(repo.createRoute(areaId, routeName, latitude, longitude, description, mountainName))
    }

  }

  put("/routes/:routeId") {

    // partial update
    val routeId      = params.getAs[Int]("routeId") getOrElse halt(BadRequest())
    val routeName    = params.get("routeName") getOrElse halt(BadRequest())
    val description  = params.get("description") getOrElse halt(BadRequest())

    db.run(repo.updateRoute(routeId, routeName, description))

  }

  delete("/routes/:routeId") {

    val routeId = params.getAs[Int]("areaId") getOrElse halt(400)

    // composing actions
    val updateAction = repo.findRoute(routeId) flatMap {
      case Some(route) => repo.deleteRoute(route)
      case None => DBIO.successful(NotFound())  // ...
    }

    // run in a single session
    db.run(updateAction)

  }

  // ----

  //  get("/search") {
  //    val q = params("q")
  //    db withTransaction { implicit session =>
  //      <ul>{for (r <- repo.areasByName(q)) yield <li>{r}</li>}</ul>
  //    }
  //  }
}

