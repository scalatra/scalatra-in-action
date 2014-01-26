package com.example.cms


class PagesController extends ScalatraCmsStack {

  get("/pages/:slug") {
    contentType = "text/html"
    PageDao.pages find (_.slug == params("slug")) match {
      case Some(page) => ssp("/pages/show", "page" -> page)
      case None => halt(404, "not found")
    }
  }

}


case class Page(slug: String, title: String, summary: String, body: String)

object PageDao {
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
