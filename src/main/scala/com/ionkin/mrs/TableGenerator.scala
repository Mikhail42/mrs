package com.ionkin.mrs

import com.ionkin.mrs.model.PersonDocument.{BirthCertificate, DriveLicense, HealthInsurancePolicy, Passport}
import com.ionkin.mrs.model._

import scala.collection.mutable

object TableGenerator extends App {

  import com.google.common.base.CaseFormat

  private def transform(in: String): String =
    CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, in.stripPrefix("class "))

  private val jdbcTypeMap = Map(
    "Boolean" -> "BIT(1)",
    "Int" -> "INT",
    "LONG" -> "BIGINT",
    "Double" -> "DOUBLE",
    "String" -> "CHAR(30)",
    "java.util.Date" -> "TIMESTAMP",
    "java.io.InputStream" -> "BLOB"
  )
  private def transformToJdbcType(className: String): String = {
    if (className.startsWith("Option[")) jdbcTypeMap(className.substring("Option[".length, className.length - 1)) + " DEFAULT NULL"
    else jdbcTypeMap(className) + " NOT NULL"
  }

  private def transformCLassName(className: String): String = className.substring(0, className.length - 1)

  import scala.reflect.runtime.universe._

  def fieldMap[T: TypeTag : reflect.ClassTag]: mutable.Buffer[(String, String)] = {
    typeOf[T].members.collect {
      case m: MethodSymbol if m.isCaseAccessor =>
        val resType: TypeApi = m.typeSignature.finalResultType
        if (resType.baseClasses.contains(typeOf[RowCreated].typeSymbol)) {
          fieldMap[RowCreated]
        } else if (resType.baseClasses.contains(typeOf[File].typeSymbol)) {
          fieldMap[File]
        } else {
          mutable.Buffer((m.name.toString, resType.toString))
        }
    }.flatMap(_.toBuffer).toBuffer
  }

  def generate[A: TypeTag : reflect.ClassTag]: String = {
    val table = transform(typeOf[A].typeSymbol.toString)
    val bufFields = fieldMap[A]
    val allFieds = bufFields.toMap.mapValues(v => transformToJdbcType(v))
    generate(table, allFieds)
  }

  def generate(table: String, fields: Map[String, String]): String = {
    val pk = mutable.Set[String]()
    if (fields.contains("id")) pk += "id"
    if (fields.contains("rowCreatedTime")) pk += "row_created_time"
    generate(table, fields.toList.sortBy(_._1), pk.toSet)
  }

  private val foreignKeys = Map(
    "personId" -> "person",
    "patientId" -> "person",
    "rowCreatedById" -> "person",
    "addressId" -> "address",
    "countryId" -> "country",
    "regionId" -> "region",
    "birthLocalityId" -> "locality",
    "photoId" -> "photo",
    "insuranceCompanyId" -> "insurance_company",
    "phoneId" -> "contact")

  def generate(table: String, fields: List[(String, String)], pk: Set[String]): String = {
    val f = fields.map {
      case (k, t) => s"${transform(k)} $t" +
        //(if (foreignKeys.contains(k)) " FOREIGN KEY " + foreignKeys(k) + " (id)" else "") +
        (if (k == "id") " AUTO_INCREMENT" else "")
    }
    val space = System.lineSeparator() + "        "
    val foreign = fields.map(_._1).filter(p => foreignKeys.contains(p))
      .map(x => s"CONSTRAINT FOREIGN KEY (${transform(x)}) REFERENCES ${foreignKeys(x)} (id)")
      .mkString(s",$space")
    s"CREATE TABLE IF NOT EXISTS $table (" + space +
      f.mkString(s",$space") + s",$space" +
      s"PRIMARY KEY (${pk.mkString(", ")})" +
      (if (foreign.isEmpty) "" else s",$space" + foreign) +
      s" );"
  }

  val res = List(generate[Person], generate[User], generate[Contact], generate[PersonAddress],
    generate[InsuranceCompany], generate[BirthCertificate], generate[Passport], generate[DriveLicense], generate[HealthInsurancePolicy],
    generate[Country], generate[Region], generate[Locality], generate[Locality], generate[Location],
    generate[Visit], generate[Document], generate[Image]).mkString("\n\n").replace(
    "salt " + jdbcTypeMap("String"), "salt CHAR(50)").replace(
    "password " + jdbcTypeMap("String"), "password CHAR(50)")
  println(res)
}
