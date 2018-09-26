package com.ionkin.mrs.dao

import com.ionkin.mrs.model._

import scala.concurrent.Future

object PlaceTestDao extends PlaceDao {
  import Data.executionContext

  def searchCountries(filter: Country => Boolean): Future[List[Country]] = Future {
    List(Country("Russia"), Country("USA"), Country("China")).filter(filter)
  }
  def searchRegions(filter: Region => Boolean): Future[List[Region]] = Future {
    List(Region("Moscow", 1), Region("Texas", 2), Region("Anhui", 3)).filter(filter)
  }
  def searchLocalities(filter: Locality => Boolean): Future[List[Locality]] = Future {
    List(Locality("Moscow", LocalityType.City.id, 1),
      Locality("Texas", LocalityType.City.id, 2),
      Locality("Hefei", LocalityType.City.id, 3)).filter(filter)
  }
  def searchAddresses(filter: Address => Boolean): Future[List[Address]] = Future {
    List(Address(1, "Pushkin street", "12 k. 123", AddressType.Home.id)).filter(filter)
  }
}
