package com.ionkin.mrs.model

import java.util.Date

import scala.concurrent.Future
import com.ionkin.mrs.dao.{PersonDao, PlaceDao}

object Sex extends Enumeration {
  type Sex = Value
  val Man, Woman = Value
}
import Sex.Sex

case class InsuranceCompany(name: String, addressId: Int, phoneId: Int, created: RowCreated, id: Int = 0) extends Table with OrderedByTime[InsuranceCompany] {
  def this(name: String, address: Address, phone: Contact, createdBy: Person) =
    this(name, address.id, phone.id, RowCreated(createdBy.id))
  def address()(implicit dao: PlaceDao): Future[Address] = dao.address(addressId)
  def phone()(implicit dao: PersonDao): Future[Contact] = dao.contactActual(phoneId)
}

sealed trait PersonDocument extends Table {
  def personId: Int
  def expireDate: Date
  def created: RowCreated
  def id: Int
  def valid: Boolean
  def person()(implicit dao: PersonDao): Future[Person] = dao.personActual(personId)
}

object PersonDocument {
  case class BirthCertificate(personId: Int, number: String, expireDate: Date, created: RowCreated, id: Int = 0,
                              valid: Boolean = true) extends PersonDocument  with OrderedByTime[BirthCertificate] {
    def this(person: Person, number: String, expireDate: Date, createdBy: Person) = this(person.id, number, expireDate, RowCreated(createdBy.id))
  }

  case class Passport(personId: Int, number: String, countryId: Int, sexId: Int, birthDate: Date, birthLocalityId: Int,
                      issueDate: Date, authority: String, expireDate: Date, created: RowCreated,
                      nationality: Option[String] = None, photoId: Option[Int] = None, id: Int = 0, valid: Boolean = true)
    extends PersonDocument with OrderedByTime[Passport] {
    def this(person: Person, number: String, country: Country, sex: Sex, birthDate: Date, birthLocality: Locality,
             issueDate: Date, authority: String, expireDate: Date, createdBy: Person, nationality: Option[String],
             photo: Option[Image]) = this(person.id, number, country.id, sex.id, birthDate, birthLocality.id, issueDate,
      authority, expireDate, RowCreated(createdBy.id), nationality, photo.map(_.id))
    def country(countryId: Int)(implicit dao: PlaceDao): Future[Country] = dao.country(countryId)
    def birthPlace(birthPlaceId: Int)()(implicit dao: PlaceDao): Future[Locality] = dao.locality(birthLocalityId)
  }

  case class DriveLicense(personId: Int, countryId: Int, number: String, categories: String, birthDate: Date,
                          birthLocalityId: Int, issueDate: Date, expireDate: Date, created: RowCreated,
                          photoId: Option[Int] = None, id: Int = 0, valid: Boolean = true)
    extends PersonDocument with OrderedByTime[DriveLicense] {
    def this(person: Person, country: Country, number: String, categories: String, birthDate: Date,
             birthLocality: Locality, issueDate: Date, expireDate: Date, createdBy: Person,
             photo: Option[Image]) = this(person.id, country.id, number, categories, birthDate, birthLocality.id,
      issueDate, expireDate, RowCreated(createdBy.id), photo.map(_.id))
    def country(countryId: Int)(implicit dao: PlaceDao): Future[Country] = dao.country(countryId)
    def birthPlace(birthPlaceId: Int)(implicit dao: PlaceDao): Future[Locality] = dao.locality(birthLocalityId)
  }

  case class HealthInsurancePolicy(personId: Int, countryId: Int, number: String, sexId: Int, birthDate: Date,
                                   birthLocalityId: Int, issueDate: Date, expireDate: Date,
                                   insuranceCompanyId: Int, registerDate: Date, created: RowCreated,
                                   registerPerson: Option[String] = None, id: Int = 0, valid: Boolean = true)
    extends PersonDocument with OrderedByTime[HealthInsurancePolicy]
}
