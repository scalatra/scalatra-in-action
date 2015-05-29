import org.scalatra.book.chapter10.Tables._
import org.scalatra.book.chapter10._
import slick.driver.H2Driver.api._
import slick.jdbc.GetResult._

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

// database setup
trait db_setup {

  val jdbcUrl = "jdbc:h2:mem:chapter10;DB_CLOSE_DELAY=-1"
  val jdbcDriverClass = "org.h2.Driver"
  val db = Database.forURL(jdbcUrl, driver = jdbcDriverClass)

  val delay = Duration(5, "seconds")

}

// actions, execution
object dbio_actions_database_driver extends App with db_setup {

  // actions

  val createFoo: DBIO[Int] = sqlu"create table foo(name varchar)"

  val dropFoo: DBIO[Int] = sqlu"drop table foo"

  def insertFoo(name: String): DBIO[Int] = sqlu"insert into foo values ($name)"
  val selectFoo: DBIO[Seq[String]] = sql"select name from foo".as[String]
  val selectFoo0 = sql"select name from foo".as[String]

  val composedAction: DBIO[Unit] = DBIO.seq(
    createFoo,
    insertFoo("air"),
    insertFoo("water"),
    selectFoo map { xs => xs foreach println },
    dropFoo
  )

  // runs the action
  val run0: Future[Unit] = db.run(composedAction.transactionally)

  // wait for the query to be finished and the result being available
  Await.result(run0, Duration(5, "seconds"))


  val composedAction0: DBIO[Seq[String]] = for {
    _ <- createFoo
    _ <- insertFoo("air")
    _ <- insertFoo("water")
    xs <- selectFoo
    _ <- dropFoo
  } yield xs

  val res = db.run(composedAction0) map { xs => println(xs) }
  Await.result(res, Duration(5, "seconds"))


}

// tables, table queries, schema, queries, inserts
object tables_table_queries_schema_queries_inserts extends App with db_setup {

  class Foos(tag: Tag) extends Table[(Int, String)](tag, "FOOS") {
    def id = column[Int]("INT")
    def name = column[String]("NAME")

    def * = (id, name)
  }


  // manage schema
  
  val createTables: DBIO[Unit] = (routes.schema ++ areas.schema).create

  val dropTables: DBIO[Unit] = (routes.schema ++ areas.schema).drop


  // select

  val allRoutes: DBIO[Seq[Route]] = routes.result

  val firstOpt: DBIO[Option[Route]] = routes.result.headOption

  val first: DBIO[Route] = routes.result.head


  // insert, +=, ++=

  val insertArea: DBIO[Int] = {
    areas += Area(0, "Magic Wood", "Switzerland, Europe", 46.3347, 9.2612,
      "Hidden in the forest of the Avers valley there are some of the best boulder problems we have ever seen.")
  }

  val insertAreaReturnId: DBIO[Int] = {
    val area = Area(0, "Magic Wood", "Switzerland, Europe", 46.3347, 9.2612,
      "Hidden in the forest of the Avers valley there are some of the best boulder problems we have ever seen.")

    (areas returning areas.map(_.id)) += area
  }

  def insertRoutes(areaId: Int): DBIO[Option[Int]] = {
    routes ++= Seq(
      Route(0, areaId, None, "Downunder", 46.3347, 9.2612, "Boulder, 7C+ at Sofasurfer"),
      Route(0, areaId, None, "The Wizard", 46.3347, 9.2612, "Boulder, 7C at Beachbloc"),
      Route(0, areaId, None, "Master of a cow", 46.3347, 9.2612, "Boulder, 7C+ at Man of a cow")
    )
  }

  val insertSampleData: DBIO[Int] = for {
    areaId <- insertAreaReturnId
    _ <- insertRoutes(areaId)
  } yield areaId

  db.run(insertSampleData) foreach println

}

// queries
object db_query_language_actions extends App with db_setup {

  // table query

  val areas: Query[Areas, Area, Seq] = TableQuery[Areas]
  val routes: Query[Routes, Route, Seq] = TableQuery[Routes]


  // filter, map, monadic queries

  val routesQuery0: Query[(Rep[Int], Rep[String]), (Int, String), scala.Seq] = routes.filter(_.areaId === 2).map(r => (r.id, r.routeName))

  val routesQuery1 = for {
    route <- routes
    if route.areaId === 2
  } yield (route.id, route.routeName)


  // execution

  val routesQuery = routes.filter(_.areaId === 2).map(r => (r.id, r.routeName))

  val routesAction: DBIO[Option[(Int, String)]] = routesQuery.result.headOption

  val action: DBIO[Option[(Int, String)]] = for {
    _ <- DbSetup.createDatabase
    x <- routesAction
    _ <- DbSetup.dropDatabase
  } yield x

  val x = Await.result(db.run(action), Duration(5, "seconds"))

  println(x)
  // Some((2,Bobcat-Houlihan))


  // limit, sorting, aggregating (min, max, groupBy)
  val lessRoutes = routes.drop(3).take(10)

  val sortById = routes.sortBy(_.id.desc)
  val sortByIdName = routes.sortBy(r => (r.areaId.asc, r.routeName))

  val statistics = routes
    .groupBy(_.areaId)
    .map { case (areaId, rs) =>
    (areaId,
      rs.length,
      rs.map(_.longitude).min,
      rs.map(_.longitude).max,
      rs.map(_.latitude).min,
      rs.map(_.latitude).max)
  }


  // distinct, filter with lowerCase
  def bySuffix(str: String) = {
    routes
      .filter(_.routeName.toLowerCase like f"%%${str.toLowerCase}")
      .groupBy(_.areaId).map(_._1)
  }

  // query composition

  val areasWithTrails = areas.filter(_.id in bySuffix("trails"))

  def log(title: String)(xs: Seq[Any]): Unit = {
    println(f"$title")
    xs foreach { x => println(f"--$x") }
    println
  }

  val action2 = for {
    _ <- DbSetup.createDatabase
    _ <- lessRoutes.result map log("limited routes")
    _ <- sortById.result map log("sorted routes (desc id)")
    _ <- sortByIdName.result map log("sorted routes (asc id, desc name)")
    _ <- statistics.result map log("area statistics")
    _ <- bySuffix("trails").result map log("area ids")
    _ <- areasWithTrails.result map log("area with trails")
    _ <- DbSetup.dropDatabase
  } yield ()

  println("running action2")

  Await.result(db.run(action2), Duration(5, "seconds"))




  // joins
  // - applicative
  // - monadic

  val crossJoin = areas join routes
  val innerJoin = routes join areas on (_.areaId === _.id)
  val leftJoin = areas joinLeft routes on (_.id === _.areaId)

  val innerJoinMonadic = for {
    r <- routes
    a <- areas if r.areaId === a.id
  } yield (r, a)


  val innerJoinAction: DBIO[Seq[(Route, Area)]] =  innerJoin.result
  val leftJoinAction: DBIO[Seq[(Area, Option[Route])]] = leftJoin.result
  val monadicJoinAction: DBIO[Seq[(Route, Area)]] = innerJoinMonadic.result


  // foreign key declaration

  val trailAreasQuery = for {
    route <- routes
    if (route.routeName.toLowerCase like "%trail")
    area <- route.area
  } yield area   // returns lifted type

  val trailAreasAction: DBIO[Seq[Area]] = trailAreasQuery.result





  // update, delete

  routes.byName("Midnight Lightning")
    .map(r => (r.description))
    .update("Midnight Lightning is a problem on the Columbia Boulder.")

  routes.byName("Midnight Lightning").delete





  // parameterized queries: queries vs. pre-compiled queries

  def bySuffixQuery(str: String) = {
    for {
      route <- routes
      if route.routeName.toLowerCase like f"%%${str.toLowerCase}"
      area <- route.area
    } yield area
  }

  val bySuffixPrecompiled = Compiled((str: Rep[String]) =>
    for {
      route <- routes
      if route.routeName.toLowerCase like f"%%${str.toLowerCase}"
      area <- route.area
    } yield area
  )

}

// organizing queries
object query_extensions_organization extends App with db_setup {

  implicit class RoutesQueryExtensions(query: Query[Routes, Route, Seq]) {

    def lessRoutes = query.drop(3).take(10)

    def sortById = query.sortBy(_.id.desc)
    def sortByIdName = query.sortBy(r => (r.areaId.asc, r.routeName))

    def statistics = query
      .groupBy(_.areaId)
      .map { case (areaId, rs) =>
      (areaId,
        rs.length,
        rs.map(_.longitude).min,
        rs.map(_.longitude).max,
        rs.map(_.latitude).min,
        rs.map(_.latitude).max)
    }

    def byId(id: Int) = query.filter(_.id === id)
    def byName(name: String) = query.filter(_.routeName === name)

    def bySuffix(str: String) = {
      query
        .filter(_.routeName.toLowerCase like f"%%${str.toLowerCase}")
    }

    val distinctAreaIds = query.groupBy(_.areaId).map(_._1)

  }

  val areas: Query[Areas, Area, Seq] = TableQuery[Areas]
  val routes: Query[Routes, Route, Seq] = TableQuery[Routes]

  def log(title: String)(xs: Seq[Any]): Unit = {
    println(f"$title")
    xs foreach { x => println(f"--$x") }
    println
  }

  val action = for {
    _ <- DbSetup.createDatabase
    _ <- routes.lessRoutes.result map log("limited routes")
    _ <- routes.sortById.result map log("sorted routes (desc id)")
    _ <- routes.sortByIdName.result map log("sorted routes (asc id, desc name)")
    _ <- routes.statistics.result map log("area statistics")
    _ <- areas.filter(_.id in routes.bySuffix("trails").distinctAreaIds).result map log("areas with trails")
    _ <- DbSetup.dropDatabase
  } yield ()

  println("running action")

  Await.result(db.run(action), Duration(5, "seconds"))

}

