import org.eclipse.jgit.api.{Git, GitCommand}
import org.eclipse.jgit.revwalk._
import org.eclipse.jgit.storage.file._

object GitRepo extends Repo {
  private val repo = new FileRepositoryBuilder()
    .setGitDir(new java.io.File(".git"))
    .readEnvironment()
    .findGitDir()
    .build()
  private val walk = new RevWalk(repo);

  private def isParent(parent: RevCommit, child: RevCommit, maxDepth: Int = 50): Boolean = {
    if (parent.equals(child)) {
      true
    } else if (maxDepth == 0) {
      false
    } else {
      val parents = Option(walk.parseCommit(child.getId()).getParents)
      parents.map(_.filter(p => isParent(parent, p, maxDepth - 1)).nonEmpty).getOrElse(false)
    }
  }

  def isParent(parentBranch: String, childBranch: String): Boolean = {
    // TODO: Take into account remotes/origin/[branch-name]
    val childCommit = walk.parseCommit(repo.getRef("refs/heads/" + childBranch).getObjectId())
    val parentCommit = walk.parseCommit(repo.getRef("refs/heads/" + parentBranch).getObjectId())

    isParent(parentCommit, childCommit)
  }
  
  private def distanceTo(parent: RevCommit, child: RevCommit, maxDepth: Int = 50): Option[Int] = {
    if (parent.equals(child)) {
      Some(0)
    } else if (maxDepth == 0) {
      None
    } else {
      val parents = Option(walk.parseCommit(child.getId()).getParents)
      parents
        .flatMap(_.flatMap(p => distanceTo(parent, p, maxDepth - 1)).reduceOption(_ min _))
        .map(_ + 1)
    }
  }

  def distanceTo(parentBranchName: String, childBranchName: String): Option[Int] = {
    // TODO: Take into account remotes/origin/[branch-name]
    val childCommit = walk.parseCommit(repo.getRef("refs/heads/" + childBranchName).getObjectId())
    val parentCommit = walk.parseCommit(repo.getRef("refs/heads/" + parentBranchName).getObjectId())

    distanceTo(parentCommit, childCommit)
  }
}