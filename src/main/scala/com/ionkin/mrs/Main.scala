package com.ionkin.mrs

object Main extends App {
  val basePath = "/home/mikhail/workspace/mrs"
  val mainPath = basePath + "/src/main"

  scalaxb.compiler.Main.main(Array(mainPath + "/xsd/fhir-codegen-xsd/fhir-single.xsd",
    "-d", mainPath + "/scala/com/ionkin/mrs/hl7/fhir",
    "-p", "com.ionkin.mrs.hl7.fhir",
    "--blocking")
  )
}
