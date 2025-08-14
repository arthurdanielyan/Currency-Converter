plugins {
    alias(libs.plugins.app.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.database"
}

dependencies {

    ksp(libs.room.complier)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.androidx.datastore)
    implementation(libs.koin.core)
}