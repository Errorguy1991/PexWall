package com.pexwall.app.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.pexwall.app.data.preferences.PreferencesManager;
import com.pexwall.app.data.repository.WallpaperRepository;
import com.pexwall.app.util.WallpaperSetter;
import dagger.internal.DaggerGenerated;
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
public final class WallpaperWorker_Factory {
  private final Provider<WallpaperRepository> repositoryProvider;

  private final Provider<WallpaperSetter> wallpaperSetterProvider;

  private final Provider<PreferencesManager> preferencesManagerProvider;

  public WallpaperWorker_Factory(Provider<WallpaperRepository> repositoryProvider,
      Provider<WallpaperSetter> wallpaperSetterProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.wallpaperSetterProvider = wallpaperSetterProvider;
    this.preferencesManagerProvider = preferencesManagerProvider;
  }

  public WallpaperWorker get(Context appContext, WorkerParameters workerParams) {
    return newInstance(appContext, workerParams, repositoryProvider.get(), wallpaperSetterProvider.get(), preferencesManagerProvider.get());
  }

  public static WallpaperWorker_Factory create(Provider<WallpaperRepository> repositoryProvider,
      Provider<WallpaperSetter> wallpaperSetterProvider,
      Provider<PreferencesManager> preferencesManagerProvider) {
    return new WallpaperWorker_Factory(repositoryProvider, wallpaperSetterProvider, preferencesManagerProvider);
  }

  public static WallpaperWorker newInstance(Context appContext, WorkerParameters workerParams,
      WallpaperRepository repository, WallpaperSetter wallpaperSetter,
      PreferencesManager preferencesManager) {
    return new WallpaperWorker(appContext, workerParams, repository, wallpaperSetter, preferencesManager);
  }
}
