package com.pexwall.app.data.repository;

import com.pexwall.app.data.api.BingApi;
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

  private final Provider<BingApi> bingApiProvider;

  private final Provider<PreferencesManager> prefsProvider;

  private final Provider<WallpaperDao> daoProvider;

  public WallpaperRepository_Factory(Provider<PexelsApi> apiProvider,
      Provider<BingApi> bingApiProvider, Provider<PreferencesManager> prefsProvider,
      Provider<WallpaperDao> daoProvider) {
    this.apiProvider = apiProvider;
    this.bingApiProvider = bingApiProvider;
    this.prefsProvider = prefsProvider;
    this.daoProvider = daoProvider;
  }

  @Override
  public WallpaperRepository get() {
    return newInstance(apiProvider.get(), bingApiProvider.get(), prefsProvider.get(), daoProvider.get());
  }

  public static WallpaperRepository_Factory create(Provider<PexelsApi> apiProvider,
      Provider<BingApi> bingApiProvider, Provider<PreferencesManager> prefsProvider,
      Provider<WallpaperDao> daoProvider) {
    return new WallpaperRepository_Factory(apiProvider, bingApiProvider, prefsProvider, daoProvider);
  }

  public static WallpaperRepository newInstance(PexelsApi api, BingApi bingApi,
      PreferencesManager prefs, WallpaperDao dao) {
    return new WallpaperRepository(api, bingApi, prefs, dao);
  }
}
