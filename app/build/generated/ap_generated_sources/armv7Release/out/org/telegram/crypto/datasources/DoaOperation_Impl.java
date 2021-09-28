package org.telegram.crypto.datasources;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.lifecycle.ComputableLiveData;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.InvalidationTracker.Observer;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.telegram.crypto.models.Currency;

@SuppressWarnings("unchecked")
public final class DoaOperation_Impl implements DoaOperation {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfCurrency;

  public DoaOperation_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCurrency = new EntityInsertionAdapter<Currency>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `currency`(`id`,`rank`,`icon`,`name`,`symbol`,`slug`,`price`,`today`,`week`,`marketCap`,`volume`,`circulating_supply`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Currency value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getRank());
        if (value.getIcon() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getIcon());
        }
        if (value.getName() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getName());
        }
        if (value.getSymbol() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getSymbol());
        }
        if (value.getSlug() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getSlug());
        }
        if (value.getPrice() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getPrice());
        }
        if (value.getToday() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getToday());
        }
        if (value.getWeek() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getWeek());
        }
        if (value.getMarketCap() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getMarketCap());
        }
        if (value.getVolume() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getVolume());
        }
        if (value.getCirculating_supply() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindString(12, value.getCirculating_supply());
        }
      }
    };
  }

  @Override
  public void update_currencies(List<Currency> messages) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfCurrency.insert(messages);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<Currency>> load_currencies() {
    final String _sql = "SELECT * FROM currency order by id asc";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return new ComputableLiveData<List<Currency>>(__db.getQueryExecutor()) {
      private Observer _observer;

      @Override
      protected List<Currency> compute() {
        if (_observer == null) {
          _observer = new Observer("currency") {
            @Override
            public void onInvalidated(@NonNull Set<String> tables) {
              invalidate();
            }
          };
          __db.getInvalidationTracker().addWeakObserver(_observer);
        }
        final Cursor _cursor = __db.query(_statement);
        try {
          final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
          final int _cursorIndexOfRank = _cursor.getColumnIndexOrThrow("rank");
          final int _cursorIndexOfIcon = _cursor.getColumnIndexOrThrow("icon");
          final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
          final int _cursorIndexOfSymbol = _cursor.getColumnIndexOrThrow("symbol");
          final int _cursorIndexOfSlug = _cursor.getColumnIndexOrThrow("slug");
          final int _cursorIndexOfPrice = _cursor.getColumnIndexOrThrow("price");
          final int _cursorIndexOfToday = _cursor.getColumnIndexOrThrow("today");
          final int _cursorIndexOfWeek = _cursor.getColumnIndexOrThrow("week");
          final int _cursorIndexOfMarketCap = _cursor.getColumnIndexOrThrow("marketCap");
          final int _cursorIndexOfVolume = _cursor.getColumnIndexOrThrow("volume");
          final int _cursorIndexOfCirculatingSupply = _cursor.getColumnIndexOrThrow("circulating_supply");
          final List<Currency> _result = new ArrayList<Currency>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Currency _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpRank;
            _tmpRank = _cursor.getInt(_cursorIndexOfRank);
            final String _tmpIcon;
            _tmpIcon = _cursor.getString(_cursorIndexOfIcon);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSymbol;
            _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            final String _tmpSlug;
            _tmpSlug = _cursor.getString(_cursorIndexOfSlug);
            final String _tmpPrice;
            _tmpPrice = _cursor.getString(_cursorIndexOfPrice);
            final String _tmpToday;
            _tmpToday = _cursor.getString(_cursorIndexOfToday);
            final String _tmpWeek;
            _tmpWeek = _cursor.getString(_cursorIndexOfWeek);
            final String _tmpMarketCap;
            _tmpMarketCap = _cursor.getString(_cursorIndexOfMarketCap);
            final String _tmpVolume;
            _tmpVolume = _cursor.getString(_cursorIndexOfVolume);
            final String _tmpCirculating_supply;
            _tmpCirculating_supply = _cursor.getString(_cursorIndexOfCirculatingSupply);
            _item = new Currency(_tmpRank,_tmpId,_tmpIcon,_tmpName,_tmpSymbol,_tmpSlug,_tmpPrice,_tmpToday,_tmpWeek,_tmpMarketCap,_tmpVolume,_tmpCirculating_supply);
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
    }.getLiveData();
  }
}
