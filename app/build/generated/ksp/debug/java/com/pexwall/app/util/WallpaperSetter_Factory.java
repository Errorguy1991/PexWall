package com.pexwall.app.util;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class WallpaperSetter_Factory implements Factory<WallpaperSetter> {
  private final Provider<Context> contextProvider;

  public WallpaperSetter_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public WallpaperSetter get() {
    return newInstance(contextProvider.get());
  }

  public static WallpaperSetter_Factory create(Provider<Context> contextProvider) {
    return new WallpaperSetter_Factory(contextProvider);
  }

  public static WallpaperSetter newInstance(Context context) {
    return new WallpaperSetter(context);
  }
}
