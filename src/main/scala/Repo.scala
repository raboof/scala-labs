/**
 * Base trait for interacting with a repository
 */
trait Repo {

  def isParent(parentBranch: String, childBranch: String): Boolean
}
