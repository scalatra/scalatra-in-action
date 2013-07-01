package org.scalatra.book.chapter05.json

import scala.collection.SortedSet

case class Book(id: String,
                title: String,
                year: Int,
                publisher: String,
                authors: List[String])

case class Account(id: String,
                   pinCode: String,
                   primaryEmail: String,
                   emails: SortedSet[String],
                   address: Address)

case class Address(street: String,
                   city: String,
                   country: String)

object Library {

  private val _accounts = collection.mutable.Map[String, Account]()

  def accounts: List[Account] = _accounts.values.toList

  def findAccount(id: String): Option[Account] = _accounts.get(id)

  def updateAccount(account: Account) {
    _accounts(account.id) = account
  }

  def deleteAccount(id: String): Boolean = {
    _accounts.remove(id).isDefined
  }

  def createAccount(pinCode: String, primaryEmail: String, address: Address): Account = {
    val acc = Account(uuid, pinCode, primaryEmail, SortedSet[String](), address)
    _accounts(acc.id) = acc
    acc
  }

  private def uuid = java.util.UUID.randomUUID().toString

  private val _books = collection.mutable.Map[String, Book]()

  def books: List[Book] = _books.values.toList

  def createBook(title: String, year: Int, publisher: String, authors: List[String]) = {
    val book = Book(uuid, title, year, publisher, authors)
    _books(book.id) = book
    book
  }

  def findBook(id: String): Option[Book] = _books.get(id)

  def updateBook(book: Book) {
    _books(book.id) = book
  }

  def deleteBook(id: String): Boolean = {
    _books.remove(id).isDefined
  }
}
