import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

    //ksp (такжже нужен для room, hilt и так далее)
    id("com.google.devtools.ksp")

    // Kotlin serialization plugin for type safe routes and navigation arguments
    kotlin("plugin.serialization") version "2.4.0" //версия плагина сериализации должна совпадать с версией Kotlin

    //Hilt
    id("com.google.dagger.hilt.android")


}

android {
    namespace = "com.example.aproject"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.aproject"
        minSdk = 26
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
//    kotlinOptions {
//        jvmTarget = "11"
//    }
    buildFeatures {
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)

    //Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //Сoroutines
    // Основная библиотека корутин(Flow уже там)
    implementation(libs.kotlinx.coroutines.core)
    // Только для Android: поддержка главного потока (Main Dispatcher)
    implementation(libs.kotlinx.coroutines.android)

    //Navigation component
    implementation(libs.androidx.navigation.compose)
    // Kotlin Serialization (для типобезопасной навигации)
    implementation(libs.kotlinx.serialization.json)

    //Hilt
    implementation(libs.hilt.android)
    ksp(libs.google.hilt.android.compiler)
    // Расширение для внедрения зависимостей в ViewModel
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)

    //Retrofit
    implementation(libs.retrofit)
    // Конвертер для преобразования JSON в объекты Kotlin (например, Gson)
    implementation(libs.converter.gson)
    // Конвертер на основе kotlinx.serialization (рекомендуется)
    implementation(libs.retrofit2.converter.kotlinx.serialization)
    // Optional: OkHttp logging interceptor
    implementation(libs.logging.interceptor)

    //Material Icons
//    implementation("androidx.compose.material:material-icons-core")
    implementation(libs.androidx.compose.material.icons.extended)

    //Tests
    // JUnit 4
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    // Coroutines
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    // MockK
    testImplementation(libs.mockk)
    androidTestImplementation(libs.mockk.android)

    // Flow and LiveData
    testImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.core.testing)

    // Room (InMemory)
    androidTestImplementation(libs.androidx.room.testing)

    // Hilt (DI)
    androidTestImplementation(libs.hilt.android.testing)
    // Обязательный плагин-компилятор для генерации тестовых компонентов
    kspAndroidTest(libs.google.hilt.android.compiler) //все верно ошибки нет про дубликат и в остальных тоже

    // Compose UI ---
    androidTestImplementation(libs.androidx.ui.test.junit4)
    // Для отладки UI-тестов (показывает дерево композиции)
    debugImplementation(libs.androidx.ui.test.manifest)

}