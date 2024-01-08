import org.jetbrains.kotlin.konan.properties.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.navigation.safeargs.kotlin)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.tvchannels.sample"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.tvchannels.sample"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        getByName("debug") {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
    kapt {
        correctErrorTypes = true
    }
    bundle {
        language {
            // We want to be able to switch the locale at runtime using AppLocale!
            enableSplit = false
        }
    }
}

dependencies {
    implementation(project(":feature:domain"))
    implementation(project(":feature:data"))
    implementation(project(":feature:coreui"))

    coreLibraryDesugaring(libs.android.desugarJdkLibs)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)

    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    implementation(libs.coil.kt)
    implementation(libs.androidx.exif)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.media3.core)
    implementation(libs.media3.ui)
    implementation(libs.media3.hls)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)

    implementation(libs.kiel.recycler)
    implementation(libs.coil.kt)
    implementation(libs.exifinterface) }

