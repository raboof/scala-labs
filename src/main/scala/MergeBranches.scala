class MergeBranches(repo: RepoMerge) {

  def mergeDirtyBranches(courseSteps: Seq[(String, String)]) = {
    val dirtySteps = CheckBranches(GitRepo).checkDirtySteps(courseSteps)

    for ((from, to) <- dirtySteps) {
      if (!repo.merge(from, to)) {
        println("Could not merge $from into $to. Please help!")
        System.exit(2)
      }
    }
  }
}

object MergeBranches {
  def apply(repo: RepoMerge): MergeBranches = new MergeBranches(repo)
}