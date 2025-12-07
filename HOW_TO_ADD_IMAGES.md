# دليل إضافة الصور للتطبيق

## أنواع الصور وأين تضعها:

### 1. الصور الثابتة (أيقونات، خلفيات)
**المكان:** `app/src/main/res/drawable/`

**متى تستخدم:**
- أيقونات التطبيق
- خلفيات ثابتة
- صور placeholder

**كيفية الإضافة:**
1. انسخ الصور إلى مجلد `app/src/main/res/drawable/`
2. استخدم أسماء صحيحة (أحرف صغيرة، أرقام، underscore فقط)
3. مثال: `nursery_placeholder.png`, `logo.png`

**الاستخدام في الكود:**
```java
imageView.setImageResource(R.drawable.nursery_placeholder);
```

---

### 2. صور الحضانات (يجب رفعها على Firebase Storage)

**المكان:** Firebase Storage

**الخطوات:**

#### أ) رفع الصور يدوياً على Firebase Storage:

1. اذهب إلى [Firebase Console](https://console.firebase.google.com/)
2. اختر مشروعك
3. اذهب إلى **Storage** من القائمة الجانبية
4. اضغط **Get Started** إذا كان أول مرة
5. اضغط **Upload File**
6. اختر الصور وارفعها
7. انسخ رابط الصورة (URL)

#### ب) إضافة روابط الصور في Firestore:

1. اذهب إلى **Firestore Database**
2. افتح collection `nurseries`
3. اختر أو أنشئ مستند حضانة
4. أضف حقل `images` من نوع Array
5. أضف روابط الصور كعناصر في المصفوفة:
   ```
   images: [
     "https://firebasestorage.googleapis.com/.../image1.jpg",
     "https://firebasestorage.googleapis.com/.../image2.jpg"
   ]
   ```

---

### 3. رفع الصور من التطبيق (برمجياً)

إذا أردت إضافة ميزة رفع الصور من التطبيق، يمكن استخدام الكود التالي:

```java
// في AddEditNurseryActivity
private void uploadImage(Uri imageUri) {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    
    // إنشاء مسار فريد للصورة
    String fileName = "nurseries/" + System.currentTimeMillis() + ".jpg";
    StorageReference imageRef = storageRef.child(fileName);
    
    // رفع الصورة
    imageRef.putFile(imageUri)
        .addOnSuccessListener(taskSnapshot -> {
            // الحصول على رابط الصورة
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                // حفظ imageUrl في Firestore
                saveImageUrl(imageUrl);
            });
        })
        .addOnFailureListener(e -> {
            Toast.makeText(this, "فشل رفع الصورة: " + e.getMessage(), 
                Toast.LENGTH_SHORT).show();
        });
}
```

---

## ملخص سريع:

| نوع الصورة | المكان | الاستخدام |
|------------|--------|-----------|
| أيقونات/خلفيات | `res/drawable/` | صور ثابتة في التطبيق |
| صور الحضانات | Firebase Storage | صور ديناميكية من قاعدة البيانات |
| صور المستخدمين | Firebase Storage | صور البروفايل |

---

## نصائح مهمة:

1. **حجم الصور:** استخدم صور بحجم معقول (أقل من 2MB)
2. **الصيغ المدعومة:** JPG, PNG, WebP
3. **الأسماء:** استخدم أسماء واضحة ووصفية
4. **التحسين:** قم بضغط الصور قبل الرفع لتوفير المساحة

---

## مثال عملي:

### إضافة صورة placeholder:

1. ضع الصورة في: `app/src/main/res/drawable/nursery_placeholder.png`
2. استخدمها في الكود:
```java
ImageView imageView = findViewById(R.id.imageView);
imageView.setImageResource(R.drawable.nursery_placeholder);
```

### إضافة صور حضانة:

1. ارفع الصور على Firebase Storage
2. انسخ الروابط
3. أضفها في Firestore في حقل `images`:
```json
{
  "name": "حضانة المثال",
  "images": [
    "https://firebasestorage.googleapis.com/.../image1.jpg",
    "https://firebasestorage.googleapis.com/.../image2.jpg"
  ]
}
```

التطبيق سيقوم بتحميل الصور تلقائياً من هذه الروابط! 🎉

