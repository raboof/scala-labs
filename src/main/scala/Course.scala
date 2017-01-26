case class Step(fromBranch: String, toBranch: String)
object Step {
  implicit def fromPair(pair: (String, String)): Step = Step(pair._1, pair._2)
}

case class Course(
  name: String,
  steps: Seq[(String, String)]
)
object Courses {
  // TODO of course read this from a configuration file:
  val basic = Course("Basic Scala",
    steps = Seq(
      "given" -> "basic02",
      "basic02" -> "basic03",
      "basic03" -> "basic04",
      "basic04" -> "basic-final")
  )
}
