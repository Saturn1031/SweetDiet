plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.narae.sweetdiet"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.narae.sweetdiet"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding {
        enable = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.8.0")
    implementation("androidx.preference:preference:1.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.multidex:multidex:2.0.1")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

    // firebase auth
    implementation("com.google.firebase:firebase-auth-ktx:23.0.0")

    // firebase firestore
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")

    // firebase storage
    implementation("com.google.firebase:firebase-storage-ktx:21.0.0")

    // 구글 로그인
    implementation("com.google.android.gms:play-services-auth:21.1.1")

    // retrofit2 사용 등록
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // gson
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
}