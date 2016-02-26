object MultiMap {
  type SetMultiMap[A, B] = Map[A, Set[B]]

  implicit class SetMultiMapOps[A, B](val map: SetMultiMap[A, B]) extends AnyVal {
    def addBinding(key: A, value: B): SetMultiMap[A, B] =
      map + (key -> { map.getOrElse(key, Set.empty[B]) + value })

    def boundValues: Set[B] = map.values.fold(Set.empty)(_ ++ _)
  }

  object SetMultiMap {
    def apply[A, B](seq: Seq[(A, B)]): SetMultiMap[A, B] =
      seq.foldLeft(Map[A, Set[B]]().withDefaultValue(Set.empty[B]))((acc, t) => acc.addBinding(t._1, t._2))
  }
}
