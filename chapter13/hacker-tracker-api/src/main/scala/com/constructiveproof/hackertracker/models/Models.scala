package com.constructiveproof.hackertracker.models

import org.squeryl.{KeyedEntity, PersistenceStatus, Schema}
import org.squeryl.PrimitiveTypeMode._

case class User(val id: String) {
  def forgetMe() = {
    println("Destroying token in datastore")
  }
}

/**
 * A hacker in the tracker.
 */
case class Hacker(val id: Long, val firstName: String, val lastName: String, val motto: String, val birthYear: Int) extends SquerylRecord {
  def this() = this(0, "Foo", "McBar", "It's better to ask forgiveness than permission", 1950)
}


/**
 * The BlogDb object acts as a cross between a Dao and a Schema definition file.
 */
object Db extends Schema {

  def init = {
    inTransaction {
      Db.create
    }
  }

  val hackers = table[Hacker]("hackers")
  on(hackers)(a => declare(
    a.id is (autoIncremented)))
}

object Hacker {
  def create(hacker: Hacker): Boolean = {
    inTransaction {
      val result = Db.hackers.insert(hacker)
      if (result.isPersisted) {
        true
      } else {
        false
      }
    }
  }

  def all = {
    from(Db.hackers)(select(_))
  }

  def get(id: Long) = {
    Db.hackers.where(h => h.id === id).single
  }

  def destroy(id: Long) = {
    Db.hackers.delete(id)
  }
}

/**
 * This trait is just a way to aggregate our model style across multiple
 * models so that we have a single point of change if we want to add
 * anything to our model behaviour
 */
trait SquerylRecord extends KeyedEntity[Long] with PersistenceStatus {

}