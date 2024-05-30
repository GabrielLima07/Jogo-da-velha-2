plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.jogo_da_velha_2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.jogo_da_velha_2"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.github.parse-community.Parse-SDK-Android:parse:1.26.0")
    //implementation("com.github.parse-community:ParseLiveQuery-Android:1.2.2")

    // Adicionando biblioteca do cardview
    implementation("androidx.cardview:cardview:1.0.0")
}