name := "stats"

version := "1.0"

scalaVersion  := "2.11.8"

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature")

libraryDependencies ++= {
  val akkaV = "2.4.2"
  val sprayV = "1.3.3"
  val json4sV = "3.3.0"
  val reactiveMongoV = "0.11.14"
  val scalaTestV = "2.2.6"
  val sprayJsonV = "1.3.2"
  val nscalaTimeV = "2.10.0"
  val logbackV = "1.1.7"
  val slf4jV = "1.7.21"
  
  Seq(
    "org.reactivemongo" %% "reactivemongo" % reactiveMongoV,
    "io.spray"            %%   "spray-can"     % sprayV,
    "io.spray"            %%   "spray-routing" % sprayV,
    "io.spray"            %%   "spray-http"    % sprayV,
    "io.spray"            %%   "spray-util"    % sprayV,
    "io.spray"            %%   "spray-httpx"   % sprayV,
    "io.spray"            %%  "spray-json"     % sprayJsonV,
    "io.spray"            %%  "spray-client"   % sprayV,
    "io.spray"            %%   "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"     % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"   % akkaV   % "test",
    "org.scalatest"       %%  "scalatest"      % scalaTestV % "test",
    "com.github.nscala-time" %% "nscala-time"  % nscalaTimeV,
    "org.json4s"          %% "json4s-native"   % json4sV,
    "org.json4s"          %% "json4s-jackson"  % json4sV,
    "org.json4s"          %% "json4s-ext"      % json4sV,
    "org.slf4j" % "slf4j-api" % slf4jV,
    "ch.qos.logback" % "logback-core" % logbackV,
    "ch.qos.logback" % "logback-classic" % logbackV,
    "net.logstash.logback" % "logstash-logback-encoder" % "4.7",
    "com.typesafe.akka" %% "akka-slf4j" % akkaV
  )
}

Defaults.itSettings

lazy val `stats` = project.in(file(".")).
  enablePlugins(BuildInfoPlugin).
  settings(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "me.reminisce.stats.server"
  )

buildInfoOptions += BuildInfoOption.BuildTime
buildInfoOptions += BuildInfoOption.ToJson

buildInfoKeys ++= Seq[BuildInfoKey](
  BuildInfoKey.action("commitHash") {
    Process("git rev-parse HEAD").lines.head
  }
)

resolvers ++= Seq("spray" at "http://repo.spray.io/")

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

resolvers ++= Seq("Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/")

resolvers ++= Seq("Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/")

resolvers ++= Seq("slf4j on Maven" at "https://mvnrepository.com/artifact/org.slf4j/slf4j-api")
resolvers ++= Seq("logback-core on Maven" at "https://mvnrepository.com/artifact/ch.qos.logback/logback-core")
resolvers ++= Seq("logback-classic on Maven" at "https://mvnrepository.com/artifact/ch.qos.logback/logback-classic")
resolvers ++= Seq("Logback logstash interface" at "https://mvnrepository.com/artifact/net.logstash.logback/logstash-logback-encoder")

assemblyJarName in assembly := "stats.jar"

coverageHighlighting := false
parallelExecution in Test := false
Revolver.settings

test in assembly := {}
