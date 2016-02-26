object CheckBranches {
  case class Graphs(desiredGraph: Graph[String], actualGraph: Graph[String]) {
    def recommendedMerges(node: String, desiredParent: String): Vector[(String, String)] = {
      val parentMerges = recommendedMerges(desiredParent)
      if (parentMerges.isEmpty && actualGraph.parents(node).contains(desiredParent)) Vector.empty
      else parentMerges :+ (desiredParent -> node)
    }

    def recommendedMerges(node: String): Vector[(String, String)] = {
      desiredGraph
        .parents.get(node).getOrElse(Nil)
        .toVector
        .flatMap(recommendedMerges(node, _))
    }

    lazy val merges = desiredGraph
      .leafs
      .flatMap(recommendedMerges)
      .foldLeft(List.empty[(String, String)])((acc, e) => if (acc.contains(e)) acc else acc :+ e)
  }

  def checkBranches(courseSteps: Seq[(String, String)]): String = {
    val desiredCourseGraph = Graph(courseSteps.map { case (given, solution) => (solution, given) })
    val actualGraph = desiredCourseGraph.filter {
      case (child, parent) => GitRepo.isParent(parent, child)
    }

    Graphs(desiredCourseGraph, actualGraph).merges.flatMap {
      case (desiredParent, child) =>
        List(s"git checkout $child", s"git merge -m 'propagate changes' $desiredParent")
    }.mkString(" &&\n  ")
  }
}
