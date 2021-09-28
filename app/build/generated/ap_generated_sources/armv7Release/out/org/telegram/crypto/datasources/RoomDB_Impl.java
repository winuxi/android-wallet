package org.telegram.crypto.datasources;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public final class RoomDB_Impl extends RoomDB {
  private volatile DoaOperation _doaOperation;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `currency` (`id` INTEGER NOT NULL, `rank` INTEGER NOT NULL, `icon` TEXT, `name` TEXT, `symbol` TEXT, `slug` TEXT, `price` TEXT, `today` TEXT, `week` TEXT, `marketCap` TEXT, `volume` TEXT, `circulating_supply` TEXT, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"f8748f4bda5f0e46ed3a1ecf8bdfb13a\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `currency`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsCurrency = new HashMap<String, TableInfo.Column>(12);
        _columnsCurrency.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsCurrency.put("rank", new TableInfo.Column("rank", "INTEGER", true, 0));
        _columnsCurrency.put("icon", new TableInfo.Column("icon", "TEXT", false, 0));
        _columnsCurrency.put("name", new TableInfo.Column("name", "TEXT", false, 0));
        _columnsCurrency.put("symbol", new TableInfo.Column("symbol", "TEXT", false, 0));
        _columnsCurrency.put("slug", new TableInfo.Column("slug", "TEXT", false, 0));
        _columnsCurrency.put("price", new TableInfo.Column("price", "TEXT", false, 0));
        _columnsCurrency.put("today", new TableInfo.Column("today", "TEXT", false, 0));
        _columnsCurrency.put("week", new TableInfo.Column("week", "TEXT", false, 0));
        _columnsCurrency.put("marketCap", new TableInfo.Column("marketCap", "TEXT", false, 0));
        _columnsCurrency.put("volume", new TableInfo.Column("volume", "TEXT", false, 0));
        _columnsCurrency.put("circulating_supply", new TableInfo.Column("circulating_supply", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCurrency = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCurrency = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCurrency = new TableInfo("currency", _columnsCurrency, _foreignKeysCurrency, _indicesCurrency);
        final TableInfo _existingCurrency = TableInfo.read(_db, "currency");
        if (! _infoCurrency.equals(_existingCurrency)) {
          throw new IllegalStateException("Migration didn't properly handle currency(org.telegram.crypto.models.Currency).\n"
                  + " Expected:\n" + _infoCurrency + "\n"
                  + " Found:\n" + _existingCurrency);
        }
      }
    }, "f8748f4bda5f0e46ed3a1ecf8bdfb13a", "585423f38897043dc8f7e22c6280108f");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "currency");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `currency`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public DoaOperation zoeDaos() {
    if (_doaOperation != null) {
      return _doaOperation;
    } else {
      synchronized(this) {
        if(_doaOperation == null) {
          _doaOperation = new DoaOperation_Impl(this);
        }
        return _doaOperation;
      }
    }
  }
}
