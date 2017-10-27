package com.moka.launcher.utils;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.module.GlideModule;

public class ApplicationIconModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Do nothing.
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        registry.append(ResolveInfo.class, ResolveInfo.class,
                new ModelLoaderFactory<ResolveInfo, ResolveInfo>() {
                    @Override
                    public ModelLoader<ResolveInfo, ResolveInfo> build(
                            MultiModelLoaderFactory multiFactory) {
                        return new ResolveInfoModelLoader();
                    }

                    @Override
                    public void teardown() {

                    }
                }).append(ResolveInfo.class, Drawable.class, new ResolveInfoIconDecoder(context));
    }
}