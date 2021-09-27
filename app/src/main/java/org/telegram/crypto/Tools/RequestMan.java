package org.telegram.crypto.Tools;

import android.content.Context;

import org.telegram.crypto.datasources.ApiProvider;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestMan {
    public static String TOKEN = "b57fec18-3560-4189-8efb-a10363008f9d";
    public static int LIMIT = 10;
    public static int START = 1;
    public static String CONVERT = "USD";
    public static String DATABASE = "CurrencyDB";
    public static String icon_url = "https://s2.coinmarketcap.com/static/img/coins/64x64/";
    public static ApiProvider service(Context context){
        String remote_url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/";
        Retrofit retrofit =  new Retrofit.Builder()
                .baseUrl(remote_url)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiProvider.class);
    }
    //client
    public static OkHttpClient getClient(Context context) {
        String token = "";
        return new OkHttpClient.Builder().readTimeout(9, TimeUnit.SECONDS)
                .connectTimeout(9, TimeUnit.SECONDS).
                        addInterceptor(chain -> {
                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("Authorization", " Bearer " + token)
                                    .build();
                            return chain.proceed(newRequest);
                        }).build();
    }

}
