package mrlacnik

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import scala.util.chaining._

// https://index.scala-lang.org/ruippeixotog/scala-scraper
final object Mrlacnik {
  def apply(): Mrlacnik = new Mrlacnik
}

final class Mrlacnik {
  private final val mrlacnikUrl = "https://www.kasca-mrlacnik.jedilnilist.si/stran/malica/"
  private final val menuCssSelector = "div.row.gutters > div.columns > h6"
  private final val dateCssSelector = "div.row.gutters > div.columns > h6 > span:nth-child(1)"

  private final lazy val doc =  JsoupBrowser().get(mrlacnikUrl)

  private def extractItems(cssSelector: String): List[String] = {
    doc.extract(elementList(cssSelector)).map(_.text).filterNot(_.isBlank)
  }

  type MenuItemWithPrice = (String, String)
  type MenuItemsWithPrice = List[MenuItemWithPrice]

  // List(("spaghetti", "2,00$"), ...)
  def menuItems(): MenuItemsWithPrice = {
    extractItems(menuCssSelector)
    .drop(1) // first element is the date and must be dropped
    .flatMap(_.split('€'))
    .dropRight(1)
    .map(_.strip.appended('€'))
    .map(_.replaceAll("""\([A-Za-z,\.]+\)""", " "))
    .map(
      _.split("""\s(?=\d)""")
       .pipe { case Array(dish, price) => (dish.strip, price.replaceAll("""[[:space:]]*""", "")) }
    )
  }

  // returns: Ponedeljek, 5.9.2022
  def menuDate(): String = {
    extractItems(dateCssSelector).head
  }
}
