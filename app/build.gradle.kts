plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.warmclouds.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.warmclouds.app"
        minSdk = 23  // Android 6.0 Marshmallow - Firebase يتطلب 23 على الأقل
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // دعم جميع أنواع الشاشات
        vectorDrawables.useSupportLibrary = true
    }
    
    // دعم جميع أحجام الشاشات
    splits {
        abi {
            isEnable = false  // لا نقسم APK حسب المعالج - APK واحد لجميع الأجهزة
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        // دعم Java 8 features للإصدارات القديمة
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Core Android
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")
    
    // Image Loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    
    // ViewPager2 for image slider
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    
    // دعم Java 8 features للإصدارات القديمة
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}