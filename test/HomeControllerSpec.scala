package controllers

import infrastructure.persistence.repositories.ExpenseRepository
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import org.scalamock.scalatest.MockFactory
import play.api.mvc.Results

/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  *
  * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
  */
class HomeControllerSpec extends PlaySpec with Results with MockFactory {

  "HomeController GET" should {

    "render the appSummary resource from a new instance of controller" in {
      val controller = new HomeController(stubControllerComponents())
      val home = controller.appSummary().apply(FakeRequest(GET, "/summary"))

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
      val resultJson = contentAsJson(home)
      resultJson.toString() mustBe """{"content":"Scala Play React Seed"}"""
    }

    "render the appSummary resource from the application" in {
      val controller = new HomeController(stubControllerComponents())
      val home = controller.appSummary().apply(FakeRequest(GET, "/summary"))

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
      val resultJson = contentAsJson(home)
      resultJson.toString() mustBe """{"content":"Scala Play React Seed"}"""
    }
  }
}
