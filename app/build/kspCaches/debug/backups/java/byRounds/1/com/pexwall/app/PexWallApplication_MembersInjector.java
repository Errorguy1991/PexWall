package com.pexwall.app;

import androidx.hilt.work.HiltWorkerFactory;
import com.pexwall.app.data.preferences.PreferencesManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class PexWallApplication_MembersInjector implements MembersInjector<PexWallApplication> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public PexWallApplication_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  public static MembersInjector<PexWallApplication> create(
      Provider<HiltWorkerFactory> workerFactoryProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new PexWallApplication_MembersInjector(workerFactoryProvider, preferencesManagerProvider);
  }

  @Override
  public void injectMembers(PexWallApplication instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
    injectPreferencesManager(instance, preferencesManagerProvider.get());
  }

  @InjectedFieldSignature("com.pexwall.app.PexWallApplication.workerFactory")
  public static void injectWorkerFactory(PexWallApplication instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }

  @InjectedFieldSignature("com.pexwall.app.PexWallApplication.preferencesManager")
  public static void injectPreferencesManager(PexWallApplication instance,
      PreferencesManager preferencesManager) {
    instance.preferencesManager = preferencesManager;
  }
}
