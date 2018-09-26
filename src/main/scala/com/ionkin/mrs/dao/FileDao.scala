package com.ionkin.mrs.dao

import java.io.InputStream
import java.sql.SQLException

import com.ionkin.mrs.model.{Data, Document, Image}

import scala.concurrent.Future
import Data.context._
import Data.executionContext
import io.getquill.context.async.SqlTypes

trait FileDao extends Dao {
  def searchImages(filter: Image => Boolean): Future[List[Image]]
  def searchDocuments(filter: Document => Boolean): Future[List[Document]]

  def searchImage(filter: Image => Boolean): Future[Option[Image]] = searchImages(filter).map(optMax)
  def searchDocument(filter: Document => Boolean): Future[Option[Document]] = searchDocuments(filter).map(optMax)

  def imageActual(id: Int): Future[Image] = searchImage(_.id == id).map(_.get)
  def documentActual(id: Int): Future[Document] = searchDocument(_.id == id).map(_.get)
}

object FileDbDao extends FileDao {
  implicit val dec: Decoder[InputStream] = AsyncDecoder(SqlTypes.BLOB)((index: Index, row: ResultRow) => row(index) match {
    case value: InputStream => value
    case value => throw new SQLException(s"Value '$value' at index $index can't be decoded to InputStream")
  })
  def searchImages(filter: Image => Boolean): Future[List[Image]] = run(quoted[Image](filter))
  def searchDocuments(filter: Document => Boolean): Future[List[Document]] = run(quoted[Document](filter))
}
