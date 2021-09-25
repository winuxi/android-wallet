package org.telegram.crypto.currency_ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import org.telegram.crypto.Tools.RequestMan;
import org.telegram.crypto.models.Currency;
import org.telegram.crypto.repository.CurrencyRepo;

import java.util.List;

public class CurrencyViewModel extends AndroidViewModel {

    public MutableLiveData<Currency> blogs;
    private final CurrencyRepo currencyRepo;
    public Application application;
    public CurrencyViewModel(@NonNull Application application) {
        super(application);
            currencyRepo = new CurrencyRepo(application, RequestMan.DATABASE);
        blogs = new MutableLiveData<>();
        this.application = application;
    }

    public LiveData<List<Currency>> getCurrencies() {
        return currencyRepo.load_currencies();
    }

}