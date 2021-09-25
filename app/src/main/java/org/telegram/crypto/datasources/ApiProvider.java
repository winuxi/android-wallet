package org.telegram.crypto.datasources;

import org.telegram.crypto.models.CurrencyResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiProvider {
    @GET("listings/latest")
    Call<CurrencyResponse> load_currency(
            @Query("start") int start,
            @Query("limit") int limit,
            @Query("convert") String convert,
            @Query("CMC_PRO_API_KEY") String CMC_PRO_API_KEY);

}
