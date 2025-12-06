tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

dependencies {
    api(libs.gdx)
    api(kotlin("stdlib"))
    implementation(libs.gdx.svmhelper.annotations)
}
