# حل مشكلة Google Play Services Error

## المشكلة
```
java.lang.SecurityException: Unknown calling package name 'com.google.android.gms'
ConnectionResult{statusCode=DEVELOPER_ERROR}
```

## السبب
البصمات (SHA-1 و SHA-256) للتطبيق غير مسجلة في Firebase Console.

## الحل

### الخطوة 1: الحصول على البصمات (تم بالفعل)
البصمات الخاصة بتطبيقك:
- **SHA-1**: `56:92:7C:B5:D1:04:32:D0:C3:6D:83:36:E6:B1:99:5D:7D:5C:FA:FF`
- **SHA-256**: `20:7A:2F:13:A5:A6:B6:86:27:DF:23:08:0F:80:C8:F1:48:A7:47:C5:2F:3C:5D:0A:3B:A9:74:9D:16:E5:23:E3`

### الخطوة 2: إضافة البصمات في Firebase Console

1. افتح [Firebase Console](https://console.firebase.google.com/)
2. اختر مشروعك **warm-clouds**
3. اضغط على أيقونة الإعدادات ⚙️ بجانب "Project Overview"
4. اختر **Project settings**
5. انتقل إلى تبويب **Your apps** (في الأسفل)
6. ابحث عن تطبيق Android الخاص بك (`com.warmclouds.app`)
7. اضغط على **Add fingerprint** (أو **إضافة بصمة**)
8. أضف البصمات التالية:

   **SHA-1:**
   ```
   56:92:7C:B5:D1:04:32:D0:C3:6D:83:36:E6:B1:99:5D:7D:5C:FA:FF
   ```

   **SHA-256:**
   ```
   20:7A:2F:13:A5:A6:B6:86:27:DF:23:08:0F:80:C8:F1:48:A7:47:C5:2F:3C:5D:0A:3B:A9:74:9D:16:E5:23:E3
   ```

9. اضغط **Save** (حفظ)

### الخطوة 3: إعادة تحميل ملف google-services.json (اختياري)

بعد إضافة البصمات، قد تحتاج إلى:
1. تحميل ملف `google-services.json` الجديد من Firebase Console
2. استبدال الملف الموجود في `app/google-services.json`

**ملاحظة:** عادة لا تحتاج إلى هذا إذا كان `package_name` في الملف الحالي صحيح (وهو صحيح: `com.warmclouds.app`)

### الخطوة 4: إعادة بناء التطبيق

1. في Android Studio، اضغط **File > Sync Project with Gradle Files**
2. ثم **Build > Clean Project**
3. ثم **Build > Rebuild Project**
4. شغّل التطبيق مرة أخرى

## التحقق من الحل

بعد إضافة البصمات وإعادة بناء التطبيق، يجب أن يعمل التطبيق بدون خطأ `DEVELOPER_ERROR`.

## ملاحظات إضافية

- إذا كنت تستخدم keystore مختلف للإصدار النهائي (release build)، ستحتاج إلى إضافة بصمات ذلك الـ keystore أيضاً
- تأكد من أن `package_name` في `google-services.json` يطابق `applicationId` في `build.gradle.kts` (وهو `com.warmclouds.app`)

