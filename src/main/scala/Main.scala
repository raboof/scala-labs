import org.eclipse.jgit.api.Git

object Main extends App {
  def usage() {
    println("Usage:")
    println("  java -jar foo.jar <command>")
    println("Where <command> is one of:")
    println("  status  - show status of branches")
    println("  merge   - merge dirty branches")
    println("  package - create a zip of course materials")
  }

  def courseSteps = Courses.basic.steps

//  if (!GitRepo.isClean) {
//    println("The repository has uncommitted changed.")
//    println("Please clean up your repo before updating course materials")
//    System.exit(1);
//  }

  args.headOption match {
    case Some("status") =>
      val dirtySteps = CheckBranches(GitRepo).checkDirtySteps(courseSteps)
      printStepInfo(courseSteps, dirtySteps)
    case Some("merge") =>
      MergeBranches(GitRepo).mergeDirtyBranches(courseSteps)
    case Some("package") =>
      // TODO select course
      Package.run(Courses.basic)
    case _ =>
      usage()
  }

  def printStepInfo(courseSteps: Seq[(String, String)], dirtySteps: Seq[(String, String)]) = {
    println(s"course steps: ${courseSteps}")
    println(s"dirty steps: ${dirtySteps}")
    for((from, to) <- courseSteps) {
      val dirty = dirtySteps.contains((from, to))

      println(s"${if (dirty) "x " else "  "} ${from} -> ${to} is ${if (dirty) "dirty" else "okay"}.");
    }
  }


}
