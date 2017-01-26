object CheckBasicCourse {
  import CheckBranches._

  def run() = {
    val mergeCommands = checkBranches(Courses.basic.steps)
    if (mergeCommands.isEmpty) {
      println("Branches are up-to-date!")
    } else {
      println("Some branches are not up-to-date with their parents. Recommended merges:")
      println(mergeCommands)
    }
  }
}
