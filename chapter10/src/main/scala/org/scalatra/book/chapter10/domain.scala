package org.scalatra.book.chapter10

case class Area(
  id: Int,
  name: String,
  location: String,
  latitude: Double,
  longitude: Double,
  description: String)

case class Route(
  id: Int,
  areaId: Int,
  mountainName: Option[String],
  routeName: String,
  latitude: Double,
  longitude: Double,
  description: String)
