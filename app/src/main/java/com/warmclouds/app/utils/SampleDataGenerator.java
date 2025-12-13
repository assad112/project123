package com.warmclouds.app.utils;

import com.google.firebase.firestore.FirebaseFirestore;
import com.warmclouds.app.models.Nursery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class to add sample nurseries to Firebase Firestore
 * Call addSampleNurseries() to add 5 nurseries automatically
 */
public class SampleDataGenerator {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface OnDataAddedListener {
        void onSuccess(int count);
        void onError(String error);
        void onProgress(int current, int total);
    }

    /**
     * Add 5 sample nurseries to Firestore automatically
     */
    public static void addSampleNurseries(OnDataAddedListener listener) {
        List<Nursery> nurseries = createSampleNurseries();
        
        final int[] successCount = {0};
        final int[] errorCount = {0};
        final int total = nurseries.size();

        for (int i = 0; i < nurseries.size(); i++) {
            final int index = i;
            Nursery nursery = nurseries.get(i);
            
            db.collection("nurseries")
                    .add(nursery)
                    .addOnSuccessListener(documentReference -> {
                        successCount[0]++;
                        listener.onProgress(successCount[0] + errorCount[0], total);
                        
                        if (successCount[0] + errorCount[0] == total) {
                            if (errorCount[0] == 0) {
                                listener.onSuccess(successCount[0]);
                            } else {
                                listener.onError("Added " + successCount[0] + " nurseries, " + errorCount[0] + " failed");
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        errorCount[0]++;
                        listener.onProgress(successCount[0] + errorCount[0], total);
                        
                        if (successCount[0] + errorCount[0] == total) {
                            listener.onError("Added " + successCount[0] + " nurseries, " + errorCount[0] + " failed: " + e.getMessage());
                        }
                    });
        }
    }

    private static List<Nursery> createSampleNurseries() {
        List<Nursery> nurseries = new ArrayList<>();

        // Nursery 1: Sunny Kids Nursery
        Nursery nursery1 = new Nursery();
        nursery1.setName("Sunny Kids Nursery");
        nursery1.setDescription("A bright and cheerful learning environment with experienced teachers and modern facilities. We focus on early childhood development through play-based learning.");
        nursery1.setLocation("Muscat");
        nursery1.setImages(Arrays.asList(
                "https://images.unsplash.com/photo-1587654780291-39c9404d746b?w=800",
                "https://images.unsplash.com/photo-1503454537195-1dcabb73ffb9?w=800",
                "https://images.unsplash.com/photo-1600948836101-f9ffda59d250?w=800"
        ));
        nursery1.setRating(4.8);
        nursery1.setRegistrationFee(100);
        nursery1.setMonthlyFee(180);
        nursery1.setPhone("+968 24123456");
        nursery1.setEmail("info@sunnykids.om");
        nursery1.setInstagram("@sunnykidsnursery");
        nursery1.setFeatures(Arrays.asList(
                "Air Conditioned Classrooms",
                "CCTV Security System",
                "Outdoor Playground",
                "Qualified Teachers",
                "Healthy Meals Provided",
                "Art & Music Classes"
        ));
        nursery1.setAgeGroups(Arrays.asList("1-2 years", "3-4 years", "5-6 years"));
        nursery1.setFacilities("Modern air-conditioned classrooms, Large outdoor play area, Indoor activity room, Library corner, Art studio, Safe and secure environment with CCTV");
        nursery1.setActive(true);
        nurseries.add(nursery1);

        // Nursery 2: Rainbow Learning Center
        Nursery nursery2 = new Nursery();
        nursery2.setName("Rainbow Learning Center");
        nursery2.setDescription("Where every child shines! Our center offers a colorful and engaging environment that promotes creativity, social skills, and academic readiness.");
        nursery2.setLocation("Salalah");
        nursery2.setImages(Arrays.asList(
                "https://images.unsplash.com/photo-1560421683-6856ea585c78?w=800",
                "https://images.unsplash.com/photo-1564007162625-3f99e0e0b2b9?w=800",
                "https://images.unsplash.com/photo-1576267423445-b2e0074d68a4?w=800"
        ));
        nursery2.setRating(4.6);
        nursery2.setRegistrationFee(80);
        nursery2.setMonthlyFee(150);
        nursery2.setPhone("+968 23456789");
        nursery2.setEmail("hello@rainbowlearning.om");
        nursery2.setInstagram("@rainbowlearningcenter");
        nursery2.setFeatures(Arrays.asList(
                "Bilingual Program (English/Arabic)",
                "Small Class Sizes",
                "Daily Reports to Parents",
                "Indoor & Outdoor Play",
                "Nutritious Snacks",
                "Swimming Pool"
        ));
        nursery2.setAgeGroups(Arrays.asList("2-3 years", "4-5 years"));
        nursery2.setFacilities("Colorful themed classrooms, Swimming pool (supervised), Large garden with slides and swings, Computer lab, Music room, Parent waiting area");
        nursery2.setActive(true);
        nurseries.add(nursery2);

        // Nursery 3: Little Explorers Academy
        Nursery nursery3 = new Nursery();
        nursery3.setName("Little Explorers Academy");
        nursery3.setDescription("Nurturing curious minds through exploration and discovery. We provide a Montessori-inspired curriculum that encourages independence and creativity.");
        nursery3.setLocation("Sohar");
        nursery3.setImages(Arrays.asList(
                "https://images.unsplash.com/photo-1544776193-352d25ca82cd?w=800",
                "https://images.unsplash.com/photo-1587654780291-39c9404d746b?w=800",
                "https://images.unsplash.com/photo-1503454537195-1dcabb73ffb9?w=800"
        ));
        nursery3.setRating(4.9);
        nursery3.setRegistrationFee(120);
        nursery3.setMonthlyFee(200);
        nursery3.setPhone("+968 26789012");
        nursery3.setEmail("contact@littleexplorers.om");
        nursery3.setInstagram("@littleexplorersacademy");
        nursery3.setFeatures(Arrays.asList(
                "Montessori Method",
                "Certified Teachers",
                "Parent-Teacher Meetings",
                "Educational Field Trips",
                "Science Lab",
                "Drama & Theater"
        ));
        nursery3.setAgeGroups(Arrays.asList("18 months-2 years", "3-4 years", "5-6 years"));
        nursery3.setFacilities("Montessori classrooms with learning materials, Science discovery corner, Drama and role-play area, Sandbox and water play, Reading nook, Secure entrance with access control");
        nursery3.setActive(true);
        nurseries.add(nursery3);

        // Nursery 4: Bright Beginnings Nursery
        Nursery nursery4 = new Nursery();
        nursery4.setName("Bright Beginnings Nursery");
        nursery4.setDescription("Starting your child's educational journey on the right foot. We offer a warm, caring environment with focus on emotional and social development.");
        nursery4.setLocation("Nizwa");
        nursery4.setImages(Arrays.asList(
                "https://images.unsplash.com/photo-1600948836101-f9ffda59d250?w=800",
                "https://images.unsplash.com/photo-1560421683-6856ea585c78?w=800",
                "https://images.unsplash.com/photo-1564007162625-3f99e0e0b2b9?w=800"
        ));
        nursery4.setRating(4.5);
        nursery4.setRegistrationFee(75);
        nursery4.setMonthlyFee(140);
        nursery4.setPhone("+968 25234567");
        nursery4.setEmail("info@brightbeginnings.om");
        nursery4.setInstagram("@brightbeginningsnizwa");
        nursery4.setFeatures(Arrays.asList(
                "Experienced Caregivers",
                "Home-like Environment",
                "Flexible Timings",
                "Organic Meals",
                "Sleep Rooms",
                "Parent App Updates"
        ));
        nursery4.setAgeGroups(Arrays.asList("6 months-1 year", "2-3 years", "4-5 years"));
        nursery4.setFacilities("Cozy age-appropriate rooms, Separate sleep area with comfortable cribs, Clean and hygienic facilities, Kitchen with organic meal preparation, Sensory play area, Safe outdoor space");
        nursery4.setActive(true);
        nurseries.add(nursery4);

        // Nursery 5: Creative Kids Haven
        Nursery nursery5 = new Nursery();
        nursery5.setName("Creative Kids Haven");
        nursery5.setDescription("A haven for creativity and imagination! We believe in learning through art, music, and hands-on activities. Every child is an artist here.");
        nursery5.setLocation("Muscat");
        nursery5.setImages(Arrays.asList(
                "https://images.unsplash.com/photo-1503454537195-1dcabb73ffb9?w=800",
                "https://images.unsplash.com/photo-1587654780291-39c9404d746b?w=800",
                "https://images.unsplash.com/photo-1576267423445-b2e0074d68a4?w=800"
        ));
        nursery5.setRating(4.7);
        nursery5.setRegistrationFee(90);
        nursery5.setMonthlyFee(165);
        nursery5.setPhone("+968 24567890");
        nursery5.setEmail("hello@creativekidshaven.om");
        nursery5.setInstagram("@creativekidshaven");
        nursery5.setFeatures(Arrays.asList(
                "Art & Craft Studio",
                "Music & Dance Classes",
                "Drama Workshops",
                "Photography Sessions",
                "Cultural Activities",
                "Annual Art Exhibition"
        ));
        nursery5.setAgeGroups(Arrays.asList("2-3 years", "3-4 years", "5-6 years"));
        nursery5.setFacilities("Dedicated art studio with supplies, Music room with instruments, Performance stage for drama, Gallery wall for children's artwork, Creative play zones, Bright and inspiring spaces");
        nursery5.setActive(true);
        nurseries.add(nursery5);

        return nurseries;
    }
}
