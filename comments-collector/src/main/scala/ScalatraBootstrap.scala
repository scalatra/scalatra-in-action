import comments._

import org.scalatra._
import org.scalatra.servlet._
import org.scalatra.swagger._

import com.mongodb.casbah.Imports._

import javax.servlet.ServletContext
import javax.servlet.http.HttpServlet

class ScalatraBootstrap extends LifeCycle {

  // create an implicit instance of ApiInfo which publishes additional information
  implicit val apiInfo = new ApiInfo("The comments API",
    "Docs for the comments API",
    "http://www.manning.com/carrero2/",
    "Ross", "MIT", "http://scalatra.org")

  // create an implicit instance of Swagger which is passed to both servlets
  implicit val swagger = new Swagger("1.0", "1", apiInfo)

  // create a mongodb client and collection
  val mongoClient = MongoClient()
  val mongoColl = mongoClient("comments_collector")("comments")

  // def mountServlet(sc: ServletContext, servlet: HttpServlet, urlPattern: String, loadOnStartup: Int = 0) {
  //   val name = servlet.getClass.getName
  //   val reg = Option(sc.getServletRegistration(name)) getOrElse {
  //     val r = sc.addServlet(name, servlet)
  //     servlet match {
  //       case s: HasMultipartConfig =>
  //         r.setMultipartConfig(s.multipartConfig.toMultipartConfigElement)
  //       case _ =>
  //     }
  //     if (servlet.isInstanceOf[ScalatraAsyncSupport])
  //       r.setAsyncSupported(true)
  //     r.setLoadOnStartup(loadOnStartup)
  //     r
  //   }

  //   reg.addMapping(urlPattern)
  // }

  override def init(context: ServletContext) {

    // create a comments repository using the mongo collection
    val comments = CommentsRepository(mongoColl)

    // mount the api + swagger docs
    context.mount(new CommentsApi(comments), "/comments-collector")
    context.mount(new CommentsApiDoc(), "/api-docs")

    // mount the html frontend
    context.mount(new CommentsFrontend(comments), "/")

  }

  override def destroy(context: ServletContext) {

    // shutdown the mongo client
    mongoClient.close

  }
}
