package com.ionkin.mrs

import com.ionkin.mrs.dao.{PersonDao, PersonTestDao}
import com.ionkin.mrs.model.Data
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

class AuthTest extends FlatSpec with Matchers {
  import Data.executionContext
  implicit val personDao: PersonDao = PersonTestDao

  private def check(login: String, password: String): Try[Boolean] =
    Await.ready(Auth.checkUser(login, password), Duration.Inf).value
    .getOrElse(Failure(new Exception("result is empty")))

  "auth with invalid login" should "failure" in {
    val v1: Try[Boolean] = check("ionkin4", "sde")
    v1 shouldBe Success(false)
  }

  "auth with invalid password" should "failure" in {
    val v1: Try[Boolean] = check("ionkin42", "sde")
    v1 shouldBe Success(false)
  }

  "auth with valid both login and password" should "success" in {
    val v2: Try[Boolean] = check("ionkin42", "***secretPassword***")
    v2 shouldBe Success(true)
  }
}
