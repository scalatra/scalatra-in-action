package org.scalatra.book.chapter05.json

import org.scalatra.{ScalatraBase, ScalatraServlet}
import org.scalatra.json.JacksonJsonSupport
import org.json4s.{CustomSerializer, DefaultFormats}
import org.json4s.JsonAST.{JInt, JArray, JString, JObject}
import scala.collection.SortedSet

/**
 * The actual application, mixes in the routes trait.
 */
class LibraryApp extends ScalatraServlet with LibraryRoutes

/**
 * The module which contains the library routes, implemented as trait.
 */
trait LibraryRoutes extends ScalatraBase with JacksonJsonSupport {

  implicit val jsonFormats = DefaultFormats

  post("/api/v1/accounts") {
    println(parsedBody)
    parsedBody match {
      case JObject(("primaryEmail", JString(email)) ::
        ("pinCode", JString(pin)) ::
        ("address", JObject(("street", JString(street)) ::
          ("city", JString(city)) ::
          ("country", JString(country)) :: Nil)) :: Nil) =>

        val account = Library.createAccount(pin, email, Address(street, city, country))

        JString(account.id)

      case _ => halt(500, "")
    }
  }

  get("/api/v1/accounts") {
    val accountIds: List[JString] = Library.accounts.map(a => JString(a.id))
    accountIds
  }

  post("/api/v1/accounts/:id/emails") {
    parsedBody match {
      case JString(email) =>

        // lookup account
        val account = Library.findAccount(params("id")).getOrElse(halt(404))

        // additional email validation can be done here (also see section about commands)

        // add email address to account, if not already exists
        val updatedAccount = account.copy(emails = account.emails + email)
        Library.updateAccount(updatedAccount)

      case _ => halt(404)
    }
  }

  get("/api/v1/accounts/:id") {
    val id = params("id")

    Library.findAccount(id) match {
      case Some(account) =>
        JObject(
          "id" -> JString(account.id),
          "primaryEmail" -> JString(account.primaryEmail),
          "fallbackEmails" -> JArray(account.emails.map(JString(_)).toList),
          "address" -> JObject(
            "street" -> JString(account.address.street),
            "city" -> JString(account.address.city),
            "country" -> JString(account.address.country)))

      case _ => halt(404)
    }
  }

  import scala.xml._

  get("/scala-books") {
    val books = List(
      Map("title" -> "Programming In Scala, 2nd edition",
        "publisher" -> "Artima",
        "year" -> 2011),
      Map("title" -> "Scala in Depth",
        "publisher" -> "Manning",
        "year" -> 2012),
      Map("title" -> "Scala in Action",
        "publisher" -> "Manning",
        "year" -> 2013))
  }

  post("/api/v1/books/prices") {
    val el = XML.loadString(request.body)

    val prices: Seq[(String, Double)] = for {
      book <- el \ "book"
      title <- book \ "title"
      price <- book \ "price"
    } yield (title.text, price.text.toDouble)

    // do something with prices

    prices foreach println
  }

  get("/api/v1/accounts/:id/emails/count") {
    Library.findAccount(params("id")).map(_.emails.size).map(JInt(_)).getOrElse(halt(404))
  }

  put("/api/v1/accounts/:id/emails/:index") {
    parsedBody match {
      case JString(email) =>

        (for {
          id <- params.get("id")
          idx <- params.getAs[Int]("index")
          account <- Library.findAccount(id)
          currentEmail <- account.emails.toSeq.lift(idx)
        } yield {
          val updatedEmails = SortedSet(account.emails.toSeq.updated(idx, email): _*)
          val updatedAccount = account.copy(emails = updatedEmails)
          Library.updateAccount(updatedAccount)
        }) getOrElse halt(404)

      case _ => halt(500)
    }
  }

  delete("/api/v1/accounts/:id/emails/:index") {
    (for {
      id <- params.get("id")
      idx <- params.getAs[Int]("index")
      account <- Library.findAccount(id)
      email <- account.emails.toSeq.lift(idx)
    } yield {
      val updatedAccount = account.copy(emails = account.emails - email)
      Library.updateAccount(updatedAccount)
    }) getOrElse halt(404)
  }

  get("/api/v1/books") {
    Library.books
  }

  post("/api/v1/books") {
    parsedBody.extractOpt[Book] match {
      case Some(b) =>
        val book = Library.createBook(b.title, b.year, b.publisher, b.authors)

        // instead of JString(book.id) we return a String which will be wrapped in a JString
        book.id
      case None => halt(500)
    }
  }

  get("/api/v1/books/:id") {
    Library.findBook(params("id")) getOrElse halt(404)
  }

  put("/api/v1/books/:id") {
    (for {
      oldBook <- Library.findBook(params("id"))
      newBook <- parsedBody.extractOpt[Book]
    } yield Library.updateBook(newBook)) getOrElse halt(404)
  }

  delete("/api/v1/books/:id") {
    if (Library.deleteBook(params("id"))) halt(200) else halt(404)
  }

  class AccountSerializer extends CustomSerializer[Account](format => ( {
    case JObject(
    ("primaryEmail", JString(email)) ::
      ("pinCode", JString(pin)) ::
      ("address", JObject(
      ("street", JString(street)) ::
        ("city", JString(city)) ::
        ("country", JString(country)) :: Nil)) :: Nil) =>
      Account(null, email, pin, SortedSet[String](), Address(street, city, country))
  }, {
    case account: Account =>
      JObject.apply(
        "id" -> JString(account.id),
        "primaryEmail" -> JString(account.primaryEmail),
        "fallbackEmails" -> JArray(account.emails.map(JString(_)).toList),
        "address" -> JObject(
          "street" -> JString(account.address.street),
          "city" -> JString(account.address.city),
          "country" -> JString(account.address.country)))
  }))



}
