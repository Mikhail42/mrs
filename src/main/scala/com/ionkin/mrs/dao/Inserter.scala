package com.ionkin.mrs.dao

import com.ionkin.mrs.model.PersonDocument.{BirthCertificate, DriveLicense, HealthInsurancePolicy, Passport}

import scala.concurrent.Future
import com.ionkin.mrs.model._

trait Inserter {
  def insert[T <: Table](t: T): Future[Int]
}

object InserterForDb extends Inserter {
  import Data.context._
  import Data.executionContext

  def insert[T <: Table](t: T): Future[Int] = t match {
    // Person file
    case x: Person => run(quote { query.insert(lift(x)).returning(_.id) })
    case x: User => run(quote { query.insert(lift(x)).returning(_.id) })
    case x: Contact => run(quote { query.insert(lift(x)).returning(_.id) })
    case x: PersonAddress => run(quote { query.insert(lift(x)).returning(_.id) })
    // Place file
    case x: Location => run(quote { query.insert(lift(x)).returning(_.id) })
    case x: Country => run(quote { query.insert(lift(x)).returning(_.id) })
    case x: Region => run(quote { query.insert(lift(x)).returning(_.id) })
    case x: Locality => run(quote { query.insert(lift(x)).returning(_.id) })
    case x: Address => run(quote { query.insert(lift(x)).returning(_.id) })
    // PersonDocument file
    case x: InsuranceCompany => run(quote { query.insert(lift(x)).returning(_.id) })
    case x: BirthCertificate => run(quote { query.insert(lift(x)).returning(_.id) })
    case x: Passport => run(quote { query.insert(lift(x)).returning(_.id) })
    case x: DriveLicense => run(quote { query.insert(lift(x)).returning(_.id) })
    case x: HealthInsurancePolicy => run(quote { query.insert(lift(x)).returning(_.id) })
    // Visit file
    case x: Visit => run(quote { query.insert(lift(x)).returning(_.id) })
    // File file
    //case x: Image => run(quote { query.insert(lift(x)).returning(_.id) })
    //case x: Document => run(quote { query.insert(lift(x)).returning(_.id) })
  }
}
