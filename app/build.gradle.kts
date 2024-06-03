plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.mahdicen.bagmag"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mahdicen.bagmag"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        kotlinOptions {
            freeCompilerArgs = listOf("-Xdebug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    // Navigation =>
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // LiveData_State =>
    implementation("androidx.compose.runtime:runtime-livedata:1.6.3")

    // Coil =>
    implementation("io.coil-kt:coil-compose:2.0.0-rc02")

    // Coroutines =>
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // Koin =>
    implementation("dev.burnoo:cokoin:0.3.2")

    implementation("dev.burnoo:cokoin-android-viewmodel:0.3.2")
    implementation("dev.burnoo:cokoin-android-navigation:0.3.2")

//    // Parse Platform =>
//    val parseVersion = "1.25.0"
//
//    //noinspection GradleDependency
//    implementation ("com.github.parse-community.Parse-SDK-Android:parse:$parseVersion")
//    //noinspection GradleDependency
//    implementation ("com.github.parse-community.Parse-SDK-Android:ktx:$parseVersion")
//    //noinspection GradleDependency
//    implementation ("com.github.parse-community.Parse-SDK-Android:coroutines:$parseVersion")

    // Retrofit =>
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Room =>
    implementation ("androidx.room:room-ktx:2.5.0-alpha01")
    kapt ("androidx.room:room-compiler:2.5.0-alpha01")
    kapt ("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.5.0")

    // System UI Controller =>
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.17.0")

    // Lottie
    implementation("com.airbnb.android:lottie-compose:5.2.0")

}