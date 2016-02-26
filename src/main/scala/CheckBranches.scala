object CheckBranches extends App {
  val courseSteps = Seq(
    "given" -> "basic02",
    "basic02" -> "basic03",
    "basic03" -> "basic04",
    "basic04" -> "basic-final"
  )

  val desiredCourseGraph = Graph(courseSteps.map { case (given, solution) => (solution, given) })
  val actualGraph = desiredCourseGraph.filter {
    case (child, parent) => GitRepo.isParent(parent, child)
  }

  def recommendedMerges(node: String, desiredParent: String): Vector[(String, String)] = {
    val parentMerges = recommendedMerges(desiredParent)
    if (parentMerges.isEmpty && actualGraph.parents(node).contains(desiredParent)) Vector.empty
    else parentMerges :+ (desiredParent -> node)
  }

  def recommendedMerges(node: String): Vector[(String, String)] = {
    desiredCourseGraph
      .parents.get(node).getOrElse(Nil)
      .toVector
      .flatMap(recommendedMerges(node, _))
  }

  val merges = desiredCourseGraph
    .leafs
    .flatMap(recommendedMerges)
    .foldLeft(List.empty[(String, String)])((acc, e) => if (acc.contains(e)) acc else acc :+ e)

  val commands = merges.flatMap {
    case (desiredParent, child) =>
      List(s"git checkout $child", s"git merge -m 'propagate changes' $desiredParent")
  }.mkString(" && ")

  println(commands)
}
