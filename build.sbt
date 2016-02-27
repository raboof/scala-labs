libraryDependencies ++= Seq(
  "org.eclipse.jgit"  % "org.eclipse.jgit.pgm" % "3.7.1.201504261725-r",
  "ch.qos.logback"    %  "logback-classic"     % "1.1.5",
  "org.scalatest"    %% "scalatest"            % "2.2.6" % "test"
)

mainClass in Compile := Some("CheckBasicCourse")
