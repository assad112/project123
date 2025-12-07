package com.warmclouds.app.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.warmclouds.app.R;

/**
 * Utility class for loading and managing images in the application
 */
public class ImageUtils {

    /**
     * Load image from URL into ImageView with default placeholder
     * Automatically loads from internet and caches locally
     * @param context The context
     * @param imageUrl The image URL (from internet)
     * @param imageView The target ImageView
     */
    public static void loadImage(Context context, String imageUrl, ImageView imageView) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_nursery_empty);
            return;
        }

        // Create GlideUrl with headers to ensure proper loading
        GlideUrl glideUrl = new GlideUrl(imageUrl, new LazyHeaders.Builder()
                .addHeader("User-Agent", "Mozilla/5.0")
                .build());

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_nursery_empty)
                .error(R.drawable.ic_nursery_empty)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache both original and transformed
                .skipMemoryCache(false) // Enable memory cache
                .centerCrop();

        Glide.with(context)
                .load(glideUrl)
                .apply(options)
                .into(imageView);
    }

    /**
     * Load image from URL with custom placeholder
     * Automatically loads from internet and caches locally
     * @param context The context
     * @param imageUrl The image URL (from internet)
     * @param imageView The target ImageView
     * @param placeholderResId The placeholder resource ID
     */
    public static void loadImage(Context context, String imageUrl, ImageView imageView, int placeholderResId) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(placeholderResId);
            return;
        }

        GlideUrl glideUrl = new GlideUrl(imageUrl, new LazyHeaders.Builder()
                .addHeader("User-Agent", "Mozilla/5.0")
                .build());

        RequestOptions options = new RequestOptions()
                .placeholder(placeholderResId)
                .error(placeholderResId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .centerCrop();

        Glide.with(context)
                .load(glideUrl)
                .apply(options)
                .into(imageView);
    }

    /**
     * Load image with rounded corners from internet
     * Automatically loads from internet and caches locally
     * @param context The context
     * @param imageUrl The image URL (from internet)
     * @param imageView The target ImageView
     * @param cornerRadius The corner radius in pixels
     */
    public static void loadRoundedImage(Context context, String imageUrl, ImageView imageView, int cornerRadius) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_nursery_empty);
            return;
        }

        GlideUrl glideUrl = new GlideUrl(imageUrl, new LazyHeaders.Builder()
                .addHeader("User-Agent", "Mozilla/5.0")
                .build());

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_nursery_empty)
                .error(R.drawable.ic_nursery_empty)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .centerCrop();

        Glide.with(context)
                .load(glideUrl)
                .apply(options)
                .into(imageView);
        
        // Note: Rounded corners should be applied via XML using cardCornerRadius or clipToOutline
    }

    /**
     * Load circular image (for profile pictures) from internet
     * Automatically loads from internet and caches locally
     * @param context The context
     * @param imageUrl The image URL (from internet)
     * @param imageView The target ImageView
     */
    public static void loadCircularImage(Context context, String imageUrl, ImageView imageView) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_profile);
            return;
        }

        GlideUrl glideUrl = new GlideUrl(imageUrl, new LazyHeaders.Builder()
                .addHeader("User-Agent", "Mozilla/5.0")
                .build());

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .circleCrop();

        Glide.with(context)
                .load(glideUrl)
                .apply(options)
                .into(imageView);
    }

    /**
     * Load image for slider with full width from internet
     * Automatically loads from internet and caches locally
     * @param context The context
     * @param imageUrl The image URL (from internet)
     * @param imageView The target ImageView
     */
    public static void loadSliderImage(Context context, String imageUrl, ImageView imageView) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.ic_launcher_background);
            return;
        }

        GlideUrl glideUrl = new GlideUrl(imageUrl, new LazyHeaders.Builder()
                .addHeader("User-Agent", "Mozilla/5.0")
                .build());

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .centerCrop();

        Glide.with(context)
                .load(glideUrl)
                .apply(options)
                .into(imageView);
    }

    /**
     * Clear Glide cache
     * @param context The context
     */
    public static void clearCache(Context context) {
        Glide.get(context).clearMemory();
        new Thread(() -> Glide.get(context).clearDiskCache()).start();
    }
}

