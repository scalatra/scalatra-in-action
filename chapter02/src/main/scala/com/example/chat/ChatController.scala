package com.example.chat

import org.scalatra._
import scalate.ScalateSupport

// Imports for Atmosphere communication
import atmosphere._
import concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import org.json4s.JsonDSL._

class ChatController extends ScalachatStack {

  atmosphere("/the-chat") {
    new AtmosphereClient {
      def receive: AtmoReceive = {
        case JsonMessage(json) =>
          val msg = json
          broadcast(msg)
      }
    }
  }

  get("/pages/:slug") {
    contentType="text/html"
    FakeDataStore.pages find (_.slug == params("slug")) match {
      case Some(page) => ssp("/pages/show", "page" -> page)
      case None => halt(404, "not found")
    }
  }

  get("/broadcast") {
    val message = ("author" -> "server") ~ ("message" -> """
      Pushing arbitrary data down the Atmosphere pipe.
      This might come from anywhere - AMQP, Twitter,
      a database after-save callback,
      whatever you can think of.""")
    AtmosphereClient.broadcast("/the-chat", message)
    "success"
  }


}

case class Page(slug:String, title:String, summary:String, body: String)

object FakeDataStore {
  val pages = List(
    Page("bacon-ipsum",
        "Bacon ipsum dolor sit amet hamburger",
        """Shankle pancetta turkey ullamco exercitation laborum ut
        officia corned beef voluptate.""",
        """Fugiat mollit, spare ribs pork belly flank voluptate ground
        round do sunt laboris jowl. Meatloaf excepteur hamburger pork
        chop fatback drumstick frankfurter pork aliqua.
        Pork belly meatball meatloaf labore. Exercitation commodo nisi
        shank, beef drumstick duis. Venison eu shankle sunt commodo short
        loin dolore chicken prosciutto beef swine elit quis beef ribs.
        Short ribs enim shankle ribeye andouille bresaola corned beef
        jowl ut beef.Tempor do boudin, pariatur nisi biltong id elit
        dolore non sunt proident sed. Boudin consectetur jowl ut dolor
        sunt consequat tempor pork chop capicola pastrami mollit short
        loin."""),
    Page("veggie-ipsum",
      "Arugula prairie turnip desert raisin sierra leone",
      """Veggies sunt bona vobis, proinde vos postulo esse magis napa
      cabbage beetroot dandelion radicchio.""",
      """Brussels sprout mustard salad jícama grape nori chickpea
      dulse tatsoi. Maize broccoli rabe collard greens jícama wattle
      seed nori garbanzo epazote coriander mustard."""))
}

