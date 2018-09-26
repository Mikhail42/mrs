package com.ionkin.mrs.model

import java.util.Date

import scala.concurrent.Future
import com.ionkin.mrs.dao.{PersonDao, PlaceDao}

object VisitType extends Enumeration {
  type VisitType = Value
  val Hospitalization, Outpatient /*Ambulatory*/ = Value
}
import VisitType.VisitType

case class Visit(patientId: Int, visitTypeId: Int, addressId: Int, time: Date, created: RowCreated, id: Int = 0)
  extends Table with OrderedByTime[Visit] {
  def this(patient: Person, visitType: VisitType, address: Address, time: Date, createdBy: Person) =
    this(patient.id, visitType.id, address.id, time, RowCreated(createdBy.id))
  def patient()(implicit dao: PersonDao): Future[Person] = dao.personActual(patientId)
  def address()(implicit dao: PlaceDao): Future[Address] = dao.address(addressId)
  def visitType: VisitType = VisitType(visitTypeId)
}
