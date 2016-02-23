object CheckBranches extends App {
  val course = Seq(
    "given" -> "basic02",
    "basic02" -> "basic03",
    "basic03" -> "basic04",
    "basic04" -> "basic-final"
  )

  case class Analysis(
    recommendedCommands: List[String] = Nil,
    updatedBranches: Set[String] = Set())

  val a = course.foldLeft(Analysis()) {
    case (acc, (givenBranch, solutionBranch)) =>
      if (!GitRepo.isParent(givenBranch, solutionBranch)) {
        acc.copy(
          recommendedCommands =
            s"git merge -m 'propagate changes' $givenBranch"
            :: s"git checkout $solutionBranch"
            :: acc.recommendedCommands,
          updatedBranches = acc.updatedBranches + solutionBranch
        )
      } else acc
  }

  println(a.recommendedCommands.reverse.mkString(" && "))
}
