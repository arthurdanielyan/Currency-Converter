plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.data"
}

dependencies {

    implementation(libs.koin.core)

    implementation(projects.network)
    implementation(projects.database)
    implementation(projects.domain)
    implementation(projects.utils)
}