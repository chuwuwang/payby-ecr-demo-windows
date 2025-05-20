import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.protobuf") version "0.9.4"
}

group = "com.payby.pos"
version = "1.1-SNAPSHOT"

repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    mavenCentral()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    val list = "include" to listOf("*.jar")
    val map = mapOf("dir" to "libs", list)
    val fileTree = fileTree(map)
    implementation(fileTree)
    implementation(compose.desktop.currentOs)

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.protobuf:protobuf-java:3.25.3")
    implementation("com.google.protobuf:protobuf-java-util:4.27.3")

    implementation("commons-io:commons-io:2.18.0")

    // widget
    implementation("io.github.dokar3:sonner:0.3.8")

    implementation("io.ultreia:bluecove:2.1.1")
    implementation("net.sf.bluecove:bluecove-gpl:2.1.0")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.3"
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "payby-ecr-demo-windows"
            packageVersion = "1.1.1"
        }
        buildTypes.release.proguard {
            version.set("7.5.0")
            configurationFiles.from("proguard-rules.pro")
        }
    }
}
