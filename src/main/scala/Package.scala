import java.io.File
import scalax.file.Path
import scalax.file.defaultfs.DefaultPath

import sys.process._

import org.eclipse.jgit.lib._
import org.eclipse.jgit.api._
import org.eclipse.jgit.storage.file._

object Package {
  def run(course: Course) = {
    val branches = course.steps
    val to = createShallowClone(branches.head._1)

    branches.foreach { case (given, solution) => {
        // jgit can't do shallow fetches yet, https://bugs.eclipse.org/bugs/show_bug.cgi?id=475615
        runCmd(Seq("git", "remote", "set-branches", "origin", solution), to.jfile)

        // Actually getting the current depth does not seem to work immediately
        runCmd(Seq("git", "fetch", "--depth", "1", "origin", solution), to.jfile)
        runCmd(Seq("git", "checkout", solution), to.jfile)

        // So get them now
        runCmd(Seq("git", "fetch", "--depth", GitRepo.distanceTo(given, solution).map(_ + 1).getOrElse(1).toString, "origin", solution), to.jfile)
      }
    }
    runCmd(Seq("git", "checkout", branches.head._1), to.jfile)
    // runCmd(Seq("git", "remote", "remove", "origin"), to.jfile)

    // TODO actually create a zip
  }

  def createShallowClone(firstBranch: String): DefaultPath = {
    val to = Path.fromString("dist")

    to.deleteRecursively(continueOnFailure = false, force = true)

    val from = new File(".").getAbsolutePath()

    // jgit can't do shallow clones yet, https://bugs.eclipse.org/bugs/show_bug.cgi?id=475615
    runCmd(Seq("git", "clone", "--depth", "1", "--branch", firstBranch, s"file://$from", to.name))

    to
  }

  def runCmd(command: Seq[String], inDirectory: File = new File(".")): Unit = {
    println(command.mkString(" "))
    val result: Int = sys.process.Process(command, inDirectory).!

    if (result != 0) {
      throw new IllegalStateException(s"Failed to run command: $command")
    }
  }
}
