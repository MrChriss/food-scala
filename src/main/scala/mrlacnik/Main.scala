package mrlacnik

object Main extends App {
  Presenter(Mrlacnik.scrapeMenu(), presentShort = ArgParser(args).isShort)
}
