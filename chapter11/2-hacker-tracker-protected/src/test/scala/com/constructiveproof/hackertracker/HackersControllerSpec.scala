package com.constructiveproof.hackertracker

import com.constructiveproof.hackertracker.init.DatabaseInit
import com.constructiveproof.hackertracker.models.Db
import org.scalatest.BeforeAndAfter
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FunSuite
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfter, FunSuite}
import org.scalatra.test.scalatest.ScalatraSuite
import org.scalatra.test.scalatest.ScalatraSuite

class HackersControllerSpec extends FunSuite with ScalatraSuite with DatabaseInit with BeforeAndAfter with BeforeAndAfterAll {

  addServlet(classOf[HackersController], "/*")

  before {
    configureDb()
    Db.init
  }

  after {
    closeDbConnection()
  }

  test("simple get") {
    get("/new") {
      status should equal(302) // It's protected. Redirect!
    }
  }
}
