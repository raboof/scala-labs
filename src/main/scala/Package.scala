import java.io.File
import scalax.file.Path
import scalax.file.defaultfs.DefaultPath

import sys.process._

import org.eclipse.jgit.lib._
import org.eclipse.jgit.api._
import org.eclipse.jgit.storage.file._

object Package {
  def run(course: Course) = {
    val branches = course.steps.flatMap { case (from, to) => List(from, to) }

    val to = createShallowClone(branches.head)

    branches.tail.foreach { branch => {
        // jgit can't do shallow fetches yet, https://bugs.eclipse.org/bugs/show_bug.cgi?id=475615
        runCmd(Seq("git", "remote", "set-branches", "origin", branch), to.jfile)
        // TODO determine the Depth to make sure it includes the parent
        runCmd(Seq("git", "fetch", "--depth", "1", "origin", branch), to.jfile)
        runCmd(Seq("git", "checkout", branch), to.jfile)
      }
    }
    runCmd(Seq("git", "checkout", branches.head), to.jfile)
    runCmd(Seq("git", "remote", "remove", "origin"), to.jfile)

    // TODO
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
