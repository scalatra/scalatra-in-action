import scala.slick.jdbc.JdbcBackend._
import scala.slick.jdbc.StaticQuery

object DbSetup {

  protected val testTableExists = StaticQuery.query[String, Int]("select count(*) from information_schema.tables where table_name = ?")

  def executeScript(f: String)(implicit session: Session) {
    io.Source.fromFile(f).mkString.split(";") foreach { q => StaticQuery.updateNA(q).execute }
  }

  def createDatabase(implicit session: Session) {
    // init only if it does not exist
    if (testTableExists("areas").first == 0) {
      executeScript("escalade-schema-h2.sql")
      executeScript("escalade-inputs-h2.sql")
    }
  }

  def dropDatabase(implicit session: Session) {
    if (testTableExists("areas").first > 0) executeScript("escalade-drop-h2.sql")
  }

}

