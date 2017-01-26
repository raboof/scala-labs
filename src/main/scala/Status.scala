object Status {
  sealed trait StatusLine
  case class UpToDate(from: String, to: String) extends StatusLine

  def determineStatus(course: Course): Seq[StatusLine] =
    course.steps.map {
      case (from, to) =>
        // TODO actually check if steps are up-to-date :)
        UpToDate(from, to)
      }
}
