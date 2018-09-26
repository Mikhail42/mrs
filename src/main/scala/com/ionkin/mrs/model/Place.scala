package com.ionkin.mrs.model

import scala.concurrent.Future
import Data.executionContext
import com.ionkin.mrs.dao.PlaceDao

object AddressType extends Enumeration {
  type AddressType = Value
  val Home, Work, Clinic, Other = Value
}
import AddressType.AddressType

object LocalityType extends Enumeration {
  type LocalityType = Value
  val City, Town /* Urban-type settlement */, Village = Value
}
import LocalityType.LocalityType


sealed trait Place extends Table

final case class Location(lat: Double, lon: Double, height: Option[Double] = None, id: Int = 0) extends Place

final case class Country(name: String, id: Int = 0) extends Place

final case class Region(name: String, countryId: Int, id: Int = 0) extends Place { // State, Province
  def this(name: String, country: Country) = this(name, country.id)
  def country()(implicit dao: PlaceDao): Future[Country] = dao.country(countryId)
}

final case class Locality(name: String, localityTypeId: Int, regionId: Int, id: Int = 0) extends Place {
  def this(name: String, localityType: LocalityType, region: Region) = this(name, localityType.id, region.id)
  def region()(implicit dao: PlaceDao): Future[Region] = dao.region(regionId)
  def country()(implicit dao: PlaceDao): Future[Country] = region.flatMap(_.country)
  def localityType: LocalityType = LocalityType(localityTypeId)
}

final case class Address(localityId: Int, street: String, home: String, addressTypeId: Int, id: Int = 0) extends Place {
  def this(locality: Locality, street: String, home: String, addressType: AddressType) =
    this(locality.id, street, home, addressType.id)
  def locality()(implicit dao: PlaceDao): Future[Locality] = dao.locality(localityId)
  def region()(implicit dao: PlaceDao): Future[Region] = locality.flatMap(_.region) // may be use cache?
  def country()(implicit dao: PlaceDao): Future[Country] = locality.flatMap(_.region.flatMap(_.country)) // may be use cache?
  // def location: Future[Location] = ??? // use Google or Yandex API
}
