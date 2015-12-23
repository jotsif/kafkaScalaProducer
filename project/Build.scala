import sbt._
import Keys._
import sbtassembly.Plugin._
import sbtdocker.DockerKeys._

object KafkaTest extends Build {
  lazy val kafkaTestSettings = Defaults.defaultSettings ++ Seq(
    name := "KafkaProducerTest",
    scalaVersion := "2.11.7",
    libraryDependencies ++= Seq(
      "org.apache.kafka" %% "kafka" % "0.8.2.2"
    )
  )

  lazy val project_name = "kafka_producer_test"

  lazy val root = Project(id = project_name,
    base = file("."),
    settings = kafkaTestSettings ++ dockerSettings ++ assemblySettings
  )
    .enablePlugins(sbtdocker.DockerPlugin)

  lazy val dockerSettings = Seq(
    docker <<= (docker.dependsOn(AssemblyKeys.assembly)),
    dockerfile in docker := {
      val artifact = (AssemblyKeys.outputPath in AssemblyKeys.assembly).value
      val artifactTargetPath = s"/app/${artifact.name}"
      new sbtdocker.mutable.Dockerfile {
        from("appdata/java8:1.8.51-16")
        add(artifact, artifactTargetPath)
        env("JAR_NAME", artifactTargetPath)
        cmd("/opt/jre/bin/java", "-cp", artifactTargetPath, "biz.appdata.kafkaproducertest.KafkaProducerTest")
      }
    },
    imageNames in docker := Seq(sbtdocker.ImageName(repository = project_name, tag = Some(version.value)))
  )
}
