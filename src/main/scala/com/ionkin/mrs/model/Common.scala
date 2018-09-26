package com.ionkin.mrs.model

import java.util.concurrent.Executors
import java.util.{Calendar, Date, TimeZone}

import com.ionkin.mrs.dao.PersonDao
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.{ExecutionContext, Future}
import io.getquill.{MysqlAsyncContext, SnakeCase}

object Data {
  val utcZone: TimeZone = TimeZone.getTimeZone("UTC")
  val utcCalendar: Calendar = Calendar.getInstance(utcZone)
  def utcTime(): Date = utcCalendar.getTime

  val config: Config = ConfigFactory.load()
  val contextConfig: Config = config.getConfig("ctx")

  implicit val executionContext: ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(4))
  val context = new MysqlAsyncContext(SnakeCase, contextConfig)
}
import Data.context._

final case class RowCreated(rowCreatedById: Int, rowCreatedTime: Date = Data.utcTime()) extends Embedded with Ordered[RowCreated] {
  def this(rowCreatedBy: Person, rowCreatedTime: Date) = this(rowCreatedBy.id, rowCreatedTime)
  def rowCreatedBy()(implicit dao: PersonDao): Future[Person] = dao.personActual(rowCreatedById)
  override def compare(that: RowCreated): Int = rowCreatedTime.compareTo(that.rowCreatedTime)
}

trait OrderedByTime[T <: OrderedByTime[T]] extends Ordered[T] {
  def created: RowCreated
  override def compare(that: T): Int = created.compareTo(that.created)
}
