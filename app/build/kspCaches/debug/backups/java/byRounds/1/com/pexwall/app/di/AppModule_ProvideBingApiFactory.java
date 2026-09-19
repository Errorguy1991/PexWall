package com.pexwall.app.di;

import com.pexwall.app.data.api.BingApi;
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
public final class AppModule_ProvideBingApiFactory implements Factory<BingApi> {
  private final Provider<Moshi> moshiProvider;

  public AppModule_ProvideBingApiFactory(Provider<Moshi> moshiProvider) {
    this.moshiProvider = moshiProvider;
  }

  @Override
  public BingApi get() {
    return provideBingApi(moshiProvider.get());
  }

  public static AppModule_ProvideBingApiFactory create(Provider<Moshi> moshiProvider) {
    return new AppModule_ProvideBingApiFactory(moshiProvider);
  }

  public static BingApi provideBingApi(Moshi moshi) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideBingApi(moshi));
  }
}
