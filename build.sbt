import play.sbt.PlayJava

name := "wesignproject-backend"

version := "1.0"

lazy val `wesignproject-backend` = (project in file("."))
  .enablePlugins(PlayJava)

scalaVersion := "2.12.8"

libraryDependencies ++= Seq( javaJdbc , javaWs , guice , javaJpa , filters, cacheApi )

// https://mvnrepository.com/artifact/org.hibernate/hibernate-core
libraryDependencies += "org.hibernate" % "hibernate-core" % "5.3.10.Final"

// https://mvnrepository.com/artifact/org.hibernate/hibernate-validator
libraryDependencies += "org.hibernate" % "hibernate-validator" % "5.4.3.Final"

// https://mvnrepository.com/artifact/dom4j/dom4j
libraryDependencies += "dom4j" % "dom4j" % "1.6"

// https://mvnrepository.com/artifact/com.google.guava/guava
libraryDependencies += "com.google.guava" % "guava" % "27.1-jre"

// https://mvnrepository.com/artifact/net.bramp.ffmpeg/ffmpeg
libraryDependencies += "net.bramp.ffmpeg" % "ffmpeg" % "0.6.2"

// https://mvnrepository.com/artifact/io.javaslang/javaslang
libraryDependencies += "io.javaslang" % "javaslang" % "2.0.6"

// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-annotations" % "2.9.9"

// https://mvnrepository.com/artifact/commons-validator/commons-validator
libraryDependencies += "commons-validator" % "commons-validator" % "1.6"

// https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.9"

// https://mvnrepository.com/artifact/commons-io/commons-io
libraryDependencies += "commons-io" % "commons-io" % "2.6"

// https://mvnrepository.com/artifact/org.postgresql/postgresql
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.6"

// https://mvnrepository.com/artifact/org.glassfish/javax.el
libraryDependencies += "org.glassfish" % "javax.el" % "3.0.1-b08"

libraryDependencies += "com.fasterxml.uuid" % "java-uuid-generator" % "3.2.0"

// https://mvnrepository.com/artifact/net.sf.ehcache/ehcache
libraryDependencies += "net.sf.ehcache" % "ehcache" % "2.10.4"

// https://mvnrepository.com/artifact/org.testng/testng
libraryDependencies += "org.testng" % "testng" % "6.14.3" % Test

// https://mvnrepository.com/artifact/org.mockito/mockito-core
libraryDependencies += "org.mockito" % "mockito-core" % "2.28.2" % Test

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"  