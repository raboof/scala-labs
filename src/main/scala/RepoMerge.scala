
trait RepoMerge {
  def merge(parentBranch: String, childBranch: String): Boolean
}
