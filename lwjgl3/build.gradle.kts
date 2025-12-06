import io.github.fourlastor.construo.Target
import org.gradle.internal.os.OperatingSystem

val enableGraalNative: String? by project
val appName = rootProject.name
val appVersion = libs.versions.projectVersion.get()
val gdxVersion = libs.versions.gdx.get()

plugins {
    alias(libs.plugins.graalvm.native) apply false
    alias(libs.plugins.construo)
    application
}

application {
    mainClass.set("${libs.versions.appId.get()}.lwjgl3.Lwjgl3Launcher")
}

sourceSets.main {
    resources.srcDirs(rootProject.file("assets"))
}

dependencies {
    implementation(project(":core"))
    implementation(libs.gdx.backend.lwjgl3)
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")

    if (enableGraalNative == "true") {
        implementation(libs.gdx.svmhelper.backend)
    }
}

tasks.named<JavaExec>("run") {
    workingDir = rootProject.file("assets")
    isIgnoreExitValue = true

    if (OperatingSystem.current().isMacOsX) {
        jvmArgs("-XstartOnFirstThread")
    }
}

tasks.named<Jar>("jar") {
    archiveFileName.set("$appName-$appVersion.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    dependsOn(configurations.runtimeClasspath)
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

    exclude("META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    exclude("META-INF/maven/**")

    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }

    doLast {
        file(archiveFile).setExecutable(true, false)
    }
}

construo {
    name.set(appName)
    humanName.set(appName)
    version = appVersion

    targets.apply {
        create("linuxX64", Target.Linux::class.java) {
            architecture.set(Target.Architecture.X86_64)
            jdkUrl.set("https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.9%2B10/OpenJDK21U-jdk_x64_linux_hotspot_21.0.9_10.tar.gz")
        }
        create("macM1", Target.MacOs::class.java) {
            architecture.set(Target.Architecture.AARCH64)
            jdkUrl.set("https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.9%2B10/OpenJDK21U-jdk_aarch64_mac_hotspot_21.0.9_10.tar.gz")
            identifier.set(libs.versions.appId.get() + "." + appName)
            macIcon.set(project.file("icons/logo.icns"))
        }
        create("macX64", Target.MacOs::class.java) {
            architecture.set(Target.Architecture.X86_64)
            jdkUrl.set("https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.9%2B10/OpenJDK21U-jdk_x64_mac_hotspot_21.0.9_10.tar.gz")
            identifier.set(libs.versions.appId.get() + "." + appName)
            macIcon.set(project.file("icons/logo.icns"))
        }
        create("winX64", Target.Windows::class.java) {
            architecture.set(Target.Architecture.X86_64)
            jdkUrl.set("https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.9%2B10/OpenJDK21U-jdk_x64_windows_hotspot_21.0.9_10.zip")
        }
        create("winARM", Target.Windows::class.java) {
            architecture.set(Target.Architecture.AARCH64)
            jdkUrl.set("https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.9%2B10/OpenJDK21U-jdk_aarch64_windows_hotspot_21.0.9_10.zip")
        }
    }
}

tasks.register("dist") {
    dependsOn("jar")
}

distributions {
    main {
        contents {
            into("libs") {
                from(configurations.runtimeClasspath) {
                    exclude(tasks.named<Jar>("jar").get().outputs.files.singleFile.name)
                }
            }
        }
    }
}

tasks.named<CreateStartScripts>("startScripts") {
    dependsOn(tasks.named("jar"))
    classpath = tasks.named<Jar>("jar").get().outputs.files
}

if (enableGraalNative == "true") {
    apply(plugin = "org.graalvm.buildtools.native")

    configure<org.graalvm.buildtools.gradle.dsl.GraalVMExtension> {
        binaries {
            named("main") {
                imageName.set(appName)
                mainClass.set(application.mainClass)
                buildArgs.add("-march=compatibility")
                jvmArgs.add("-Dfile.encoding=UTF8")
                sharedLibrary.set(false)
            }
        }
    }

    tasks.named("run") {
        doNotTrackState("Running the app should not be affected by Graal.")
    }

    tasks.register("generateResourcesConfigFile") {
        doFirst {
            val assetsFolder = rootProject.file("assets")
            val resFolder = project.file("src/main/resources/META-INF/native-image/$appName")
            resFolder.mkdirs()

            val resFile = File(resFolder, "resource-config.json")
            if (resFile.exists()) resFile.delete()

            val filesPattern = assetsFolder.walk()
                .filter { it.isFile }
                .map { "\\Q${it.name}\\E" }
                .joinToString("|")

            val jsonContent = """
                {
                  "resources": {
                    "includes": [
                      {
                        "pattern": ".*($filesPattern|libgdx.+\\.png|lsans.+)"
                      }
                    ]
                  },
                  "bundles": []
                }
            """.trimIndent()

            resFile.writeText(jsonContent)
            println("Wygenerowano konfigurację zasobów dla GraalVM: ${resFile.absolutePath}")
        }
    }
}
