package com.pexwall.app.di;

import com.pexwall.app.data.db.AppDatabase;
import com.pexwall.app.data.db.WallpaperDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class AppModule_ProvideWallpaperDaoFactory implements Factory<WallpaperDao> {
  private final Provider<AppDatabase> databaseProvider;

  public AppModule_ProvideWallpaperDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public WallpaperDao get() {
    return provideWallpaperDao(databaseProvider.get());
  }

  public static AppModule_ProvideWallpaperDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new AppModule_ProvideWallpaperDaoFactory(databaseProvider);
  }

  public static WallpaperDao provideWallpaperDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideWallpaperDao(database));
  }
}
