# Warm Clouds - Nursery Management App

An Android application (Java + Firebase) that helps parents search for nurseries, compare prices, locations, and ratings, then book a seat for their child with online payment for registration fees.

## Features

### For Users (Parents):
- 🔍 Search for nurseries with multiple filters (price, location, rating)
- 📱 View complete details for each nursery
- ⭐ View ratings and reviews
- 📅 Book a seat for a child
- 💳 Pay registration fees online
- 🌐 Automatic image loading from internet URLs

### For Admins:
- ➕ Add new nurseries
- ✏️ Edit nursery data
- 🗑️ Delete/disable nurseries
- 📊 Manage bookings
- ✅ Accept/reject booking requests

## Technologies Used

- **Language**: Java
- **Backend**: Firebase (Authentication, Firestore, Storage)
- **UI**: Material Design Components
- **Image Loading**: Glide
- **Architecture**: MVC Pattern

## Visual Identity

- **Primary Color**: Light Sky Blue (#4A90E2)
- **Accent Color**: Warm Brown (#D4A574)
- **Background**: Light Gray/White

## Project Setup

### 1. Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or use an existing one
3. Add a new Android app
4. Use `com.warmclouds.app` as Package Name
5. Download `google-services.json` and place it in the `app/` directory
6. Enable Authentication (Email/Password)
7. Create Firestore Database
8. Add SHA-1 and SHA-256 fingerprints to Firebase project settings

### 2. Database Structure

#### Collection: `users`
```json
{
  "id": "user_id",
  "email": "user@example.com",
  "name": "User Name",
  "role": "parent" | "admin",
  "phone": "0501234567"
}
```

#### Collection: `nurseries`
```json
{
  "id": "nursery_id",
  "name": "Nursery Name",
  "description": "Description",
  "location": "Location/State",
  "address": "Full Address",
  "phone": "Phone Number",
  "email": "Email",
  "instagram": "Instagram Account",
  "images": ["https://...", "https://..."],
  "features": ["Feature 1", "Feature 2"],
  "registrationFee": 1000,
  "monthlyFee": 500,
  "ageGroups": ["2-3 years", "3-4 years"],
  "facilities": ["Playground", "Library"],
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
  "nurseryName": "Nursery Name",
  "childName": "Child Name",
  "childAge": "5",
  "ageGroup": "4-5 years",
  "parentName": "Parent Name",
  "parentPhone": "0501234567",
  "parentEmail": "parent@example.com",
  "registrationFee": 1000,
  "bookingCode": "ABC12345",
  "status": "pending" | "confirmed" | "cancelled",
  "bookingDate": 1234567890
}
```

## Installation

1. Clone the repository:
```bash
git clone https://github.com/assad112/project123.git
```

2. Open the project in Android Studio

3. Add your `google-services.json` file to the `app/` directory

4. Sync project with Gradle files

5. Run the app on an emulator or physical device

## Configuration

### Firebase Security Rules

Update Firestore Security Rules in Firebase Console:
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

### Image Loading

The app automatically loads nursery images from internet URLs. Add image URLs to the `images` array in Firestore nursery documents.

## Screenshots

- Welcome Screen
- Login/Register
- Parent Dashboard
- Search Nurseries
- Nursery Details
- Booking
- Payment
- Admin Panel

## Language

The entire application is in **English**.

## License

This project is private and proprietary.

## Author

Warm Clouds Team

## Support

For issues and questions, please open an issue in this repository.
