libraryDependencies ++= Seq(
  "org.eclipse.jgit"  % "org.eclipse.jgit.pgm" % "3.7.1.201504261725-r",
  "ch.qos.logback"    % "logback-classic"      % "1.1.5",
  "org.scalatest"    %% "scalatest"            % "3.0.1" % "test"
)

scalaVersion := "2.12.1"

assemblyMergeStrategy in assembly := {
  case PathList("plugin.properties", xs @ _*) => MergeStrategy.discard
  case x => {
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
  }
}
