# Warm Clouds - تطبيق البحث وحجز الحضانات

تطبيق أندرويد (Java + Firebase) يساعد أولياء الأمور في البحث عن الحضانات، مقارنة الأسعار والمواقع والتقييمات، ثم حجز مقعد لطفلهم مع دفع مبلغ التسجيل إلكترونيًا.

## المميزات

### للمستخدمين (أولياء الأمور):
- 🔍 البحث عن الحضانات مع فلاتر متعددة (السعر، الموقع، التقييم)
- 📱 عرض تفاصيل كاملة لكل حضانة
- ⭐ عرض التقييمات والتعليقات
- 📅 حجز مقعد لطفل
- 💳 دفع رسوم التسجيل إلكترونيًا

### للأدمن:
- ➕ إضافة حضانات جديدة
- ✏️ تعديل بيانات الحضانات
- 🗑️ حذف/تعطيل الحضانات
- 📊 إدارة الحجوزات

## التقنيات المستخدمة

- **اللغة**: Java
- **Backend**: Firebase (Authentication, Firestore, Storage)
- **UI**: Material Design Components
- **Image Loading**: Glide
- **Architecture**: MVC Pattern

## الهوية البصرية

- **اللون الأساسي**: أزرق فاتح سماوي (#87CEEB)
- **اللون المميز**: بني (#8B4513)
- **الخلفية**: رمادي فاتح/أبيض

## إعداد المشروع

### 1. إعداد Firebase

1. اذهب إلى [Firebase Console](https://console.firebase.google.com/)
2. أنشئ مشروع جديد أو استخدم مشروع موجود
3. أضف تطبيق أندرويد جديد
4. استخدم `com.warmclouds.app` كـ Package Name
5. حمّل ملف `google-services.json` وضعه في مجلد `app/`
6. فعّل Authentication (Email/Password)
7. أنشئ Firestore Database

### 2. هيكل قاعدة البيانات

#### Collection: `users`
```json
{
  "id": "user_id",
  "email": "user@example.com",
  "name": "اسم المستخدم",
  "phone": "1234567890",
  "role": "parent" | "admin",
  "createdAt": timestamp
}
```

#### Collection: `nurseries`
```json
{
  "id": "nursery_id",
  "name": "اسم الحضانة",
  "description": "الوصف",
  "location": "الولاية",
  "address": "العنوان",
  "phone": "1234567890",
  "email": "email@example.com",
  "instagram": "instagram_handle",
  "images": ["url1", "url2"],
  "features": ["ميزة 1", "ميزة 2"],
  "registrationFee": 1000,
  "monthlyFee": 500,
  "ageGroups": ["2-3 سنوات", "3-4 سنوات"],
  "facilities": ["مرافق 1", "مرافق 2"],
  "rating": 4.5,
  "reviewCount": 10,
  "isActive": true
}
```

#### Collection: `bookings`
```json
{
  "id": "booking_id",
  "userId": "user_id",
  "nurseryId": "nursery_id",
  "nurseryName": "اسم الحضانة",
  "childName": "اسم الطفل",
  "childAge": "3",
  "ageGroup": "2-3 سنوات",
  "parentName": "اسم ولي الأمر",
  "parentPhone": "1234567890",
  "parentEmail": "email@example.com",
  "registrationFee": 1000,
  "status": "pending" | "confirmed" | "cancelled",
  "bookingDate": timestamp,
  "bookingCode": "ABC12345"
}
```

#### Collection: `reviews`
```json
{
  "id": "review_id",
  "userId": "user_id",
  "userName": "اسم المستخدم",
  "nurseryId": "nursery_id",
  "rating": 4.5,
  "comment": "التعليق",
  "timestamp": timestamp
}
```

### 3. بناء المشروع

1. افتح المشروع في Android Studio
2. انتظر حتى يتم تحميل جميع التبعيات
3. تأكد من وجود ملف `google-services.json` في مجلد `app/`
4. اضغط على Build > Make Project

## الصفحات الرئيسية

1. **WelcomeActivity** - صفحة البداية/الترحيب
2. **LoginActivity** - تسجيل الدخول (ولي أمر/أدمن)
3. **SearchActivity** - البحث عن الحضانات
4. **NurseryDetailsActivity** - تفاصيل الحضانة
5. **BookingActivity** - حجز مقعد
6. **PaymentActivity** - صفحة الدفع
7. **AdminActivity** - لوحة تحكم الأدمن
8. **AddEditNurseryActivity** - إضافة/تعديل حضانة

## ملاحظات مهمة

- تأكد من إضافة ملف `google-services.json` الصحيح من Firebase
- قم بتفعيل Authentication و Firestore في Firebase Console
- يمكنك إضافة نظام دفع حقيقي (مثل Stripe أو PayPal) في `PaymentActivity`
- يمكنك إضافة رفع الصور إلى Firebase Storage في `AddEditNurseryActivity`

## الترخيص

هذا المشروع مفتوح المصدر ومتاح للاستخدام والتعديل.


