package mrlacnik

object Main extends App {
  Presenter(Mrlacnik(), presentShort = ArgParser(args).isShort)
}
