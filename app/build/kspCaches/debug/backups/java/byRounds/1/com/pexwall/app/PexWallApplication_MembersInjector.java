package com.pexwall.app;

import androidx.hilt.work.HiltWorkerFactory;
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

  public PexWallApplication_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<PexWallApplication> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new PexWallApplication_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(PexWallApplication instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.pexwall.app.PexWallApplication.workerFactory")
  public static void injectWorkerFactory(PexWallApplication instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
