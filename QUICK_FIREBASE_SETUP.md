# ⚡ إضافة Firebase - دليل سريع

## 🎯 الخطوات السريعة:

### 1. اذهب إلى Firebase Console
👉 **https://console.firebase.google.com/**

### 2. أنشئ مشروع جديد
- اضغط "Add project"
- اسم المشروع: "Warm Clouds"
- اتبع التعليمات

### 3. أضف تطبيق Android
- اضغط على أيقونة Android 🟢
- **Package name**: `com.warmclouds.app` ⚠️ مهم جداً
- اضغط "Register app"

### 4. حمّل google-services.json
- اضغط "Download google-services.json"
- **ضع الملف في**: `app/google-services.json` (بجانب build.gradle.kts)

### 5. فعّل Authentication
- Authentication → Sign-in method → Email/Password → Enable

### 6. فعّل Firestore
- Firestore Database → Create database → Test mode → Enable

---

## ✅ بعد إضافة الملف:

بعد وضع `google-services.json` في `app/`، أخبرني وسأقوم بـ:
1. تفعيل Firebase dependencies
2. تفعيل Google Services plugin  
3. تحديث الكود لاستخدام Firebase

---

## 📍 المسار الصحيح للملف:

```
MyApplication3/
└── app/
    ├── google-services.json  ← هنا
    └── build.gradle.kts
```

---

## ⚠️ تأكد من:
- ✅ Package name في Firebase: `com.warmclouds.app`
- ✅ الملف في `app/` وليس `app/src/main/`
- ✅ اسم الملف: `google-services.json` (بدون أخطاء)


