plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.library"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.library"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation ("io.ktor:ktor-client-cio:2.3.4")

    implementation("io.ktor:ktor-client-core:2.3.9")
    implementation("io.ktor:ktor-client-android:2.3.9")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.9")
    implementation("io.ktor:ktor-serialization-gson:2.3.9")

// Para usar lifecycleScope
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    // ✅ Ktor Client para Android
    implementation("io.ktor:ktor-client-core:2.3.9")
    implementation("io.ktor:ktor-client-android:2.3.9")

    // ✅ Plugin para lidar com JSON via Gson
    implementation("io.ktor:ktor-client-content-negotiation:2.3.9")
    implementation("io.ktor:ktor-serialization-gson:2.3.9")

    // ✅ (Opcional) logs de rede (ajuda a debugar)
    implementation("io.ktor:ktor-client-logging:2.3.9")

    // ✅ AndroidX e Material Design
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")

    // ✅ JSON extra (caso precise usar Gson manualmente)
    implementation("com.google.code.gson:gson:2.11.0")

    // ✅ Glide (para carregar imagens, se usar)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation(libs.androidx.activity)
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // ✅ RecyclerView (para listas)
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // ✅ Testes
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
