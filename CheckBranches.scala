object CheckBranches extends App {
  val course = Seq(
    "given" -> "basic02",
    "basic02" -> "basic03",
    "basic03" -> "basic04",
    "basic04" -> "basic-final"
  )
  
  course.foreach {
    case (givenBranch, solutionBranch) => 
      if (!GitRepo.isParent(givenBranch, solutionBranch)) {
        println(s"git checkout $solutionBranch && git merge -m 'propagate changes' $givenBranch")
      }
  } 

}
