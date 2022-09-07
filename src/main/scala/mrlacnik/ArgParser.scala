package mrlacnik

// https://jodersky.github.io/scala-argparse/index.html#tutorial
final object ArgParser {
  def apply(args: Array[String]): ArgParser = new ArgParser(args)
}

final class ArgParser(args: Array[String]) {
  final lazy val isShort = long.value ^ short.value

  private val parser = argparse.default.ArgumentParser(description = """Presents "Kašča Mrlačnik" lunchmenu for the current day to the terminal""")

  private val long = parser.param[Boolean](
    name = "-l",
    aliases = Seq("--long"),
    default = false,
    flag = true,
    help = "Present the menu with prices"
  )

  private val short = parser.param[Boolean](
    name = "-s",
    aliases = Seq("--short"),
    default = true,
    flag = true,
    help = "Present only menu items"
  )

  parser.parseOrExit(args)
}
