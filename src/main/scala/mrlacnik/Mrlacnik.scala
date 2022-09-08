package mrlacnik

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.browser._
import scala.util.chaining._

import scala.util.Try

// https://index.scala-lang.org/ruippeixotog/scala-scraper
object Mrlacnik {
  def scrapeMenu(): Either[ScrapeError, Menu] = {
    for {
      doc   <- Try(JsoupBrowser().get(mrlacnikUrl)).toEither.left.map(cause => ScrapeError(s"Network error: $cause"))
      date  <- menuDate(doc)
      items <- menuItems(doc)
    } yield Menu(date, items)
  }

  final case class MenuItem(dish: String, price: String)
  final case class Menu(date: String, menuItems: List[MenuItem])
  final case class ScrapeError(errorMessage: String)

  private val mrlacnikUrl = "https://www.kasca-mrlacnik.jedilnilist.si/stran/malica/"
  private val menuCssSelector = "div.row.gutters > div.columns > h6"
  private val dateCssSelector = "div.row.gutters > div.columns > h6 > span:nth-child(1)"

  private def extractItems(doc: Browser#DocumentType, cssSelector: String): List[String] = {
    doc.extract(elementList(cssSelector)).map(_.text).filterNot(_.isBlank)
  }

  private def menuItems(doc: Browser#DocumentType): Either[ScrapeError, List[MenuItem]] = {
    // Left(ScrapeError("Failed to scrape menu items"))
    Right(extractItems(doc, menuCssSelector)
    .drop(1)               // drop first element (which is the date, and not a menu item)
    .flatMap(_.split('€')) // handle first two menu items which are one string due to bad markup by the website maintainers
    .dropRight(1)          // last element is not needed
    .map(_.strip.appended('€')) // remove whitespace and append '€', to get consistent price format at the end of each menu item
    .map(_.replaceAll("""\([A-Za-z,\.]+\)""", " ")) // remove information about allergenes
    .map(
      _.split("""\s(?=\d)""")
       .pipe { case Array(dish, price) => MenuItem(dish.strip, price.replaceAll("""[[:space:]]*""", "")) }
    )) // split dish name and price, so that they can be presented better, remove whitespace from price
  }

  // returns: Ponedeljek, 5.9.2022
  private def menuDate(doc: Browser#DocumentType): Either[ScrapeError, String] = {
    extractItems(doc, dateCssSelector).headOption match {
      case Some(date) => Right(date)
      case None => Left(ScrapeError("Date scraping failed"))
    }
  }
}
