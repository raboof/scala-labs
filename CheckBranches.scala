import org.eclipse.jgit.revwalk._
import org.eclipse.jgit.storage.file._

object CheckBranches extends App {
  val course = Seq(
    "given" -> "basic02",
    "basic02" -> "basic03",
    "basic03" -> "basic04",
    "basic04" -> "basic-final"
  )

  
  val repo = new FileRepositoryBuilder()
    .setGitDir(new java.io.File(".git"))
    .readEnvironment()
    .findGitDir()
    .build()
  val walk = new RevWalk(repo);

  course.foreach {
    case (givenBranch, solutionBranch) => 
      val solutionCommit = walk.parseCommit(repo.getRef("refs/heads/" + solutionBranch).getObjectId());
      val givenCommit = walk.parseCommit(repo.getRef("refs/heads/" + givenBranch).getObjectId());
      if (!isParent(givenCommit, solutionCommit)) {
        println(s"git checkout $solutionBranch && git merge -m 'propagate changes' $givenBranch")
      }
  } 

  def isParent(parent: RevCommit, child: RevCommit): Boolean = {
    if (parent.equals(child)) {
        true;
    } else {
        val parents = Option(child.getParents())
        parents.map(_.filter(p => isParent(parent, p)).nonEmpty).getOrElse(false)
    }
  }
}
