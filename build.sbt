name := "with-kakeibo-back"
version := "0.1"

scalaVersion := "2.13.12"

idePackagePrefix := Some("work.withkakeibo")

val PekkoVersion = "1.0.2"
val PekkoHttpVersion = "1.0.1"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "4.2.1",
  "org.scalikejdbc" %% "scalikejdbc-test" % "4.2.1" % "test",
  "org.hsqldb" % "hsqldb" % "2.5.0",
  "ch.qos.logback" % "logback-classic" % "1.4.14",
  "org.apache.pekko" %% "pekko-actor-typed" % PekkoVersion,
  "org.apache.pekko" %% "pekko-stream" % PekkoVersion,
  "org.apache.pekko" %% "pekko-http" % PekkoHttpVersion
)

enablePlugins(FlywayPlugin)

flywayUrl := s"jdbc:hsqldb:file:${baseDirectory.value}/target/db/with_kakeibo;sql.syntax_mys=true"
flywayUser := "sa"
flywayPassword := ""
Test / flywayUrl := s"jdbc:hsqldb:file:${baseDirectory.value}target/db/with_kakeibo"
Test / flywayUser := "SA"
Test / flywayLocations := Seq("filesystem:src/main/resources/db/migration")
Test / flywayPassword := ""



