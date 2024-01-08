@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
}

android {

    namespace = "com.tvchannels.sample.coreui"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    viewBinding {
        android.buildFeatures.viewBinding = true
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(project(":feature:domain"))
    coreLibraryDesugaring(libs.android.desugarJdkLibs)
    api(libs.androidx.lifecycle)
    api(libs.androidx.constraintLayout)
    api(libs.androidx.swipeRefresh)
    api(libs.material)
    api(libs.androidx.appcompat)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)
    implementation(libs.kotlinx.coroutines.play.services)
}
