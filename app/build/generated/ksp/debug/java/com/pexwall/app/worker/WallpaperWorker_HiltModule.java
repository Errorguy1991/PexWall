package com.pexwall.app.worker;

import androidx.hilt.work.WorkerAssistedFactory;
import androidx.work.ListenableWorker;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import javax.annotation.processing.Generated;

@Generated("androidx.hilt.AndroidXHiltProcessor")
@Module
@InstallIn(SingletonComponent.class)
@OriginatingElement(
    topLevelClass = WallpaperWorker.class
)
public interface WallpaperWorker_HiltModule {
  @Binds
  @IntoMap
  @StringKey("com.pexwall.app.worker.WallpaperWorker")
  WorkerAssistedFactory<? extends ListenableWorker> bind(WallpaperWorker_AssistedFactory factory);
}
