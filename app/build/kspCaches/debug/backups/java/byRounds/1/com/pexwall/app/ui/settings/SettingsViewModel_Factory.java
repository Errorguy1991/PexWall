package com.pexwall.app.ui.settings;

import android.content.Context;
import com.pexwall.app.data.preferences.PreferencesManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<PreferencesManager> preferencesManagerProvider;

  private final Provider<Context> contextProvider;

  public SettingsViewModel_Factory(Provider<PreferencesManager> preferencesManagerProvider,
      Provider<Context> contextProvider) {
    this.preferencesManagerProvider = preferencesManagerProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(preferencesManagerProvider.get(), contextProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<PreferencesManager> preferencesManagerProvider, Provider<Context> contextProvider) {
    return new SettingsViewModel_Factory(preferencesManagerProvider, contextProvider);
  }

  public static SettingsViewModel newInstance(PreferencesManager preferencesManager,
      Context context) {
    return new SettingsViewModel(preferencesManager, context);
  }
}
