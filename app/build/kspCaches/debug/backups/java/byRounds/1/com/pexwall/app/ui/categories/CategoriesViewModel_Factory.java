package com.pexwall.app.ui.categories;

import com.pexwall.app.data.preferences.PreferencesManager;
import com.pexwall.app.data.repository.WallpaperRepository;
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
public final class CategoriesViewModel_Factory implements Factory<CategoriesViewModel> {
  private final Provider<WallpaperRepository> repositoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public CategoriesViewModel_Factory(Provider<WallpaperRepository> repositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public CategoriesViewModel get() {
    return newInstance(repositoryProvider.get(), preferencesManagerProvider.get());
  }

  public static CategoriesViewModel_Factory create(Provider<WallpaperRepository> repositoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new CategoriesViewModel_Factory(repositoryProvider, preferencesManagerProvider);
  }

  public static CategoriesViewModel newInstance(WallpaperRepository repository,
      PreferencesManager preferencesManager) {
    return new CategoriesViewModel(repository, preferencesManager);
  }
}
