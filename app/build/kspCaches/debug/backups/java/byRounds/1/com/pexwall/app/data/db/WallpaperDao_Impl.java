package com.pexwall.app.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class WallpaperDao_Impl implements WallpaperDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WallpaperEntity> __insertionAdapterOfWallpaperEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOlderThan;

  public WallpaperDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWallpaperEntity = new EntityInsertionAdapter<WallpaperEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `wallpaper_history` (`id`,`pexelsPhotoId`,`imageUrl`,`thumbnailUrl`,`photographer`,`photographerUrl`,`category`,`dateSet`,`avgColor`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WallpaperEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getPexelsPhotoId());
        statement.bindString(3, entity.getImageUrl());
        statement.bindString(4, entity.getThumbnailUrl());
        statement.bindString(5, entity.getPhotographer());
        statement.bindString(6, entity.getPhotographerUrl());
        statement.bindString(7, entity.getCategory());
        statement.bindLong(8, entity.getDateSet());
        if (entity.getAvgColor() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getAvgColor());
        }
      }
    };
    this.__preparedStmtOfDeleteOlderThan = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM wallpaper_history WHERE dateSet < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertWallpaper(final WallpaperEntity wallpaper,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfWallpaperEntity.insert(wallpaper);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOlderThan(final long cutoffTime,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOlderThan.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cutoffTime);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOlderThan.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<WallpaperEntity>> getAllHistory() {
    final String _sql = "SELECT * FROM wallpaper_history ORDER BY dateSet DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"wallpaper_history"}, new Callable<List<WallpaperEntity>>() {
      @Override
      @NonNull
      public List<WallpaperEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPexelsPhotoId = CursorUtil.getColumnIndexOrThrow(_cursor, "pexelsPhotoId");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailUrl");
          final int _cursorIndexOfPhotographer = CursorUtil.getColumnIndexOrThrow(_cursor, "photographer");
          final int _cursorIndexOfPhotographerUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "photographerUrl");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfDateSet = CursorUtil.getColumnIndexOrThrow(_cursor, "dateSet");
          final int _cursorIndexOfAvgColor = CursorUtil.getColumnIndexOrThrow(_cursor, "avgColor");
          final List<WallpaperEntity> _result = new ArrayList<WallpaperEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WallpaperEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpPexelsPhotoId;
            _tmpPexelsPhotoId = _cursor.getInt(_cursorIndexOfPexelsPhotoId);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpThumbnailUrl;
            _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            final String _tmpPhotographer;
            _tmpPhotographer = _cursor.getString(_cursorIndexOfPhotographer);
            final String _tmpPhotographerUrl;
            _tmpPhotographerUrl = _cursor.getString(_cursorIndexOfPhotographerUrl);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final long _tmpDateSet;
            _tmpDateSet = _cursor.getLong(_cursorIndexOfDateSet);
            final String _tmpAvgColor;
            if (_cursor.isNull(_cursorIndexOfAvgColor)) {
              _tmpAvgColor = null;
            } else {
              _tmpAvgColor = _cursor.getString(_cursorIndexOfAvgColor);
            }
            _item = new WallpaperEntity(_tmpId,_tmpPexelsPhotoId,_tmpImageUrl,_tmpThumbnailUrl,_tmpPhotographer,_tmpPhotographerUrl,_tmpCategory,_tmpDateSet,_tmpAvgColor);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<WallpaperEntity>> getHistoryByCategory(final String category) {
    final String _sql = "SELECT * FROM wallpaper_history WHERE category = ? ORDER BY dateSet DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, category);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"wallpaper_history"}, new Callable<List<WallpaperEntity>>() {
      @Override
      @NonNull
      public List<WallpaperEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPexelsPhotoId = CursorUtil.getColumnIndexOrThrow(_cursor, "pexelsPhotoId");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailUrl");
          final int _cursorIndexOfPhotographer = CursorUtil.getColumnIndexOrThrow(_cursor, "photographer");
          final int _cursorIndexOfPhotographerUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "photographerUrl");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfDateSet = CursorUtil.getColumnIndexOrThrow(_cursor, "dateSet");
          final int _cursorIndexOfAvgColor = CursorUtil.getColumnIndexOrThrow(_cursor, "avgColor");
          final List<WallpaperEntity> _result = new ArrayList<WallpaperEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WallpaperEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpPexelsPhotoId;
            _tmpPexelsPhotoId = _cursor.getInt(_cursorIndexOfPexelsPhotoId);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpThumbnailUrl;
            _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            final String _tmpPhotographer;
            _tmpPhotographer = _cursor.getString(_cursorIndexOfPhotographer);
            final String _tmpPhotographerUrl;
            _tmpPhotographerUrl = _cursor.getString(_cursorIndexOfPhotographerUrl);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final long _tmpDateSet;
            _tmpDateSet = _cursor.getLong(_cursorIndexOfDateSet);
            final String _tmpAvgColor;
            if (_cursor.isNull(_cursorIndexOfAvgColor)) {
              _tmpAvgColor = null;
            } else {
              _tmpAvgColor = _cursor.getString(_cursorIndexOfAvgColor);
            }
            _item = new WallpaperEntity(_tmpId,_tmpPexelsPhotoId,_tmpImageUrl,_tmpThumbnailUrl,_tmpPhotographer,_tmpPhotographerUrl,_tmpCategory,_tmpDateSet,_tmpAvgColor);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getLatestWallpaper(final Continuation<? super WallpaperEntity> $completion) {
    final String _sql = "SELECT * FROM wallpaper_history ORDER BY dateSet DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<WallpaperEntity>() {
      @Override
      @Nullable
      public WallpaperEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPexelsPhotoId = CursorUtil.getColumnIndexOrThrow(_cursor, "pexelsPhotoId");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailUrl");
          final int _cursorIndexOfPhotographer = CursorUtil.getColumnIndexOrThrow(_cursor, "photographer");
          final int _cursorIndexOfPhotographerUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "photographerUrl");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfDateSet = CursorUtil.getColumnIndexOrThrow(_cursor, "dateSet");
          final int _cursorIndexOfAvgColor = CursorUtil.getColumnIndexOrThrow(_cursor, "avgColor");
          final WallpaperEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpPexelsPhotoId;
            _tmpPexelsPhotoId = _cursor.getInt(_cursorIndexOfPexelsPhotoId);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpThumbnailUrl;
            _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            final String _tmpPhotographer;
            _tmpPhotographer = _cursor.getString(_cursorIndexOfPhotographer);
            final String _tmpPhotographerUrl;
            _tmpPhotographerUrl = _cursor.getString(_cursorIndexOfPhotographerUrl);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final long _tmpDateSet;
            _tmpDateSet = _cursor.getLong(_cursorIndexOfDateSet);
            final String _tmpAvgColor;
            if (_cursor.isNull(_cursorIndexOfAvgColor)) {
              _tmpAvgColor = null;
            } else {
              _tmpAvgColor = _cursor.getString(_cursorIndexOfAvgColor);
            }
            _result = new WallpaperEntity(_tmpId,_tmpPexelsPhotoId,_tmpImageUrl,_tmpThumbnailUrl,_tmpPhotographer,_tmpPhotographerUrl,_tmpCategory,_tmpDateSet,_tmpAvgColor);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<WallpaperEntity> getLatestWallpaperFlow() {
    final String _sql = "SELECT * FROM wallpaper_history ORDER BY dateSet DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"wallpaper_history"}, new Callable<WallpaperEntity>() {
      @Override
      @Nullable
      public WallpaperEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPexelsPhotoId = CursorUtil.getColumnIndexOrThrow(_cursor, "pexelsPhotoId");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfThumbnailUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnailUrl");
          final int _cursorIndexOfPhotographer = CursorUtil.getColumnIndexOrThrow(_cursor, "photographer");
          final int _cursorIndexOfPhotographerUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "photographerUrl");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfDateSet = CursorUtil.getColumnIndexOrThrow(_cursor, "dateSet");
          final int _cursorIndexOfAvgColor = CursorUtil.getColumnIndexOrThrow(_cursor, "avgColor");
          final WallpaperEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final int _tmpPexelsPhotoId;
            _tmpPexelsPhotoId = _cursor.getInt(_cursorIndexOfPexelsPhotoId);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpThumbnailUrl;
            _tmpThumbnailUrl = _cursor.getString(_cursorIndexOfThumbnailUrl);
            final String _tmpPhotographer;
            _tmpPhotographer = _cursor.getString(_cursorIndexOfPhotographer);
            final String _tmpPhotographerUrl;
            _tmpPhotographerUrl = _cursor.getString(_cursorIndexOfPhotographerUrl);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final long _tmpDateSet;
            _tmpDateSet = _cursor.getLong(_cursorIndexOfDateSet);
            final String _tmpAvgColor;
            if (_cursor.isNull(_cursorIndexOfAvgColor)) {
              _tmpAvgColor = null;
            } else {
              _tmpAvgColor = _cursor.getString(_cursorIndexOfAvgColor);
            }
            _result = new WallpaperEntity(_tmpId,_tmpPexelsPhotoId,_tmpImageUrl,_tmpThumbnailUrl,_tmpPhotographer,_tmpPhotographerUrl,_tmpCategory,_tmpDateSet,_tmpAvgColor);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllUsedPhotoIds(final Continuation<? super List<Integer>> $completion) {
    final String _sql = "SELECT pexelsPhotoId FROM wallpaper_history";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Integer>>() {
      @Override
      @NonNull
      public List<Integer> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<Integer> _result = new ArrayList<Integer>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Integer _item;
            _item = _cursor.getInt(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object isPhotoUsed(final int photoId, final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM wallpaper_history WHERE pexelsPhotoId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, photoId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
