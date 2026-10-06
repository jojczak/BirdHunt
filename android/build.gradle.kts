import java.util.Properties

val natives: Configuration = configurations.create("natives")

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = libs.versions.appId.get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.appId.get()
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.android.versionCode.get().toInt()
        versionName = libs.versions.projectVersion.get()
        multiDexEnabled = true
    }

    signingConfigs {
        create("release") {
            val keystoreFile = file("keystore.jks")
            if (keystoreFile.exists()) {
                storeFile = keystoreFile
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyPassword = System.getenv("KEY_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                enableV1Signing = true
                enableV2Signing = true
                enableV3Signing = true
                enableV4Signing = true
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        isCoreLibraryDesugaringEnabled = true
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.directories += setOf("src/main/java", "src/main/kotlin")
            aidl.directories += setOf("src/main/java", "src/main/kotlin")
            renderscript.directories += setOf("src/main/java", "src/main/kotlin")
            res.directories += "res"
            assets.directories += "../assets"
            jniLibs.directories += "libs"
        }
    }

    packaging {
        resources {
            excludes += listOf(
                "META-INF/robovm/ios/robovm.xml",
                "META-INF/DEPENDENCIES.txt",
                "META-INF/DEPENDENCIES",
                "META-INF/dependencies.txt",
                "**/*.gwt.xml"
            )
            pickFirsts += listOf(
                "META-INF/LICENSE.txt",
                "META-INF/LICENSE",
                "META-INF/license.txt",
                "META-INF/LGPL2.1",
                "META-INF/NOTICE.txt",
                "META-INF/NOTICE",
                "META-INF/notice.txt"
            )
        }
    }

    flavorDimensions += "PGS"
    productFlavors {
        create("PGS_On") {
            dimension = "PGS"
            isDefault = true
        }
        create("PGS_Off") {
            dimension = "PGS"
        }
    }
}

dependencies {
    implementation(project(":core"))
    coreLibraryDesugaring(libs.desugar.jdk)

    implementation(libs.gdx.backend.android)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.splashscreen)
    implementation(libs.play.review)
    implementation(libs.play.review.ktx)
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64").forEach { arch ->
        add(
            natives.name,
            "com.badlogicgames.gdx:gdx-platform:${libs.versions.gdx.get()}:natives-$arch"
        )
    }

    "PGS_OnImplementation"(libs.play.games)
}

val copyAndroidNatives = tasks.register("copyAndroidNatives") {
    group = "build"
    description = "Copies native .so files from dependencies into the libs directory."

    doFirst {
        file("libs/armeabi-v7a/").mkdirs()
        file("libs/arm64-v8a/").mkdirs()
        file("libs/x86_64/").mkdirs()
        file("libs/x86/").mkdirs()

        natives.files.forEach { jar ->
            val match = Regex("natives-(.+)\\.jar").find(jar.name)
            val outputDir = match?.groupValues?.get(1)?.let { file("libs/$it") }

            if (outputDir != null) {
                copy {
                    from(zipTree(jar))
                    into(outputDir)
                    include("*.so")
                }
            }
        }
    }
}

tasks.withType<com.android.build.gradle.tasks.MergeSourceSetFolders>().configureEach {
    dependsOn(copyAndroidNatives)
}

tasks.register<Exec>("run") {
    group = "application"
    description = "Runs the application on a connected Android device via ADB."

    val localProperties = project.rootProject.file("local.properties")
    var sdkPath = System.getenv("ANDROID_SDK_ROOT")

    if (localProperties.exists()) {
        val properties = Properties()
        localProperties.inputStream().use { properties.load(it) }
        val sdkDir = properties.getProperty("sdk.dir")
        if (sdkDir != null) {
            sdkPath = sdkDir
        }
    }

    if (sdkPath == null) {
        throw GradleException("Nie znaleziono SDK Androida. Ustaw sdk.dir w local.properties lub zmienną ANDROID_SDK_ROOT.")
    }

    val adb = "$sdkPath/platform-tools/adb"
    commandLine(
        adb,
        "shell",
        "am",
        "start",
        "-n",
        "pl.jojczak.birdhunt/pl.jojczak.birdhunt.android.AndroidLauncher"
    )
}
