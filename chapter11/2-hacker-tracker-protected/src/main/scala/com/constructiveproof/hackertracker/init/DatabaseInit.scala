package com.constructiveproof.hackertracker.init

import java.io.File

import com.mchange.v2.c3p0.ComboPooledDataSource
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import org.squeryl.{Session, SessionFactory}
import org.squeryl.adapters.H2Adapter

trait DatabaseInit {
  val logger = LoggerFactory.getLogger(getClass)

  val dbDir = "/tmp/db"
  val dbPath = dbDir + "/hackertracker.db"
  val databaseConnection = "jdbc:h2:file:" + dbPath

  var cpds = new ComboPooledDataSource

  def configureDb() {
    cpds.setDriverClass("org.h2.Driver")
    cpds.setJdbcUrl(databaseConnection)

    cpds.setMinPoolSize(1)
    cpds.setAcquireIncrement(1)
    cpds.setMaxPoolSize(50)

    SessionFactory.concreteFactory = Some(() => connection)

    def connection = {
      logger.info("Creating connection with c3po connection pool")
      Session.create(cpds.getConnection, new H2Adapter)
    }
  }

  def closeDbConnection() {
    logger.info("Closing c3po connection pool")
    cpds.close()
  }

  def wipeDb = {
    FileUtils.deleteDirectory(new File(dbDir))
  }
}