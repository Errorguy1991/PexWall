package com.pexwall.app.ui.categories;

import com.pexwall.app.data.preferences.PreferencesManager;
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
  private final Provider<PreferencesManager> preferencesManagerProvider;

  public CategoriesViewModel_Factory(Provider<PreferencesManager> preferencesManagerProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  @Override
  public CategoriesViewModel get() {
    return newInstance(preferencesManagerProvider.get());
  }

  public static CategoriesViewModel_Factory create(
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new CategoriesViewModel_Factory(preferencesManagerProvider);
  }

  public static CategoriesViewModel newInstance(PreferencesManager preferencesManager) {
    return new CategoriesViewModel(preferencesManager);
  }
}
