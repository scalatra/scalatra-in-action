package org.scalatra.book.chapter03

import org.scalatra.ScalatraServlet
import javax.servlet.http.HttpServletRequest

class RecordStore extends ScalatraServlet {
  get("/artists/?") {
    <artists>${Artist.fetchAll().map(_.toXml)}</artists>
  }

  get("/artists/:name/info") {
    Artist.find(params("name")) match {
      case Some(artist) => artist.toXml
      case None => status = 404
    }
  }

  post("/artists/new") {
    val artist = parseArtist(request)
    Artist.save(artist)
    status = 201
    response.setHeader("Location", s"/artist/${artist.name}")
  }

  put("/artists/:name") {
    val artist = parseArtist(request)
    Artist.update(artist)
    status = 204
  }

  delete("/artists/:name") {
    val name = params("name")
    if (name == "Frank_Zappa")
      status = 405
    else {
      Artist.delete(name)
      status = 204
    }
  }

  /*
  head("/artists/:name/info") {
    status =
      if (Artist.exists(params("name").replace('_', ' '))) 200
      else 404
    contentType = "application/xml"
  }

  options("/artists/Frank_Zappa") {
    response.setHeader("Allow", "GET, HEAD")
  }

  /* Uncomment to forbid all options requests */
  options() {
    status = 403
  }
  */

  def parseArtist(implicit request: HttpServletRequest): Artist =
    Artist(
      name = Artist.fromParam(params("name")),
      nationality = params("nationality"),
      isActive = params("isActive").toBoolean
    )
}

