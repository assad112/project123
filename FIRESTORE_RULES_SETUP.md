# إعداد قواعد Firestore - حل مشكلة PERMISSION_DENIED

## المشكلة
عند محاولة الوصول إلى Firestore، تظهر رسالة الخطأ:
```
PERMISSION_DENIED: Missing or insufficient permissions
```

## الحل

### الخطوة 1: فتح Firebase Console
1. اذهب إلى [Firebase Console](https://console.firebase.google.com/)
2. اختر مشروع "Warm Clouds"

### الخطوة 2: فتح Firestore Database
1. من القائمة الجانبية، اضغط على **Firestore Database**
2. اضغط على تبويب **Rules** (القواعد)

### الخطوة 3: نسخ القواعد التالية

انسخ والصق القواعد التالية في Firebase Console:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // قواعد المستخدمين (users)
    match /users/{userId} {
      // المستخدم يمكنه قراءة وكتابة بياناته الخاصة فقط
      allow read, write: if request.auth != null && request.auth.uid == userId;
      
      // يمكن للمستخدمين إنشاء حساباتهم الخاصة
      allow create: if request.auth != null;
    }
    
    // قواعد الحضانات (nurseries)
    match /nurseries/{nurseryId} {
      // الجميع يمكنهم قراءة الحضانات النشطة
      allow read: if request.auth != null && resource.data.isActive == true;
      
      // فقط الأدمن يمكنه إنشاء/تحديث/حذف الحضانات
      allow write: if request.auth != null && 
        exists(/databases/$(database)/documents/users/$(request.auth.uid)) &&
        get(/databases/$(database)/documents/users/$(request.auth.uid)).data.role == 'admin';
    }
    
    // قواعد الحجوزات (bookings)
    match /bookings/{bookingId} {
      // المستخدم يمكنه قراءة حجوزاته الخاصة فقط
      allow read: if request.auth != null && 
        resource.data.userId == request.auth.uid;
      
      // المستخدم يمكنه إنشاء حجز جديد
      allow create: if request.auth != null && 
        request.resource.data.userId == request.auth.uid;
      
      // المستخدم يمكنه تحديث حجوزاته الخاصة
      allow update: if request.auth != null && 
        resource.data.userId == request.auth.uid;
      
      // الأدمن يمكنه قراءة جميع الحجوزات
      allow read: if request.auth != null && 
        exists(/databases/$(database)/documents/users/$(request.auth.uid)) &&
        get(/databases/$(database)/documents/users/$(request.auth.uid)).data.role == 'admin';
    }
    
    // قواعد التقييمات (reviews)
    match /reviews/{reviewId} {
      // الجميع يمكنهم قراءة التقييمات
      allow read: if request.auth != null;
      
      // المستخدم يمكنه إنشاء تقييم
      allow create: if request.auth != null && 
        request.resource.data.userId == request.auth.uid;
      
      // المستخدم يمكنه تحديث/حذف تقييمه الخاص
      allow update, delete: if request.auth != null && 
        resource.data.userId == request.auth.uid;
    }
  }
}
```

### الخطوة 4: حفظ القواعد
1. اضغط على زر **Publish** (نشر) في أعلى الصفحة
2. انتظر حتى تظهر رسالة "Rules published successfully"

## قواعد بديلة (للتطوير فقط - غير آمنة للإنتاج)

⚠️ **تحذير**: استخدم هذه القواعد فقط للتطوير والاختبار. لا تستخدمها في الإنتاج!

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

هذه القواعد تسمح لأي مستخدم مسجل بالدخول بقراءة وكتابة أي بيانات. استخدمها فقط للاختبار السريع.

## التحقق من القواعد

بعد حفظ القواعد:
1. حاول تسجيل الدخول مرة أخرى في التطبيق
2. يجب أن يعمل الآن بدون أخطاء PERMISSION_DENIED

## ملاحظات مهمة

1. **الأمان**: القواعد الأولى أكثر أماناً وتتحكم في الوصول بشكل دقيق
2. **الاختبار**: استخدم القواعد البديلة فقط للتطوير
3. **الإنتاج**: تأكد من استخدام القواعد الآمنة قبل نشر التطبيق

## استكشاف الأخطاء

إذا استمرت المشكلة:
1. تأكد من أن المستخدم مسجل الدخول في Firebase Authentication
2. تحقق من أن collection "users" موجود في Firestore
3. تأكد من حفظ القواعد ونشرها بنجاح
4. انتظر بضع دقائق حتى يتم تطبيق القواعد


