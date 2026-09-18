package com.pexwall.app.ui.history;

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
public final class HistoryViewModel_Factory implements Factory<HistoryViewModel> {
  private final Provider<WallpaperRepository> repositoryProvider;

  private final Provider<WallpaperSetter> wallpaperSetterProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public HistoryViewModel_Factory(Provider<WallpaperRepository> repositoryProvider,
      Provider<WallpaperSetter> wallpaperSetterProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.wallpaperSetterProvider = wallpaperSetterProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public HistoryViewModel get() {
    return newInstance(repositoryProvider.get(), wallpaperSetterProvider.get(), preferencesManagerProvider.get());
  }

  public static HistoryViewModel_Factory create(Provider<WallpaperRepository> repositoryProvider,
      Provider<WallpaperSetter> wallpaperSetterProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new HistoryViewModel_Factory(repositoryProvider, wallpaperSetterProvider, preferencesManagerProvider);
  }

  public static HistoryViewModel newInstance(WallpaperRepository repository,
      WallpaperSetter wallpaperSetter, PreferencesManager preferencesManager) {
    return new HistoryViewModel(repository, wallpaperSetter, preferencesManager);
  }
}
