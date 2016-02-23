import org.eclipse.jgit.revwalk._
import org.eclipse.jgit.storage.file._

object GitRepo {
  private val repo = new FileRepositoryBuilder()
    .setGitDir(new java.io.File(".git"))
    .readEnvironment()
    .findGitDir()
    .build()
  private val walk = new RevWalk(repo);

  private def isParent(parent: RevCommit, child: RevCommit, maxDepth: Int = 50): Boolean = {
    if (parent.equals(child)) {
      true;
    } else if (maxDepth == 0) {
      false;
    } else {
      val parents = Option(walk.parseCommit(child.getId()).getParents)
      parents.map(_.filter(p => isParent(parent, p, maxDepth - 1)).nonEmpty).getOrElse(false)
    }
  }

  def isParent(parentBranch: String, childBranch: String): Boolean = {
    val childCommit = walk.parseCommit(repo.getRef("refs/heads/" + childBranch).getObjectId())
    val parentCommit = walk.parseCommit(repo.getRef("refs/heads/" + parentBranch).getObjectId())

    isParent(parentCommit, childCommit)
  }
}
