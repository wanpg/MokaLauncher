package com.moka.launcher.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.bumptech.glide.util.Util;

import java.io.IOException;

class ResolveInfoIconDecoder implements ResourceDecoder<ResolveInfo, Drawable> {
    private final Context context;

    public ResolveInfoIconDecoder(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public Resource<Drawable> decode(ResolveInfo source, int width, int height, Options options)
            throws IOException {
        Drawable icon = source.loadIcon(context.getPackageManager());
        return new DrawableResource<Drawable>(icon) {
            @Override
            public Class<Drawable> getResourceClass() {
                return Drawable.class;
            }

            @Override
            public int getSize() { // best effort
                if (drawable instanceof BitmapDrawable) {
                    return Util.getBitmapByteSize(((BitmapDrawable) drawable).getBitmap());
                } else {
                    return 1;
                }
            }

            @Override
            public void recycle() { /* not from our pool */ }
        };
    }

    @Override
    public boolean handles(ResolveInfo source, Options options) throws IOException {
        return true;
    }
}