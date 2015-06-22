package org.scalatra.book.chapter03

import org.scalatra._
import javax.servlet.http.HttpServletRequest
import java.io.File

class RecordStore(downloadPath: String) extends ScalatraServlet {
  get("/artists/?") {
    <artists>{Artist.fetchAll().map(_.toXml)}</artists>
  }

  get("/artists/:name/info.?:format?") {
    Artist.find(params("name")) match {
      case Some(artist) =>
        params.get("format") match {
          case Some("json") => artist.toJson
          case _ => artist.toXml
    	  }
      case None => NotFound()
    }
  }

  get("/artists/:name/info.?:format?") {
    Artist.find(params("name")) match {
      case Some(artist) =>
        params.get("format") match {
          case Some("json") => artist.toJson
          case _ => artist.toXml
    	  }
      case None => NotFound()
    }
  }

  post("/artists/new") {
    val artist = parseArtist(request)
    Artist.save(artist)
    val location = s"/artists/${artist.name}"
    Created(artist, headers = Map("Location" -> location))
  }

  put("/artists/:name") {
    val artist = parseArtist(request)
    Artist.update(artist)
    NoContent()
  }

  delete("/artists/:name") {
    val name = params("name")
    if (name == "Frank Zappa")
      MethodNotAllowed()
    else if (Artist.delete(name).isDefined)
      NoContent()
    else
      NotFound()
  }

  head("/artists/:name/info") {
    contentType = "application/xml"
    if (Artist.exists(params("name").replace('_', ' '))) Ok()
    else NotFound()
  }

  options("/artists/Frank_Zappa") {
    response.setHeader("Allow", "GET, HEAD")
  }

  /* Uncomment to forbid all options requests */
  options() {
    Forbidden()
  }

  get("/downloads/*") {
    val path = params("splat")
    new File(downloadPath, path)
  }

  get("""/best-of/(\d{4})""".r) {
    val year = params("captures")
    // load albums by year
  }

  get("""/best-of/(\d{3}0)s""".r) {
    val decade = params("captures")
    // load albums by decade
  }

  get("/listen/*".r, isMobile(request)) {
    // stream mobile
  }

  get("/listen/*".r, !isMobile(request)) {
    // stream desktop
  }

  get("/artists/:name/albums/:album") {
    Albums.findByName(artist = params("name"), name = params("album"))
  }

  get("/artists/The_:name/*") {
    redirect("/artists/%s/%s".format(params("name"), params("splat")))
  }

  def isMobile(request: HttpServletRequest): Boolean = {
    val lower = request.getHeader("User-Agent").toLowerCase
    lower.contains("android") || lower.contains("iphone")
  }

  def parseArtist(implicit request: HttpServletRequest): Artist =
    Artist(
      name = Artist.fromParam(params("name")),
      nationality = params("nationality"),
      isActive = params("isActive").toBoolean
    )
}

