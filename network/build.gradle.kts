plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.network"
}

dependencies {

    api(libs.bundles.ktor)
    implementation(libs.koin.core)
    implementation(projects.utils)
}