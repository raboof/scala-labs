class CheckBranches(repo: Repo) {
  import CheckBranches._

  def checkDirtySteps(courseSteps: Seq[(String, String)]): Seq[(String, String)] = {
    val desiredCourseGraph = Graph(courseSteps.map { case (given, solution) => (solution, given) })

    val actualGraph = desiredCourseGraph.filter {
      case (child, parent) => repo.isParent(parent, child)
    }

    mergedGraphs(desiredCourseGraph, actualGraph)
  }
}

object CheckBranches {

  def apply(repo: Repo): CheckBranches = new CheckBranches(repo)

  def mergedGraphs(desiredGraph: Graph[String], actualGraph: Graph[String]): Seq[(String, String)] = {
    def recommendedMerges(node: String, desiredParent: String): Vector[(String, String)] = {
      val parentMerges = merge(desiredParent)
      if (parentMerges.isEmpty && actualGraph.parents(node).contains(desiredParent)) Vector.empty
      else parentMerges :+ (desiredParent -> node)
    }

    def merge(node: String): Vector[(String, String)] = {
      desiredGraph
        .parents(node)
        .toVector
        .flatMap(recommendedMerges(node, _))
    }

    desiredGraph
      .leafs
      .flatMap(merge)
      .foldLeft(List.empty[(String, String)])((acc, e) => if (acc.contains(e)) acc else acc :+ e)
  }

}
