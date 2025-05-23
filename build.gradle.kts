// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.8.2" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("convention.detekt")
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.5.3" apply false
}
