package mrlacnik

final object Presenter {
  def apply(m: Mrlacnik, presentShort: Boolean): Unit = new Presenter(m, presentShort).present()
}

final class Presenter(mrlacnik: Mrlacnik, presentShort: Boolean) {
  private def present() = {
    presentDate()
    if (presentShort) presentShortMenu() else presentLongMenu()
  }

  private final val menuDateSeparator = "="
  private final val menuItemLineSeparator= "-"
  private final val priceSpacePadding = 4
  private final lazy val maxMenuEntrySize: Int = { mrlacnik.menuItems().map(_._1).maxBy(_.size).size }
  // private def menuEntryJustificationSize(): Int = {
  //   mrlacnik.menuItems().map(_._1).maxBy(_.size).size
  // }

  private def presentShortMenu() = {
    mrlacnik.menuItems().foreach { case(dish, price) => println(s"- $dish\n\n") }
  }

  private def presentLongMenu() = {
    mrlacnik.menuItems().foreach {
      case(dish, price) => {
        val extraWhitespace = " " * {maxMenuEntrySize - dish.size + priceSpacePadding}
        val formattedDishWithPrice = dish + extraWhitespace + price
        println(formattedDishWithPrice)
        println(menuItemLineSeparator * formattedDishWithPrice.size)
      }
    }
  }


  private def presentDate() = {
    println(mrlacnik.menuDate())
    println(menuDateSeparator * mrlacnik.menuDate.length())
  }
}
