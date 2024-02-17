addSbtPlugin("org.jetbrains" % "sbt-ide-settings" % "1.1.0")
addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "7.4.0")

libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.33"
addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "4.2.1")