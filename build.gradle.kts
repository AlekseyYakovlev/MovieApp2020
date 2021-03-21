buildscript {
    val kotlinVersion by extra("1.4.30")
    val hiltVersion by extra("2.33-beta")
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${kotlinVersion}")
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:${hiltVersion}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}