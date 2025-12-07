# حل مشكلة Activity class does not exist

## المشكلة:
```
Activity class (com.example.myapplication/com.warmclouds.app.WelcomeActivity) does not exist
```

## الحلول (جربها بالترتيب):

### الحل 1: تحديث Run Configuration في Android Studio

1. في Android Studio، انقر على القائمة المنسدلة بجانب زر Run (أعلى الشاشة)
2. اختر **"Edit Configurations..."**
3. في النافذة التي تفتح:
   - تأكد من أن **"Launch"** مضبوط على **"Default Activity"** أو **"Specified Activity"**
   - إذا كان "Specified Activity"، تأكد من أن النص هو: `com.warmclouds.app.WelcomeActivity`
   - تأكد من أن **"Package"** أو **"Module"** مضبوط على `app`
4. اضغط **"Apply"** ثم **"OK"**

### الحل 2: Sync Project with Gradle Files

1. في Android Studio: **File → Sync Project with Gradle Files**
2. انتظر حتى ينتهي الـ Sync
3. حاول تشغيل التطبيق مرة أخرى

### الحل 3: Invalidate Caches / Restart

1. في Android Studio: **File → Invalidate Caches / Restart**
2. اختر **"Invalidate and Restart"**
3. انتظر حتى يعيد Android Studio فتح المشروع
4. حاول تشغيل التطبيق مرة أخرى

### الحل 4: حذف Run Configurations القديمة

1. أغلق Android Studio
2. احذف الملفات التالية (إن وجدت):
   - `.idea/workspace.xml`
   - `.idea/runConfigurations.xml`
3. افتح Android Studio مرة أخرى
4. افتح المشروع
5. اضغط على زر Run (أو Shift+F10)

### الحل 5: إعادة بناء المشروع

1. في Android Studio: **Build → Clean Project**
2. ثم: **Build → Rebuild Project**
3. بعد انتهاء البناء، حاول تشغيل التطبيق

### الحل 6: التحقق من AndroidManifest.xml

تأكد من أن `AndroidManifest.xml` يحتوي على:

```xml
<activity
    android:name=".WelcomeActivity"
    android:exported="true"
    android:theme="@style/Theme.MyApplication">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

### الحل 7: التحقق من build.gradle.kts

تأكد من أن:
- `namespace = "com.warmclouds.app"`
- `applicationId = "com.warmclouds.app"`

## إذا لم تعمل الحلول أعلاه:

1. أغلق Android Studio تماماً
2. احذف مجلد `.idea` من المشروع
3. افتح Android Studio
4. افتح المشروع (Open Project)
5. انتظر حتى ينتهي Indexing
6. اضغط **File → Sync Project with Gradle Files**
7. حاول تشغيل التطبيق

## ملاحظة مهمة:

إذا استمرت المشكلة، تأكد من:
- أن ملف `WelcomeActivity.java` موجود في: `app/src/main/java/com/warmclouds/app/WelcomeActivity.java`
- أن package في بداية الملف هو: `package com.warmclouds.app;`
- أن `namespace` في `build.gradle.kts` هو: `com.warmclouds.app`


