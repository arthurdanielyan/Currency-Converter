import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-gradle-plugin`
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("currencyConverterAndroidPlugin") {
            id = "com.currencyconverter.android.library"
            implementationClass = "com.example.gradleplugins.CurrencyConverterAndroidLibraryPlugin"
            version = "1.0.0"
        }
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())

    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.android.tools.build.gradle)
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}