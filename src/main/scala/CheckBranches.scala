class CheckBranches(repo: Repo) {
  import CheckBranches._

  def checkDirtySteps(courseSteps: Seq[(String, String)]): Seq[(String, String)] = {
    val desiredCourseGraph = Graph(courseSteps.map { case (given, solution) => (solution, given) })

    val actualGraph = desiredCourseGraph.filter {
      case (child, parent) => repo.isParent(parent, child)
    }

    MergedGraphs(desiredCourseGraph, actualGraph).merges
  }
}

object CheckBranches {

  def apply(repo: Repo): CheckBranches = new CheckBranches(repo)

  case class MergedGraphs(desiredGraph: Graph[String], actualGraph: Graph[String]) {
    def recommendedMerges(node: String, desiredParent: String): Vector[(String, String)] = {
      val parentMerges = recommendedMerges(desiredParent)
      if (parentMerges.isEmpty && actualGraph.parents(node).contains(desiredParent)) Vector.empty
      else parentMerges :+ (desiredParent -> node)
    }

    def recommendedMerges(node: String): Vector[(String, String)] = {
      desiredGraph
        .parents(node)
        .toVector
        .flatMap(recommendedMerges(node, _))
    }

    lazy val merges = desiredGraph
      .leafs
      .flatMap(recommendedMerges)
      .foldLeft(List.empty[(String, String)])((acc, e) => if (acc.contains(e)) acc else acc :+ e)
  }

}
