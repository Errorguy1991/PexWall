package com.pexwall.app.data.repository;

import com.pexwall.app.data.api.PexelsApi;
import com.pexwall.app.data.db.WallpaperDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class WallpaperRepository_Factory implements Factory<WallpaperRepository> {
  private final Provider<PexelsApi> apiProvider;

  private final Provider<WallpaperDao> daoProvider;

  public WallpaperRepository_Factory(Provider<PexelsApi> apiProvider,
      Provider<WallpaperDao> daoProvider) {
    this.apiProvider = apiProvider;
    this.daoProvider = daoProvider;
  }

  @Override
  public WallpaperRepository get() {
    return newInstance(apiProvider.get(), daoProvider.get());
  }

  public static WallpaperRepository_Factory create(Provider<PexelsApi> apiProvider,
      Provider<WallpaperDao> daoProvider) {
    return new WallpaperRepository_Factory(apiProvider, daoProvider);
  }

  public static WallpaperRepository newInstance(PexelsApi api, WallpaperDao dao) {
    return new WallpaperRepository(api, dao);
  }
}
