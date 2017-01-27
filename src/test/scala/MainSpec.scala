import org.scalatest.{FlatSpec, Matchers}

class MainSpec extends FlatSpec with Matchers {

  it should "print usage on no command" in {
    Main.main(Array())
  }

  it should "print usage on invalid command" in {
    Main.main(Array("foo"))
  }

  it should "work on a clean repo" in {
    Main.main(Array("status"))
  }

}
