package comments

import org.scalatra._
import com.mongodb.casbah.Imports._
import com.mongodb.util.JSON
import org.scalatra.json.JacksonJsonSupport
import org.json4s._
import org.json4s.mongo.{JObjectParser, ObjectIdSerializer}

/**
 * This trait adds conversion of DBObject/Traversable=MongoCursor to the render pipeline.
 */
trait MongoDbJsonConversion extends ScalatraBase with ApiFormats {

  // converts DBObject and MongoCursor to a JSON string
  def transformMongoObjectsToJson4s = {
    case dbo: DBObject =>
      contentType = formats("json")
      JSON.serialize(dbo)

    case xs: TraversableOnce[_] =>
      contentType = formats("json")
      JSON.serialize(xs)

  }: RenderPipeline

  // add to render pipeline
  override protected def renderPipeline = transformMongoObjectsToJson4s orElse super.renderPipeline

}

