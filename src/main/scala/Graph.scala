import MultiMap._

/**
 * A directed, acyclic, not necessarily connected graph of nodes of type T.
 *
 * Vertices, as in git, point from 'child' to 'parent'.
 */
case class Graph[T] private (parents: SetMultiMap[T, T]) {
  def add(child: T, parent: T): Graph[T] = Graph[T](parents.addBinding(child, parent))

  def filter(predicate: (T, T) => Boolean) =
    Graph(parents.map {
      case (child, parents) => (child, parents.filter(parent => predicate(child, parent)))
    })

  /** nodes without children */
  lazy val leafs = parents.keySet -- parents.boundValues
}

object Graph {
  def empty[T] = Graph[T](Map.empty[T, Set[T]].withDefaultValue(Set.empty[T]))

  /** Create graph based on child -> parent mappings */
  def apply[T](seq: Seq[(T, T)]): Graph[T] = seq.foldLeft(empty[T])((acc, t) => acc.add(t._1, t._2))
}
