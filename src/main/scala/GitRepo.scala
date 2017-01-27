import java.io.File

import org.eclipse.jgit.api.{Git, GitCommand}
import org.eclipse.jgit.revwalk._
import org.eclipse.jgit.storage.file._

object GitRepo extends Repo with RepoMerge {
  private val repo = new FileRepositoryBuilder()
    //.setGitDir(new java.io.File(".git"))
    .readEnvironment()
    .findGitDir()
    .build()
  private val walk = new RevWalk(repo)

  private def isParent(parent: RevCommit, child: RevCommit, maxDepth: Int = 50): Boolean = {
    if (parent.equals(child)) {
      true
    } else if (maxDepth == 0) {
      false
    } else {
      val parents = Option(walk.parseCommit(child.getId).getParents)
      parents.exists(_.exists(p => isParent(parent, p, maxDepth - 1)))
    }
  }

  private def gitCommit(branch: String) =
    Option(repo.getRef("refs/heads/" + branch))
      .orElse(Option(repo.getRef("refs/remotes/origin/" + branch)))
      .map(_.getObjectId)
      .map(walk.parseCommit).get

  def isClean: Boolean = new Git(repo).status().call().isClean

  def isParent(parentBranch: String, childBranch: String): Boolean = {
    val childCommit = gitCommit(childBranch)
    val parentCommit = gitCommit(parentBranch)

    isParent(parentCommit, childCommit)
  }

  def merge(parentBranch: String, childBranch: String): Boolean = {
    runCmd(Seq("git", "checkout", childBranch))
    runCmd(Seq("git", "merge", parentBranch))
    true
  }


  def runCmd(command: Seq[String], inDirectory: File = new File(".")): Unit = {
    println(command.mkString(" "))
    val result: Int = sys.process.Process(command, inDirectory).!

    if (result != 0) {
      throw new IllegalStateException(s"Failed to run command: $command")
    }
  }

}
