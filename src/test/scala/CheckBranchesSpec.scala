import org.scalatest.{FlatSpec, Matchers}

class CheckBranchesSpec extends FlatSpec with Matchers {

  val course = Course("Basic Scala",
    steps = Seq(
      "given" -> "basic02",
      "basic02" -> "basic03",
      "basic03" -> "basic04",
      "basic04" -> "basic-final")
  )

  it should "compare clean branches from a repository" in {
    val checkBranches = CheckBranches((parentBranch, childBranch) => true)
    checkBranches.checkDirtySteps(course.steps) should contain theSameElementsInOrderAs List()
  }

  it should "compare all dirty branches from a repository" in {
    val checkBranches = CheckBranches((parentBranch, childBranch) => false)
    checkBranches.checkDirtySteps(course.steps) should contain theSameElementsInOrderAs course.steps
  }

}
