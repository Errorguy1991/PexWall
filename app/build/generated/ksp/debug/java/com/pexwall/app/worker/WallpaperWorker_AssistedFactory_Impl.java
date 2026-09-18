package com.pexwall.app.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class WallpaperWorker_AssistedFactory_Impl implements WallpaperWorker_AssistedFactory {
  private final WallpaperWorker_Factory delegateFactory;

  WallpaperWorker_AssistedFactory_Impl(WallpaperWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public WallpaperWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<WallpaperWorker_AssistedFactory> create(
      WallpaperWorker_Factory delegateFactory) {
    return InstanceFactory.create(new WallpaperWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<WallpaperWorker_AssistedFactory> createFactoryProvider(
      WallpaperWorker_Factory delegateFactory) {
    return InstanceFactory.create(new WallpaperWorker_AssistedFactory_Impl(delegateFactory));
  }
}
