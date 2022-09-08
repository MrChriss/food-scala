package mrlacnik

import Presenter._

object Presenter {
  private val menuDateSeparator = "="
  private val menuItemLineSeparator= "-"
  private val priceSpacePadding = 4

  def apply(eitherMenu: Either[Mrlacnik.ScrapeError, Mrlacnik.Menu], presentShort: Boolean): Unit = {
    new Presenter(eitherMenu, presentShort).present()
  }
}

final class Presenter(eitherMenu: Either[Mrlacnik.ScrapeError, Mrlacnik.Menu], presentShort: Boolean) {
  private def present() = {
    eitherMenu match {
      case Left(scrapeError) => presentScrapeError(scrapeError.errorMessage)
      case Right(menu) => {
        presentDate(menu)
        if (presentShort) presentShortMenu(menu) else presentLongMenu(menu)
      }
    }
  }

  private def presentScrapeError(errorMessage: String) = {
    println(s"=> Error: $errorMessage")
  }

  private def presentShortMenu(menu: Mrlacnik.Menu) = {
    menu.menuItems.foreach(menuItem => println(s"- ${menuItem.dish}\n\n"))
  }

  private def presentLongMenu(menu: Mrlacnik.Menu) = {
    val maxMenuEntrySize: Int = menu.menuItems.map(_.dish).maxBy(_.size).size

    menu.menuItems.foreach(
      menuItem => {
        val extraWhitespace = " " * {maxMenuEntrySize - menuItem.dish.size + priceSpacePadding}
        val formattedDishWithPrice = menuItem.dish + extraWhitespace + menuItem.price
        println(formattedDishWithPrice)
        println(menuItemLineSeparator * formattedDishWithPrice.size)
      }
    )
  }

  private def presentDate(menu: Mrlacnik.Menu) = {
    println(menu.date)
    println(menuDateSeparator * menu.date.length())
  }
}
