package org.telegram.crypto.datasources;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import org.telegram.crypto.models.Currency;
import org.telegram.crypto.models.Data;

import java.util.List;


@Dao
public interface DoaOperation {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update_currencies(List<Currency> messages);

    @Query("SELECT * FROM currency order by id asc")
    LiveData<List<Currency>> load_currencies();

}

