import org.scalatest._

class MergedGraphsSpec extends FlatSpec with Matchers {
  import CheckBranches._

  "Graphs" should "suggest a simple merge" in {
    val desiredGraph = Graph(Seq("solution" -> "given"))
    val actualGraph = Graph.empty[String]

    mergedGraphs(desiredGraph, actualGraph) should contain theSameElementsAs (Seq("given" -> "solution"))
  }

  it should "suggest a transitive merge" in {
    val desiredGraph = Graph(Seq("intermediate" -> "given", "solution" -> "intermediate"))
    val actualGraph = Graph(Seq("solution" -> "intermediate"))

    mergedGraphs(desiredGraph, actualGraph) should contain theSameElementsInOrderAs (Seq("given" -> "intermediate", "intermediate" -> "solution"))
  }

  it should "suggest merges in separate parts of a disconnected graph" in {
    val desiredGraph = Graph(Seq("some" -> "other", "foo" -> "bar"))
    val actualGraph = Graph.empty[String]

    mergedGraphs(desiredGraph, actualGraph) should contain theSameElementsAs (Seq("other" -> "some", "bar" -> "foo"))
  }

}
