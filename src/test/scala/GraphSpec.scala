import org.scalatest._

class GraphSpec extends FlatSpec with Matchers {
  "Graph" should "correctly determine leaf nodes" in {
    Graph(Vector("child" -> "parent")).leafs should contain("child")
    Graph(Vector("child" -> "parent", "otherChild" -> "parent")).leafs should contain theSameElementsAs (Set("child", "otherChild"))
    Graph(Vector("intermediate" -> "parent", "child" -> "intermediate")).leafs should contain theSameElementsAs (Set("child"))
  }
}
