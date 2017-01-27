object Main extends App {
  def usage() {
    println("Usage:")
    println("  java -jar foo.jar <command>")
    println("Where <command> is one of:")
    println("  status - show status of branches")
    println("  check - check whether branches are up-to-date")
  }

  args.headOption match {
    case Some("status") =>
      Status.determineStatus(Courses.basic).map {
        case Status.UpToDate(from, to) => s"$from -> $to: up-to-date"
      }.foreach(println)
    case Some("check") =>
      CheckBasicCourse.run()
    case Some("package") =>
      // TODO select course
      Package.run(Courses.basic)
    case _ =>
      usage()
  }
}
