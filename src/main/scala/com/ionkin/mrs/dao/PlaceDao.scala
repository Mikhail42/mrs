package com.ionkin.mrs.dao

import com.ionkin.mrs.model._

import scala.concurrent.Future

import Data.context._
import Data.executionContext

trait PlaceDao extends Dao {
  def searchCountries(filter: Country => Boolean): Future[List[Country]]
  def searchRegions(filter: Region => Boolean): Future[List[Region]]
  def searchLocalities(filter: Locality => Boolean): Future[List[Locality]]
  def searchAddresses(filter: Address => Boolean): Future[List[Address]]

  def searchCountry(filter: Country => Boolean): Future[Option[Country]] = searchCountries(filter).map(_.headOption)
  def searchRegion(filter: Region => Boolean): Future[Option[Region]] = searchRegions(filter).map(_.headOption)
  def searchLocality(filter: Locality => Boolean): Future[Option[Locality]] = searchLocalities(filter).map(_.headOption)
  def searchAddress(filter: Address => Boolean): Future[Option[Address]] = searchAddresses(filter).map(_.headOption)

  def country(countryId: Int): Future[Country] = searchCountries(_.id == countryId).map(_.head)
  def region(regionId: Int): Future[Region] = searchRegions(_.id == regionId).map(_.head)
  def locality(localityId: Int): Future[Locality] = searchLocalities(_.id == localityId).map(_.head)
  def address(addressId: Int): Future[Address] = searchAddresses(_.id == addressId).map(_.head)
}

object PlaceDbDao extends PlaceDao {
  def searchCountries(filter: Country => Boolean): Future[List[Country]] = run(quoted[Country](filter))
  def searchRegions(filter: Region => Boolean): Future[List[Region]] = run(quoted[Region](filter))
  def searchLocalities(filter: Locality => Boolean): Future[List[Locality]] = run(quoted[Locality](filter))
  def searchAddresses(filter: Address => Boolean): Future[List[Address]] = run(quoted[Address](filter))
}
