# إعداد قواعد Firestore - حل مشكلة PERMISSION_DENIED

## المشكلة
عند محاولة الوصول إلى Firestore، تظهر رسالة الخطأ:
```
PERMISSION_DENIED: Missing or insufficient permissions
```

## الحل السريع

### الخطوة 1: اذهب إلى Firebase Console
1. افتح [Firebase Console](https://console.firebase.google.com/)
2. اختر مشروع **Warm Clouds**

### الخطوة 2: افتح Firestore Database
1. من القائمة الجانبية، اضغط على **Firestore Database**
2. اضغط على تبويب **Rules** (القواعد)

### الخطوة 3: استبدل القواعد الحالية

انسخ والصق القواعد التالية:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

### الخطوة 4: احفظ القواعد
1. اضغط على زر **Publish** (نشر)
2. انتظر حتى تظهر رسالة "Rules published successfully"

## شرح القواعد

القواعد المذكورة أعلاه تسمح لـ:
- **أي مستخدم مسجل الدخول** بقراءة وكتابة البيانات
- هذه القواعد مناسبة للتطوير والاختبار

## قواعد أكثر أماناً (للإنتاج)

إذا أردت قواعد أكثر أماناً، استخدم هذه:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // المستخدمين
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
      allow create: if request.auth != null;
    }
    
    // الحضانات
    match /nurseries/{nurseryId} {
      allow read: if request.auth != null && resource.data.isActive == true;
      allow write: if request.auth != null && 
        exists(/databases/$(database)/documents/users/$(request.auth.uid)) &&
        get(/databases/$(database)/documents/users/$(request.auth.uid)).data.role == 'admin';
    }
    
    // الحجوزات
    match /bookings/{bookingId} {
      allow read: if request.auth != null && 
        resource.data.userId == request.auth.uid;
      allow create: if request.auth != null && 
        request.resource.data.userId == request.auth.uid;
      allow update: if request.auth != null && 
        resource.data.userId == request.auth.uid;
    }
    
    // التقييمات
    match /reviews/{reviewId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null && 
        request.resource.data.userId == request.auth.uid;
      allow update, delete: if request.auth != null && 
        resource.data.userId == request.auth.uid;
    }
  }
}
```

## خطوات مفصلة بالصور

### 1. افتح Firebase Console
- اذهب إلى: https://console.firebase.google.com/
- اختر مشروعك "Warm Clouds"

### 2. افتح Firestore
- من القائمة الجانبية اليسرى
- اضغط على **Firestore Database**

### 3. افتح القواعد
- اضغط على تبويب **Rules** في الأعلى
- سترى القواعد الحالية

### 4. استبدل القواعد
- احذف كل القواعد الحالية
- الصق القواعد الجديدة
- اضغط **Publish**

## التحقق

بعد حفظ القواعد:
1. انتظر 10-30 ثانية
2. حاول تسجيل الدخول في التطبيق مرة أخرى
3. يجب أن يعمل الآن بدون أخطاء

## استكشاف الأخطاء

إذا استمرت المشكلة:
- ✅ تأكد من أن المستخدم مسجل الدخول
- ✅ تحقق من حفظ القواعد (Publish)
- ✅ انتظر بضع دقائق
- ✅ أعد تشغيل التطبيق


