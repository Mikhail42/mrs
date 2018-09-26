package com.ionkin.mrs.dao

import scala.concurrent.Future

import com.ionkin.mrs.model._

import Data.context._
import Data.executionContext

trait PersonDao extends Dao {
  def searchPersons(filter: Person => Boolean): Future[List[Person]]
  def searchUsers(filter: User => Boolean): Future[List[User]]
  def searchContacts(filter: Contact => Boolean): Future[List[Contact]]
  def searchPersonAddresses(filter: PersonAddress => Boolean): Future[List[PersonAddress]]

  def searchPerson(filter: Person => Boolean): Future[Option[Person]] = searchPersons(filter).map(optMax)
  def searchUser(filter: User => Boolean): Future[Option[User]] = searchUsers(filter).map(optMax)
  def searchContact(filter: Contact => Boolean): Future[Option[Contact]] = searchContacts(filter).map(optMax)
  def searchPersonAddress(filter: PersonAddress => Boolean): Future[Option[PersonAddress]] = searchPersonAddresses(filter).map(optMax)

  def personActual(personId: Int): Future[Person] = searchPerson(_.id == personId).map(_.get)
  def userActual(userId: Int): Future[User] = searchUser(_.id == userId).map(_.get)
  def contactActual(id: Int): Future[Contact] = searchContact(_.id == id).map(_.get)
  def personAddressActual(id: Int): Future[PersonAddress] = searchPersonAddress(_.id == id).map(_.get)
}

object PersonDbDao extends PersonDao {
  def searchPersons(filter: Person => Boolean): Future[List[Person]] = run(quoted[Person](filter))
  def searchUsers(filter: User => Boolean): Future[List[User]] = run(quoted[User](filter))
  def searchContacts(filter: Contact => Boolean): Future[List[Contact]] = run(quoted[Contact](filter))
  def searchPersonAddresses(filter: PersonAddress => Boolean): Future[List[PersonAddress]] = run(quoted[PersonAddress](filter))
}
