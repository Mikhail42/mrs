package com.ionkin.mrs.model

import scala.concurrent.Future
import com.ionkin.mrs.dao.{PersonDao, PlaceDao}

object Role extends Enumeration {
  type Role = Value
  val Patient, Doctor, Nurse, Admin, Auto /* PC */ = Value
}
import Role.Role

object ContactType extends Enumeration {
  type ContactType = Value
  val Phone, Email, Telegram, Facebook, Twitter, WhatsApp, Viber, Vk = Value
}
import ContactType.ContactType

// both id and created are primary keys. Latest valid object is current, others stored for history
// all fields are final: if you want change them, you should create new row
case class Person(first: String, last: String, middle: Option[String], created: RowCreated, id: Int = 0, valid: Boolean = true)
  extends Table with OrderedByTime[Person]

case class User(personId: Int, login: String, password: String, roleId: Int, created: RowCreated, salt: String, id: Int = 0,
                valid: Boolean = true) extends Table with OrderedByTime[User] {
  def this(person: Person, login: String, password: String, role: Role, rowCreatedBy: Person, salt: String) =
    this(person.id, login, password, role.id, RowCreated(rowCreatedBy.id), salt)
  def person()(implicit dao: PersonDao): Future[Person] = dao.personActual(personId)
  override def toString: String = s"User($personId,$login,secret,${Role(roleId)},$created,secret,$id,$valid)"
}

case class Contact(personId: Int, contactTypeId: Int, value: String, created: RowCreated, id: Int = 0, valid: Boolean = true)
  extends Table with OrderedByTime[Contact] {
  def this(person: Person, contactType: ContactType, value: String, rowCreatedBy: Person) =
    this(person.id, contactType.id, value, RowCreated(rowCreatedBy.id))
  def person()(implicit dao: PersonDao): Future[Person] = dao.personActual(personId)
  def contactType: ContactType = ContactType(contactTypeId)
}

case class PersonAddress(personId: Int, addressId: Int, created: RowCreated, id: Int = 0, valid: Boolean = true)
  extends Table with OrderedByTime[PersonAddress] {
  def this(person: Person, address: Address, rowCreatedBy: Person) = this(person.id, address.id, RowCreated(rowCreatedBy.id))
  def person()(implicit dao: PersonDao): Future[Person] = dao.personActual(personId)
  def address()(implicit dao: PlaceDao): Future[Address] = dao.address(addressId)
}
