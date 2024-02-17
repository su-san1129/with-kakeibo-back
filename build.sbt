name := "with-kakeibo-back"
version := "0.1"

scalaVersion := "2.13.12"

idePackagePrefix := Some("work.withkakeibo")

val PekkoVersion = "1.0.2"
val PekkoHttpVersion = "1.0.1"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "4.2.1",
  "org.scalikejdbc" %% "scalikejdbc-test" % "4.2.1" % "test",
  "org.scalikejdbc" %% "scalikejdbc-joda-time" % "4.2.1",
  "mysql" % "mysql-connector-java" % "8.0.33",
  "ch.qos.logback" % "logback-classic" % "1.4.14",
  "joda-time" % "joda-time" % "2.12.5",
  "org.apache.pekko" %% "pekko-actor-typed" % PekkoVersion,
  "org.apache.pekko" %% "pekko-stream" % PekkoVersion,
  "org.apache.pekko" %% "pekko-http" % PekkoHttpVersion
)

enablePlugins(FlywayPlugin)
enablePlugins(ScalikejdbcPlugin)

flywayUrl := s"jdbc:mysql://127.0.0.1:3306/with_kakeibo"
flywayUser := "root"
flywayPassword := "passwd"
Test / flywayUrl := s"jdbc:mysql://127.0.0.1:3306/with_kakeibo"
Test / flywayUser := "root"
Test / flywayLocations := Seq("filesystem:src/main/resources/db/migration")
Test / flywayPassword := "passwd"
