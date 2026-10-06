import org.gradle.plugins.ide.idea.model.IdeaModel
import org.gradle.api.plugins.JavaPluginExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
}

allprojects {
    pluginManager.apply(IdeaPlugin::class.java)

    configure<IdeaModel> {
        module {
            outputDir = file("build/classes/java/main")
            testOutputDir = file("build/classes/java/test")
        }
    }
}

subprojects {
    if (this.name != "android") {
        pluginManager.apply(JavaLibraryPlugin::class.java)
        pluginManager.apply("org.jetbrains.kotlin.jvm")

        configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(21))
            }
        }

        tasks.withType<JavaCompile> {
            options.release.set(21)
            options.isIncremental = true
        }

        tasks.named("compileJava") {
            doLast {
                val assetsFolder = rootProject.file("assets")
                val assetsFile = File(assetsFolder, "assets.txt")

                if (assetsFile.exists()) {
                    assetsFile.delete()
                }

                if (assetsFolder.exists()) {
                    val sb = StringBuilder()
                    assetsFolder.walkTopDown()
                        .filter { it.isFile && it.name != "assets.txt" }
                        .forEach { file ->
                            val relativePath = file
                                .relativeTo(assetsFolder)
                                .path.replace("\\", "/")
                            sb.append("$relativePath\n")
                        }
                    assetsFile.writeText(sb.toString())
                }
            }
        }
    }
}
