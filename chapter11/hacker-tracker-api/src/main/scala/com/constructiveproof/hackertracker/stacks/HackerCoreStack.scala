package com.constructiveproof.hackertracker.stacks

import org.scalatra.{MethodOverride, ScalatraServlet}
import com.constructiveproof.hackertracker.init.DatabaseSessionSupport

trait HackerCoreStack extends ScalatraServlet with DatabaseSessionSupport with MethodOverride {

}
