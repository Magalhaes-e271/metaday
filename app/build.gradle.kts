plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize") // ✅ Necessário para @Parcelize
}

android {
    namespace = "com.example.library"
    compileSdk = 35


    buildFeatures {
        viewBinding = true // <--- ADICIONE ESTA LINHA
    }

    defaultConfig {
        applicationId = "com.example.library"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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

    // 1. Retrofit Core (O cliente HTTP)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // 2. Retrofit Converter (Para converter JSON em objetos Kotlin, usando Gson)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // 3. (Opcional, mas recomendado) Para interceptar e ver os logs de rede no Logcat
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("io.ktor:ktor-client-core:2.3.8")

    // Ktor para Android Engine (Obrigatório)
    implementation("io.ktor:ktor-client-android:2.3.8")

    // Serialização JSON (Obrigatório para converter JSON em Usuario)
    implementation("io.ktor:ktor-client-content-negotiation:2.3.8")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.8")


    // ✅ JSON
    implementation("com.google.code.gson:gson:2.11.0")

    // ✅ Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation(libs.androidx.activity)
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // ✅ AndroidX
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // ✅ Testes
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


    // ...
}
