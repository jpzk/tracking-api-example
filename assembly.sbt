import AssemblyKeys._

assemblySettings
mergeStrategy in assembly := {
  case "application.conf"                           => MergeStrategy.concat
  case "unwanted.txt"                                => MergeStrategy.discard
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case x => MergeStrategy.first
}
jarName in assembly := "tracking-api.jar"

