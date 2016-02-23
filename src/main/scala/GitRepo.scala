import org.eclipse.jgit.revwalk._
import org.eclipse.jgit.storage.file._

object GitRepo {
  private val repo = new FileRepositoryBuilder()
    .setGitDir(new java.io.File(".git"))
    .readEnvironment()
    .findGitDir()
    .build()
  private val walk = new RevWalk(repo);

  private def isParent(parent: RevCommit, child: RevCommit): Boolean = {
    if (parent.equals(child)) {
        true;
    } else {
        val parents = Option(child.getParents())
        parents.map(_.filter(p => isParent(parent, p)).nonEmpty).getOrElse(false)
    }
  }
  
  def isParent(parentBranch: String, childBranch: String): Boolean = {
    val childCommit = walk.parseCommit(repo.getRef("refs/heads/" + childBranch).getObjectId())
    val parentCommit = walk.parseCommit(repo.getRef("refs/heads/" + parentBranch).getObjectId())

    isParent(parentCommit, childCommit)
  }
}
