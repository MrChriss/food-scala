package mrlacnik

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.browser._
import scala.util.chaining._

// https://index.scala-lang.org/ruippeixotog/scala-scraper
object Mrlacnik {
  def scrapeMenu(): Menu = {
    val doc = JsoupBrowser().get(mrlacnikUrl)

    Menu(menuDate(doc), menuItems(doc))
  }

  final case class MenuItem(dish: String, price: String)
  final case class Menu(date: String, menuItems: List[MenuItem])

  private val mrlacnikUrl = "https://www.kasca-mrlacnik.jedilnilist.si/stran/malica/"
  private val menuCssSelector = "div.row.gutters > div.columns > h6"
  private val dateCssSelector = "div.row.gutters > div.columns > h6 > span:nth-child(1)"

  private def extractItems(doc: Browser#DocumentType, cssSelector: String): List[String] = {
    doc.extract(elementList(cssSelector)).map(_.text).filterNot(_.isBlank)
  }

  // List(("spaghetti", "2,00$"), ...)
  private def menuItems(doc: Browser#DocumentType): List[MenuItem] = {
    extractItems(doc, menuCssSelector)
    .drop(1) // first element is the date and must be dropped
    .flatMap(_.split('€'))
    .dropRight(1)
    .map(_.strip.appended('€'))
    .map(_.replaceAll("""\([A-Za-z,\.]+\)""", " "))
    .map(
      _.split("""\s(?=\d)""")
       .pipe { case Array(dish, price) => MenuItem(dish.strip, price.replaceAll("""[[:space:]]*""", "")) }
    )
  }

  // returns: Ponedeljek, 5.9.2022
  private def menuDate(doc: Browser#DocumentType): String = {
    extractItems(doc, dateCssSelector).head
  }
}
