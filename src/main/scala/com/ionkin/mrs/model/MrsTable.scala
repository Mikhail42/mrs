package com.ionkin.mrs.model

import java.sql.Timestamp
import java.util.Date

import com.ionkin.mrs.dao.PersonDao
import slick.jdbc.H2Profile.api._
import slick.lifted.{CanBeQueryCondition, PrimaryKey}

import scala.concurrent.Future

sealed trait MrsTable

/*trait IdTable extends MrsTable { this: Table[_] =>
  def id: Rep[Int] = column[Int]("id", O.AutoInc)
  def pk: PrimaryKey = primaryKey("id", id)
}

trait CreatedInfoTable extends MrsTable { this: Table[_] =>
  def id: Rep[Int] = column[Int]("id", O.AutoInc)
  def rowCreatedBy: Rep[Int] = column[Int]("row_created_by")
  def rowCreatedTime: Rep[Timestamp] = column[Timestamp]("row_created_time")
  def note: Rep[String] = column[String]("note")

  def pk: PrimaryKey = primaryKey("pk_id_time", (id, rowCreatedTime))
  def commonCI = (rowCreatedBy, rowCreatedTime, note, id).mapTo[CreatedInfo]
}

final case class CreatedInfo(rowCreatedBy: Int, rowCreatedTime: Timestamp = new Timestamp(System.currentTimeMillis()),
                       note: String = "", id: Int = 0)

final case class RowCreated(rowCreatedById: Int, rowCreatedTime: Date = Data.utcTime()) with Ordered[RowCreated] {
  def this(rowCreatedBy: Person, rowCreatedTime: Date) = this(rowCreatedBy.id, rowCreatedTime)
  def rowCreatedBy()(implicit dao: PersonDao): Future[Person] = dao.personActual(rowCreatedById)
  override def compare(that: RowCreated): Int = rowCreatedTime.compareTo(that.rowCreatedTime)
}

trait OrderedByTime[T <: OrderedByTime[T]] extends Ordered[T] {
  def rowCreatedBy: RowCreated
  override def compare(that: T): Int = rowCreatedBy.compareTo(that.rowCreatedBy)
}

case class MaybeFilter[X, Y, C[_]](query: slick.lifted.Query[X, Y, C]) {
  def filter[T,R: CanBeQueryCondition](data: Option[T])(f: T => X => R): MaybeFilter[X, Y, C] =
    data.map(v => MaybeFilter(query.withFilter(f(v)))).getOrElse(this)
}*/


