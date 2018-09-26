package com.ionkin.mrs.dao

import com.ionkin.mrs.model.Data
import com.ionkin.mrs.model.PersonDocument.{BirthCertificate, DriveLicense, HealthInsurancePolicy, Passport}

import scala.concurrent.Future

import Data.context._
import Data.executionContext

trait PersonDocumentDao extends Dao {
  def searchBirthCertificates(filter: BirthCertificate => Boolean): Future[List[BirthCertificate]]
  def searchPassports(filter: Passport => Boolean): Future[List[Passport]]
  def searchDriveLicenses(filter: DriveLicense => Boolean): Future[List[DriveLicense]]
  def searchHealthInsurancePolicies(filter: HealthInsurancePolicy => Boolean): Future[List[HealthInsurancePolicy]]

  def searchBirthCertificate(filter: BirthCertificate => Boolean): Future[Option[BirthCertificate]] = searchBirthCertificates(filter).map(optMax)
  def searchPassport(filter: Passport => Boolean): Future[Option[Passport]] = searchPassports(filter).map(optMax)
  def searchDriveLicense(filter: DriveLicense => Boolean): Future[Option[DriveLicense]] = searchDriveLicenses(filter).map(optMax)
  def searchHealthInsurancePolicy(filter: HealthInsurancePolicy => Boolean): Future[Option[HealthInsurancePolicy]] = searchHealthInsurancePolicies(filter).map(optMax)

  def birthCertificateActual(id: Int): Future[BirthCertificate] = searchBirthCertificate(_.id == id).map(_.get)
  def passportActual(id: Int): Future[Passport] = searchPassport(_.id == id).map(_.get)
  def driveLicenseActual(id: Int): Future[DriveLicense] = searchDriveLicense(_.id == id).map(_.get)
  def healthInsurancePolicyActual(id: Int): Future[HealthInsurancePolicy] = searchHealthInsurancePolicy(_.id == id).map(_.get)
}

object PersonDocumentDbDao extends PersonDocumentDao {
  def searchBirthCertificates(filter: BirthCertificate => Boolean): Future[List[BirthCertificate]] = run(quoted[BirthCertificate](filter))
  def searchPassports(filter: Passport => Boolean): Future[List[Passport]] = run(quoted[Passport](filter))
  def searchDriveLicenses(filter: DriveLicense => Boolean): Future[List[DriveLicense]] = run(quoted[DriveLicense](filter))
  def searchHealthInsurancePolicies(filter: HealthInsurancePolicy => Boolean): Future[List[HealthInsurancePolicy]] = run(quoted[HealthInsurancePolicy](filter))
}
