package com.ionkin.mrs.dao

import scala.concurrent.Future
import com.ionkin.mrs.model.Data.executionContext
import com.ionkin.mrs.model._

import scala.collection.mutable

object InserterForTest extends Inserter {
  import PersonTestDao._

  def insert[T <: Table](t: T): Future[Int] = t match {
    // Person file
    case x: Person => add(id => x.copy(id = id), persons)
    case x: User => add(id => x.copy(id = id), users)
    case x: Contact => add(id => x.copy(id = id), contacts)
    case x: PersonAddress => add(id => x.copy(id = id), personAddresses)
    // PersonDocument file
    // case x: InsuranceCompany => add(id => x.copy(id = id), personAddresses)
    // case _ => ??? // TODO
  }

  private def add[T <: Table](t: Int => T, buffer: mutable.Buffer[T]): Future[Int] = {
    buffer.synchronized {
      val id = buffer.length + 1 // TODO: may be need synchronize by persons
      buffer += t(id)
      Future { id }
    }
  }
}
