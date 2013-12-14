import com.example.comments._
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {

  implicit val swagger = new CommentsSwagger

  override def init(context: ServletContext) {
    context.mount(new CommentsController, "/comments/*")
    context mount (new ResourcesApp, "/api-docs/*")
  }
}
