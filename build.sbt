libraryDependencies ++= Seq(
  "org.eclipse.jgit"  % "org.eclipse.jgit.pgm" % "3.7.1.201504261725-r",
  "ch.qos.logback"    % "logback-classic"      % "1.1.5",
  "org.scalatest"    %% "scalatest"            % "3.0.1" % "test",
  "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3-1"
)

scalaVersion := "2.11.8"

assemblyMergeStrategy in assembly := {
  case PathList("plugin.properties", xs @ _*) => MergeStrategy.discard
  case x => {
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
  }
}
