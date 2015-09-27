package org.scalatra.book.chapter09

import com.typesafe.config.ConfigFactory

case class AppConfig(
  port: Int,
  webBase: String,
  assetsDirectory: String,
  env: AppEnvironment,
  mailConfig: MailConfig) {

  def isProduction = env == Production
  def isDevelopment = env == Development
}

case class MailConfig(
  user: String,
  password: String,
  host: String,
  sender: String)

sealed trait AppEnvironment
case object Development extends AppEnvironment
case object Staging extends AppEnvironment
case object Test extends AppEnvironment
case object Production extends AppEnvironment

object AppEnvironment {
  def fromString(s: String): AppEnvironment = {
    s match {
      case "development" => Development
      case "staging" => Staging
      case "test" => Test
      case "production" => Production
    }
  }

  def asString(s: AppEnvironment): String = {
    s match {
      case Development => "development"
      case Staging => "staging"
      case Test => "test"
      case Production => "production"
    }
  }
}

object AppConfig {
  def load: AppConfig = {
    val cfg = ConfigFactory.load

    val assetsDirectory = cfg.getString("assetsDirectory")
    val webBase = cfg.getString("webBase")
    val port = cfg.getInt("port")
    val env = AppEnvironment.fromString(cfg.getString("environment"))
    val mailConfig = MailConfig(
      cfg.getString("email.user"),
      cfg.getString("email.password"),
      cfg.getString("email.host"),
      cfg.getString("email.sender"))

    AppConfig(port, webBase, assetsDirectory, env, mailConfig)
  }
}