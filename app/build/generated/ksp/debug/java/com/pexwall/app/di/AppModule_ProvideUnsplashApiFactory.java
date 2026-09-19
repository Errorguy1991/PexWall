package com.pexwall.app.di;

import com.pexwall.app.data.api.UnsplashApi;
import com.squareup.moshi.Moshi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideUnsplashApiFactory implements Factory<UnsplashApi> {
  private final Provider<Moshi> moshiProvider;

  public AppModule_ProvideUnsplashApiFactory(Provider<Moshi> moshiProvider) {
    this.moshiProvider = moshiProvider;
  }

  @Override
  public UnsplashApi get() {
    return provideUnsplashApi(moshiProvider.get());
  }

  public static AppModule_ProvideUnsplashApiFactory create(Provider<Moshi> moshiProvider) {
    return new AppModule_ProvideUnsplashApiFactory(moshiProvider);
  }

  public static UnsplashApi provideUnsplashApi(Moshi moshi) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideUnsplashApi(moshi));
  }
}
