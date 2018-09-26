package com.ionkin.mrs.model

import com.ionkin.mrs.dao.{PersonDao, PersonTestDao}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

class PersonTest extends FlatSpec with Matchers {
  implicit val personDao: PersonDao = PersonTestDao

  "person" should "be found by id" in {
    Await.ready(personDao.searchPerson(_.id == 1), Duration.Inf).value
    .getOrElse(Failure(new Exception("result is empty"))) shouldBe
      Success(Some(Person("Mikhail", "Ionkin", Some("Anatolevich"), RowCreated(1), 1)))
    Await.ready(personDao.searchPerson(_.last == "ds"), Duration.Inf).value
      .getOrElse(Failure(new Exception("result is empty"))) shouldBe Success(None)
  }
  "user" should "be found by id" in {
    import com.ionkin.mrs.dao.PersonTestDao.{encryptedPassword, salt}
    Await.ready(personDao.searchUser(_.id == -1), Duration.Inf).value
      .getOrElse(Failure(new Exception("result is empty"))) shouldBe Success(None)
    Await.ready(personDao.searchUser(_.id == 1), Duration.Inf).value
      .getOrElse(Failure(new Exception("result is empty"))) shouldBe
      Success(Some(User(1, "ionkin42", encryptedPassword, Role.Admin.id, RowCreated(1), salt, 1)))
  }
  "contact" should "be found by id" in {
    Await.ready(personDao.searchContact(_.id == -1), Duration.Inf).value
      .getOrElse(Failure(new Exception("result is empty"))) shouldBe Success(None)
    Await.ready(personDao.searchContact(_.id == 1), Duration.Inf).value
      .getOrElse(Failure(new Exception("result is empty"))) shouldBe
      Success(Some(Contact(1, ContactType.Email.id, "ionkinmikhail@gmail.com", RowCreated(1), 1)))
  }
  "person address" should "be found by id" in {
    Await.ready(personDao.searchPersonAddress(_.id == -1), Duration.Inf).value
      .getOrElse(Failure(new Exception("result is empty"))) shouldBe Success(None)
    Await.ready(personDao.searchPersonAddress(_.id == 1), Duration.Inf).value
      .getOrElse(Failure(new Exception("result is empty"))) shouldBe
      Success(Some(PersonAddress(1, 1, RowCreated(1), 1)))
  }
}
