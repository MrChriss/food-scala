package mrlacnik

import Presenter._

object Presenter {
  private val menuDateSeparator = "="
  private val menuItemLineSeparator= "-"
  private val priceSpacePadding = 4

  def apply(menu: Mrlacnik.Menu, presentShort: Boolean): Unit = new Presenter(menu, presentShort).present()
}

final class Presenter(menu: Mrlacnik.Menu, presentShort: Boolean) {
  private def present() = {
    presentDate()
    if (presentShort) presentShortMenu() else presentLongMenu()
  }

  private def presentShortMenu() = {
    menu.menuItems.foreach(menuItem => println(s"- ${menuItem.dish}\n\n"))
  }

  private def presentLongMenu() = {
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

  private def presentDate() = {
    println(menu.date)
    println(menuDateSeparator * menu.date.length())
  }
}
