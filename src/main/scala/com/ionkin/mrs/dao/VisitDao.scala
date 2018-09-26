package com.ionkin.mrs.dao

import scala.concurrent.Future

import com.ionkin.mrs.model.{Data, Visit}
import Data.executionContext
import Data.context._

trait VisitDao extends Dao {
  def searchVisits(filter: Visit => Boolean): Future[List[Visit]]
  def searchVisit(filter: Visit => Boolean): Future[Option[Visit]] = searchVisits(filter).map(optMax)
  def visitActual(id: Int): Future[Visit] = searchVisit(_.id == id).map(_.get)
}

object VisitDbDao extends VisitDao {
  def searchVisits(filter: Visit => Boolean): Future[List[Visit]] = run(quoted[Visit](filter))
}
