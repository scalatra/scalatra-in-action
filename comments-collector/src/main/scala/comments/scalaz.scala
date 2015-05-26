package comments

import org.scalatra._

import scalaz._, Scalaz._

trait ScalazSupport extends ScalatraBase {

  // be able to handle scalaz' \/ as return value, simply unwrap the value from the container
  override def renderPipeline: RenderPipeline = ({
    case \/-(r) => r
    case -\/(l) => l
  }: RenderPipeline) orElse super.renderPipeline

}
