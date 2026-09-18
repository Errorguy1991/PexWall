package com.pexwall.app.data.repository;

import com.pexwall.app.data.api.PexelsApi;
import com.pexwall.app.data.db.WallpaperDao;
import com.pexwall.app.data.preferences.PreferencesManager;
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

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public WallpaperRepository_Factory(Provider<PexelsApi> apiProvider,
      Provider<WallpaperDao> daoProvider, Provider<PreferencesManager> preferencesManagerProvider) {
    this.apiProvider = apiProvider;
    this.daoProvider = daoProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public WallpaperRepository get() {
    return newInstance(apiProvider.get(), daoProvider.get(), preferencesManagerProvider.get());
  }

  public static WallpaperRepository_Factory create(Provider<PexelsApi> apiProvider,
      Provider<WallpaperDao> daoProvider, Provider<PreferencesManager> preferencesManagerProvider) {
    return new WallpaperRepository_Factory(apiProvider, daoProvider, preferencesManagerProvider);
  }

  public static WallpaperRepository newInstance(PexelsApi api, WallpaperDao dao,
      PreferencesManager preferencesManager) {
    return new WallpaperRepository(api, dao, preferencesManager);
  }
}
