plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "dev.codex.backgesturehook.hiddenapi"
    compileSdk = 37

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
