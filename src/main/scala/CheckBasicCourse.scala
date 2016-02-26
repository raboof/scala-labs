object CheckBasicCourse extends App {
  import CheckBranches._

  val courseSteps = Seq(
    "given" -> "basic02",
    "basic02" -> "basic03",
    "basic03" -> "basic04",
    "basic04" -> "basic-final"
  )

  val mergeCommands = checkBranches(courseSteps)
  if (mergeCommands.isEmpty) {
    println("Branches are up-to-date!")
  } else {
    println("Some branches are not up-to-date with their parents. Recommended merges:")
    println(mergeCommands)
  }
}
