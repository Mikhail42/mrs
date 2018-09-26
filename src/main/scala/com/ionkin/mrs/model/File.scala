package com.ionkin.mrs.model

import java.io.InputStream

import io.getquill.Embedded

import scala.concurrent.Future
import scala.reflect.io.Streamable
import Data.executionContext

case class File(filename: String, mimeType: String, stream: InputStream, created: RowCreated,
                description: Option[String] = None, size: Option[Int] = None, id: Int = 0, valid: Boolean = true)
  extends Embedded with OrderedByTime[File] {
  lazy val bytes: Future[Array[Byte]] = Future { Streamable.bytes(stream) }
}

case class Image(file: File, width: Int, height: Int) extends Table with OrderedByTime[Image] {
  override def id: Int = file.id
  override def created: RowCreated = file.created
}

case class Document(file: File, pages: Option[Int] = None) extends Table with OrderedByTime[Document] {
  override def id: Int = file.id
  override def created: RowCreated = file.created
}
