// Top-level build file where you can add configuration options common to all subprojects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false

    //ksp
    id("com.google.devtools.ksp") version "2.3.9" apply false

    //Hilt
    alias(libs.plugins.hilt) apply false

}