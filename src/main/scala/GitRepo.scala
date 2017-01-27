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
      val parents = Option(walk.parseCommit(child.getId).getParents)
      parents.exists(_.exists(p => isParent(parent, p, maxDepth - 1)))
    }
  }

  def isParent(parentBranch: String, childBranch: String): Boolean = {
    def gitRefObject(branch: String) =
      Option(repo.getRef("refs/heads/" + branch))
        .orElse(Option(repo.getRef("refs/remotes/origin/" + branch)))
        .map(_.getObjectId).get

    val childCommit = walk.parseCommit(gitRefObject(childBranch))
    val parentCommit = walk.parseCommit(gitRefObject(parentBranch))

    isParent(parentCommit, childCommit)
  }
}
