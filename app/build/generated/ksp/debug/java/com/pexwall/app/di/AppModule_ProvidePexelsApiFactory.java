package com.pexwall.app.di;

import com.pexwall.app.data.api.PexelsApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

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
public final class AppModule_ProvidePexelsApiFactory implements Factory<PexelsApi> {
  private final Provider<Retrofit> retrofitProvider;

  public AppModule_ProvidePexelsApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public PexelsApi get() {
    return providePexelsApi(retrofitProvider.get());
  }

  public static AppModule_ProvidePexelsApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new AppModule_ProvidePexelsApiFactory(retrofitProvider);
  }

  public static PexelsApi providePexelsApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providePexelsApi(retrofit));
  }
}
