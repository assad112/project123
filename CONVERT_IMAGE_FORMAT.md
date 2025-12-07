# تحويل صيغة الصورة من JFIF إلى PNG/JPG

## المشكلة:
Android لا يدعم صيغة `.jfif` مباشرة. يجب تحويل الصورة إلى `.png` أو `.jpg`.

---

## الحل السريع:

### الطريقة 1: استخدام Paint (مدمج في Windows)

1. افتح الصورة `logo_warm_clouds.jfif` بزر الماوس الأيمن
2. اختر **Open with → Paint**
3. اضغط **File → Save As**
4. اختر **PNG picture** أو **JPEG picture**
5. احفظ باسم: `logo_warm_clouds.png` (أو `.jpg`)
6. انسخ الملف الجديد إلى: `app/src/main/res/drawable/`
7. احذف الملف القديم `.jfif`

---

### الطريقة 2: استخدام أداة تحويل أونلاين

1. اذهب إلى: https://convertio.co/jfif-png/ أو https://cloudconvert.com/jfif-to-png
2. ارفع الصورة `logo_warm_clouds.jfif`
3. اختر التحويل إلى PNG
4. حمّل الصورة المحولة
5. انسخها إلى: `app/src/main/res/drawable/logo_warm_clouds.png`
6. احذف الملف القديم `.jfif`

---

### الطريقة 3: استخدام PowerShell (سريع)

افتح PowerShell في مجلد الصورة واكتب:

```powershell
# تحويل JFIF إلى PNG
Add-Type -AssemblyName System.Drawing
$image = [System.Drawing.Image]::FromFile("logo_warm_clouds.jfif")
$image.Save("logo_warm_clouds.png", [System.Drawing.Imaging.ImageFormat]::Png)
$image.Dispose()
```

ثم انسخ `logo_warm_clouds.png` إلى: `app/src/main/res/drawable/`

---

## بعد التحويل:

1. ✅ تأكد من أن الملف الجديد اسمه: `logo_warm_clouds.png` (أو `.jpg`)
2. ✅ ضعه في: `app/src/main/res/drawable/`
3. ✅ احذف الملف القديم `.jfif`
4. ✅ افتح Android Studio
5. ✅ اضغط **File → Sync Project with Gradle Files**
6. ✅ أعد بناء المشروع: **Build → Rebuild Project**

---

## ملاحظات:

- **PNG** أفضل للشعارات (يدعم الشفافية)
- **JPG** أصغر حجماً لكن لا يدعم الشفافية
- تأكد من أن اسم الملف: `logo_warm_clouds.png` (أحرف صغيرة فقط)

---

## ✅ بعد التحويل:

شاشة Splash Screen جاهزة وستظهر الصورة تلقائياً عند فتح التطبيق! 🎉

