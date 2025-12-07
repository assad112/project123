# حل مشكلة FAILED_PRECONDITION في Firestore

## المشكلة
```
FAILED_PRECONDITION: The query requires an index
```

هذا الخطأ يحدث عندما يستخدم الاستعلام `whereEqualTo` مع `orderBy` على حقول مختلفة في Firestore.

## الحل المؤقت (تم تطبيقه)
تم تعديل الكود في `ParentDashboardActivity.java` لإزالة `orderBy` من الاستعلام والقيام بالترتيب في الكود بعد جلب البيانات:

```java
// بدلاً من:
.whereEqualTo("userId", currentUser.getUid())
.orderBy("bookingDate", Query.Direction.DESCENDING)
.limit(5)

// أصبح:
.whereEqualTo("userId", currentUser.getUid())
// ثم الترتيب في الكود بعد جلب البيانات
```

هذا الحل يعمل لكنه قد يكون أبطأ قليلاً إذا كان هناك الكثير من الحجوزات.

## الحل الأفضل: إنشاء فهرس مركب في Firestore

للحصول على أداء أفضل، يجب إنشاء فهرس مركب في Firebase Console:

### الخطوات:

1. افتح [Firebase Console](https://console.firebase.google.com/)
2. اختر مشروعك **warm-clouds**
3. انتقل إلى **Firestore Database**
4. اضغط على تبويب **Indexes** (الفهارس)
5. اضغط على **Create Index** (إنشاء فهرس)
6. املأ البيانات التالية:
   - **Collection ID**: `bookings`
   - **Fields to index**:
     - Field 1: `userId` - Type: `Ascending`
     - Field 2: `bookingDate` - Type: `Descending`
   - **Query scope**: `Collection`
7. اضغط **Create** (إنشاء)

### بعد إنشاء الفهرس:

1. انتظر حتى يتم إنشاء الفهرس (قد يستغرق بضع دقائق)
2. يمكنك العودة إلى الكود واستخدام الاستعلام الأصلي:
   ```java
   db.collection("bookings")
       .whereEqualTo("userId", currentUser.getUid())
       .orderBy("bookingDate", Query.Direction.DESCENDING)
       .limit(5)
       .get()
   ```

## ملاحظات

- الفهرس المركب يحسن الأداء بشكل كبير عند وجود الكثير من البيانات
- الحل المؤقت (الترتيب في الكود) يعمل بشكل جيد للبيانات الصغيرة
- يمكنك استخدام أي من الحلين حسب احتياجاتك

