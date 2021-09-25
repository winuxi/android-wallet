package org.telegram.crypto.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import org.telegram.crypto.Tools.RequestMan;
import org.telegram.crypto.datasources.DoaOperation;
import org.telegram.crypto.datasources.RoomDB;
import org.telegram.crypto.models.Currency;
import org.telegram.crypto.models.CurrencyResponse;
import org.telegram.crypto.models.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrencyRepo {
    private final DoaOperation doaOperation;
    private LiveData<List<CurrencyRepo>> currency;
    static public Application application;
    public boolean is_active = false;
    public CurrencyRepo(Application application, String user_spec) {
        RoomDB zoeLocDataB = RoomDB.getInstance(application, user_spec);
        doaOperation = zoeLocDataB.zoeDaos();
        CurrencyRepo.application = application;
    }


    public LiveData<List<Currency>> load_currencies() {
        sync_currencies();
        return doaOperation.load_currencies();
    }

    public void sync_currencies() {
        if (!is_active) {
            is_active = true;
                Call<CurrencyResponse> call = RequestMan.service(application)
                            .load_currency(
                                    RequestMan.START,
                                    RequestMan.LIMIT,
                                    RequestMan.CONVERT,
                                    RequestMan.TOKEN
                            );
                call.enqueue(new Callback<CurrencyResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CurrencyResponse> call,
                                           @NonNull Response<CurrencyResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d("repos","success: ");
                            int error_code = response.body().getStatus().getError_code();
                            if (error_code == 0) {
                                CurrencyResponse raw = response.body();
                                if(raw.getData().size() > 0){
                                    prepare_data(raw.getData());
                                }
                            }else {
                                Log.d("repos","error: "+error_code);
                            }
                        }else {
                            Log.d("repos","error: "+response.code());
                        }
                        is_active = false;
                    }
                    @Override
                    public void onFailure(@NonNull Call<CurrencyResponse> call, @NonNull Throwable t) {
                        is_active = false;
                        Log.d("repos","error: "+t.toString());
                    }
                });
            }
    }
    public void prepare_data(List<Data> data_list){
        List<Currency> currencies = new ArrayList<>();
        for (Data data:data_list){
            currencies.add(toLocalModel(data));
        }
        Log.d("repos","raw list size: "+data_list.size());
        Log.d("repos","converted list size: "+currencies.size());
        if(currencies.size() > 0){
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(() -> {
                //doaOperation.update_currencies(currencies);
                handler.post(() -> {
                    //UI Thread work here
                });
            });
        }
    }
    public Currency toLocalModel(Data data){
        return new Currency(
                data.getId(),data.getSymbol(),data.getName(),data.getSlug(),data.getQuote().getUsd().getPrice(),
                data.getQuote().getUsd().getPercent_change_24h(),data.getQuote().getUsd().getPercent_change_7d(),
                data.getQuote().getUsd().getMarket_cap(),data.getQuote().getUsd().getVolume_24h()
        );
    }

}
