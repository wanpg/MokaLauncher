package com.moka.launcher.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.support.annotation.Nullable;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;

class ResolveInfoModelLoader implements ModelLoader<ResolveInfo, ResolveInfo> {
    @Nullable
    @Override
    public LoadData<ResolveInfo> buildLoadData(final ResolveInfo resolveInfo, int width,
                                               int height, Options options) {
        return new LoadData<>(new ObjectKey(resolveInfo), new DataFetcher<ResolveInfo>() {
            @Override
            public void loadData(Priority priority, DataCallback<? super ResolveInfo> callback) {
                callback.onDataReady(resolveInfo);
            }

            @Override
            public void cleanup() {

            }

            @Override
            public void cancel() {

            }

            @Override
            public Class<ResolveInfo> getDataClass() {
                return ResolveInfo.class;
            }

            @Override
            public DataSource getDataSource() {
                return DataSource.LOCAL;
            }
        });
    }

    @Override
    public boolean handles(ResolveInfo resolveInfo) {
        return true;
    }
}