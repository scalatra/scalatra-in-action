package com.constructiveproof.hackertracker

import com.constructiveproof.hackertracker.models.Hacker
import com.constructiveproof.hackertracker.stacks.ApiStack
import org.scalatra.BadRequest
import org.scalatra.swagger._
import org.scalatra.swagger.Parameter
import scala.Some

class ApiController()(implicit val swagger: Swagger) extends ApiStack with SwaggerSupport {

  before() {
    contentType = formats("json")
    validateRequest()
  }

  // Identifies the controller to swagger
  override protected val applicationName = Some("hackers")
  protected val applicationDescription =
    """The Hacker Tracker API. Exposes operations for adding hackers and retrieving lists of hackers."""

  val listHackers = (apiOperation[List[Hacker]]("listHackers")
    summary("Show all hackers")
    notes("Shows all available hackers."))

  val createHacker = (apiOperation[Hacker]("createHacker")
    summary("Create a new hacker")
    notes("firstname, lastname, motto, and year of birth are required")
    parameters(
      Parameter("firstname", DataType.String, Some("The hacker's first name"), None, ParamType.Body, required = true),
      Parameter("lastname", DataType.String, Some("The hacker's last name"), None, ParamType.Body, required = true),
      Parameter("motto", DataType.String, Some("A phrase associated with this hacker"), None, ParamType.Body, required = true),
      Parameter("birthyear", DataType.Int, Some("A four-digit number, the year that the user was born in"),
        Some("A four-digit number"), ParamType.Body, required = true))
    )

  val getHacker = (apiOperation[Hacker]("getHacker")
    summary("Retrieve a single hacker by id")
    notes("Foo")
    parameters(
      Parameter("id", DataType.Int, Some("The hacker's database id"), None, ParamType.Path, required = true)
    ))


  /**
   * List all hackers.
   */
  get("/", operation(listHackers)) {
    Hacker.all.toList
  }

  /**
   * Retrieve a specific hacker.
   */
  get("/:id", operation(getHacker)) {
    val id = params.getAs[Int]("id").getOrElse(0)
    Hacker.get(id)
  }

  /**
   * Create a new hacker in the database.
   */
  post("/", operation(createHacker)) {
    val firstName = params("firstname")
    val lastName = params("lastname")
    val motto = params("motto")
    val birthYear = params.getAs[Int]("birthyear").getOrElse(
      halt(BadRequest("Please provide a year of birth.")))

    val hacker = new Hacker(0, firstName, lastName, motto, birthYear)
    if(Hacker.create(hacker)) {
      hacker
    }
  }

  delete("/:id") {
    val id = params.getAs[Long]("id").getOrElse(
      halt(BadRequest("Please provide an id to destroy"))
    )
    Hacker.destroy(id)
  }

}
