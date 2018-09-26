package com.ionkin.mrs.dao

import com.ionkin.mrs.Auth
import com.ionkin.mrs.model._

import scala.collection.mutable
import scala.concurrent.Future

object PersonTestDao extends PersonDao {
  import Data.executionContext

  private val clearPassword = "***secretPassword***"
  private[mrs] val salt = Auth.createSaltAsBase64()
  private[mrs] val encryptedPassword = Auth.encrypt(clearPassword, salt)

  private[dao] val persons: mutable.Buffer[Person] = mutable.Buffer(
    Person("Mikhail", "Ionkin", Some("Anatolevich"), RowCreated(1), 1)
  )
  private[dao] val users: mutable.Buffer[User] = mutable.Buffer(
    User(1, "ionkin42", encryptedPassword, Role.Admin.id, RowCreated(1), salt, 1)
  )
  private[dao] val contacts: mutable.Buffer[Contact] = mutable.Buffer(
    Contact(1, ContactType.Email.id, "ionkinmikhail@gmail.com", RowCreated(1), 1)
  )
  private[dao] val personAddresses: mutable.Buffer[PersonAddress] = mutable.Buffer(
    PersonAddress(1, 1, RowCreated(1), 1)
  )

  def searchPersons(filter: Person => Boolean): Future[List[Person]] = Future {
    persons.filter(filter).toList
  }
  def searchUsers(filter: User => Boolean): Future[List[User]] = Future {
    users.filter(filter).toList
  }
  def searchContacts(filter: Contact => Boolean): Future[List[Contact]] = Future {
    contacts.filter(filter).toList
  }
  def searchPersonAddresses(filter: PersonAddress => Boolean): Future[List[PersonAddress]] = Future {
    personAddresses.filter(filter).toList
  }
}
