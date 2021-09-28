val attoVersion                 = "0.9.5"
val catsEffectVersion           = "3.2.7"
val catsMtlVersion              = "1.2.1"
val catsTestkitScalaTestVersion = "2.1.5"
val catsVersion                 = "2.6.1"
val catsScalacheckVersion       = "0.3.1"
val catsTimeVersion             = "0.3.4"
val circeOpticsVersion          = "0.14.1"
val circeVersion                = "0.14.1"
val cirisVersion                = "2.1.1"
val clueVersion                 = "0.17.0"
val http4sVersion               = "0.23.3"
val http4sJdkHttpClientVersion  = "0.5.0"
val fs2Version                  = "3.1.1"
val jawnVersion                 = "1.2.0"
val kindProjectorVersion        = "0.13.2"
val logbackVersion              = "1.2.5"
val lucumaCoreVersion           = "0.13.1"
// val lucumaSsoVersion            = "0.0.9" AWAITING CE3
val log4catsVersion             = "2.1.1"
val monocleVersion              = "3.1.0"
val munitCatsEffectVersion      = "1.0.5"
val refinedVersion              = "0.9.27"
val sangriaVersion              = "2.1.3"
val sangriaCirceVersion         = "1.3.2"
val singletonOpsVersion         = "0.5.2"
val grackleVersion              = "0.1.11"
val natcchezHttp4sVersion  = "0.1.3"
val natchezVersion         = "0.1.5"

val munitVersion                = "0.7.29"
val disciplineMunitVersion      = "1.0.9"


inThisBuild(
  Seq(
    homepage := Some(url("https://github.com/gemini-hlsw/lucuma-itc")),
    Global / onChangedBuildSource := ReloadOnSourceChanges,
    addCompilerPlugin(
      ("org.typelevel" % "kind-projector" % kindProjectorVersion).cross(CrossVersion.full)
      )
    ) ++ lucumaPublishSettings
  )

lazy val commonSettings = Seq(
    libraryDependencies ++= Seq(
      "org.typelevel"     %% "cats-testkit"           % catsVersion                 % "test",
      "org.typelevel"     %% "cats-testkit-scalatest" % catsTestkitScalaTestVersion % "test"
      ),
    testFrameworks += new TestFramework("munit.Framework"),
    Test / parallelExecution := false, // tests run fine in parallel but output is nicer this way
    scalacOptions --= Seq("-Xfatal-warnings").filterNot(_ => insideCI.value),
    scalacOptions ++= Seq(
      "-Ymacro-annotations",
      "-Ywarn-macros:after"
      ),
    )

lazy val core = project
  .in(file("modules/core"))
  .enablePlugins(AutomateHeaderPlugin)
.settings(commonSettings)
  .settings(
      name := "lucuma-odb-api-core",
      libraryDependencies ++= Seq(
        "co.fs2"                     %% "fs2-core"                  % fs2Version,
        "dev.optics"                 %% "monocle-core"              % monocleVersion,
        "dev.optics"                 %% "monocle-state"             % monocleVersion,
        "dev.optics"                 %% "monocle-macro"             % monocleVersion,
        "org.sangria-graphql"        %% "sangria"                   % sangriaVersion,
        "org.sangria-graphql"        %% "sangria-circe"             % sangriaCirceVersion,
      "edu.gemini"                 %% "clue-model"                % clueVersion,
      "edu.gemini"                 %% "lucuma-core"               % lucumaCoreVersion,
      //      "edu.gemini"                 %% "lucuma-sso-backend-client" % lucumaSsoVersion,
      "org.tpolecat"               %% "atto-core"                 % attoVersion,
      "org.typelevel"              %% "cats-core"                 % catsVersion,
      "org.typelevel"              %% "cats-effect"               % catsEffectVersion,
      "org.typelevel"              %% "cats-mtl"                  % catsMtlVersion,
      "io.chrisdavenport"          %% "cats-time"                 % catsTimeVersion,
      "io.circe"                   %% "circe-core"                % circeVersion,
      "io.circe"                   %% "circe-literal"             % circeVersion,
      "io.circe"                   %% "circe-optics"              % circeOpticsVersion,
      "io.circe"                   %% "circe-parser"              % circeVersion,
      "io.circe"                   %% "circe-generic"             % circeVersion,
      "io.circe"                   %% "circe-generic-extras"      % circeVersion,
      "io.circe"                   %% "circe-refined"             % circeVersion,
      "org.typelevel"              %% "jawn-parser"               % jawnVersion,
      "org.typelevel"              %% "log4cats-slf4j"            % log4catsVersion,
      "ch.qos.logback"             %  "logback-classic"           % logbackVersion,
      "eu.timepit"                 %% "singleton-ops"             % singletonOpsVersion,
      "eu.timepit"                 %% "refined"                   % refinedVersion,
      "eu.timepit"                 %% "refined-cats"              % refinedVersion,

      "edu.gemini"                 %% "lucuma-core-testkit"       % lucumaCoreVersion      % Test,
      "io.chrisdavenport"          %% "cats-scalacheck"           % catsScalacheckVersion  % Test,
      "org.scalameta"              %% "munit"                     % munitVersion           % Test,
      "org.typelevel"              %% "discipline-munit"          % disciplineMunitVersion % Test
      ),
      )

      lazy val itc = project
  .in(file("modules/itc"))
  .enablePlugins(AutomateHeaderPlugin)
.settings(commonSettings)
  .settings(
      name := "lucuma-odb-itc",
      scalacOptions ++= Seq(
        "-Ymacro-annotations"
        ),
      libraryDependencies ++= Seq(
        "edu.gemini"                 %% "lucuma-core"               % lucumaCoreVersion,
        "org.typelevel"              %% "cats-core"                 % catsVersion,
        "org.typelevel"              %% "cats-effect"               % catsEffectVersion,
        "org.http4s"                 %% "http4s-async-http-client"  % http4sVersion,
        "org.http4s"                 %% "http4s-circe"              % http4sVersion,
        "org.http4s"                 %% "http4s-dsl"                % http4sVersion,
        "io.circe"                   %% "circe-literal"             % circeVersion,
        "edu.gemini"                 %% "clue-model"                % clueVersion,
        // "io.circe"                   %% "circe-optics"              % circeOpticsVersion,
        // "io.circe"                   %% "circe-parser"              % circeVersion,
        "io.circe"                   %% "circe-generic"             % circeVersion,
        "org.sangria-graphql"        %% "sangria"                   % sangriaVersion,
        "org.sangria-graphql"        %% "sangria-circe"             % sangriaCirceVersion,
        "org.typelevel"              %% "munit-cats-effect-3"       % munitCatsEffectVersion % Test,
        ),
      )

      lazy val service = project
  .in(file("modules/service"))
  .enablePlugins(AutomateHeaderPlugin)
  .dependsOn(itc)
.settings(commonSettings)
  .settings(
      name := "lucuma-odb-api-service",
      scalacOptions ++= Seq(
        "-Ymacro-annotations"
        ),
        scalacOptions -= "-Vtype-diffs",
    libraryDependencies ++= Seq(
//       "dev.optics"                 %% "monocle-core"              % monocleVersion,
      "edu.gemini"     %% "gsp-graphql-core"   % grackleVersion,
      "edu.gemini"     %% "gsp-graphql-generic"   % grackleVersion,
      "edu.gemini"     %% "gsp-graphql-circe"   % grackleVersion,
      "org.tpolecat"   %% "natchez-honeycomb"   % natchezVersion,
      "org.tpolecat"   %% "natchez-log"         % natchezVersion,
      "org.tpolecat"   %% "natchez-http4s"      % natcchezHttp4sVersion,
      "co.fs2"                     %% "fs2-core"                  % fs2Version,
      "org.sangria-graphql"        %% "sangria"                   % sangriaVersion,
      "org.sangria-graphql"        %% "sangria-circe"             % sangriaCirceVersion,
//       "edu.gemini"                 %% "clue-model"                % clueVersion,
      "edu.gemini"                 %% "lucuma-core"               % lucumaCoreVersion,
// //      "edu.gemini"                 %% "lucuma-sso-backend-client" % lucumaSsoVersion,
//       "org.tpolecat"               %% "atto-core"                 % attoVersion,
      "org.typelevel"              %% "cats-core"                 % catsVersion,
      "org.typelevel"              %% "cats-effect"               % catsEffectVersion,
//       "io.circe"                   %% "circe-core"                % circeVersion,
//       "io.circe"                   %% "circe-literal"             % circeVersion,
//       "io.circe"                   %% "circe-optics"              % circeOpticsVersion,
//       "io.circe"                   %% "circe-parser"              % circeVersion,
//       "io.circe"                   %% "circe-generic"             % circeVersion,
//       "io.circe"                   %% "circe-generic-extras"      % circeVersion,
      "is.cir"                     %% "ciris"                     % cirisVersion,
//       "org.typelevel"              %% "jawn-parser"               % jawnVersion,
      "org.typelevel"              %% "log4cats-slf4j"            % log4catsVersion,
      "ch.qos.logback"             %  "logback-classic"           % logbackVersion,
      "org.http4s"                 %% "http4s-core"               % http4sVersion,
      "org.http4s"                 %% "http4s-blaze-server"       % http4sVersion,
      "org.http4s"                 %% "http4s-blaze-client"       % http4sVersion,
//       "org.http4s"                 %% "http4s-circe"              % http4sVersion,
//       "org.http4s"                 %% "http4s-dsl"                % http4sVersion,
//       "edu.gemini"                 %% "clue-http4s-jdk-client"    % clueVersion            % Test,
      "org.typelevel"              %% "munit-cats-effect-3"       % munitCatsEffectVersion % Test,
    ),
  ).enablePlugins(JavaAppPackaging)
