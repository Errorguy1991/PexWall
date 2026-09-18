package com.pexwall.app.ui.home;

import com.pexwall.app.data.preferences.PreferencesManager;
import com.pexwall.app.data.repository.WallpaperRepository;
import com.pexwall.app.util.WallpaperSetter;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<WallpaperRepository> repositoryProvider;

  private final Provider<WallpaperSetter> wallpaperSetterProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public HomeViewModel_Factory(Provider<WallpaperRepository> repositoryProvider,
      Provider<WallpaperSetter> wallpaperSetterProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.wallpaperSetterProvider = wallpaperSetterProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(repositoryProvider.get(), wallpaperSetterProvider.get(), preferencesManagerProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<WallpaperRepository> repositoryProvider,
      Provider<WallpaperSetter> wallpaperSetterProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new HomeViewModel_Factory(repositoryProvider, wallpaperSetterProvider, preferencesManagerProvider);
  }

  public static HomeViewModel newInstance(WallpaperRepository repository,
      WallpaperSetter wallpaperSetter, PreferencesManager preferencesManager) {
    return new HomeViewModel(repository, wallpaperSetter, preferencesManager);
  }
}
