object CheckBasicCourse {

  def run() = {
    val mergeCommands = CheckBranches(GitRepo).checkDirtySteps(Courses.basic.steps)
    if (mergeCommands.isEmpty) {
      println("Branches are up-to-date!")
    } else {
      println("Some branches are not up-to-date with their parents. Recommended merges:")
      println(mergeCommands)
    }
  }
}
