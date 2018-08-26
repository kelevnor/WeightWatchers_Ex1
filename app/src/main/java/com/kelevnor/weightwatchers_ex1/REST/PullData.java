package com.kelevnor.weightwatchers_ex1.REST;

import android.app.Activity;

import com.kelevnor.weightwatchers_ex1.MODELS.Item;
import com.kelevnor.weightwatchers_ex1.UTILITY.Config;
import com.kelevnor.weightwatchers_ex1.UTILITY.UtilityHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by kelevnor on 8/25/18.
 */

public class PullData {

    OnAsyncResult onAsyncResult;

    public PullData(Activity act, final OnAsyncResult onAsyncResult){
        this.onAsyncResult = onAsyncResult;

        if(UtilityHelper.isNetworkAvailable(act)){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
            PullTestData testData = retrofit.create(PullTestData.class);

            Call<List<Item>> call= testData.getTestData();
            call.enqueue(new Callback<List<Item>>() {
                @Override
                public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                    List<Item> res = response.body();
                    onAsyncResult.onResultSuccess(Config.RESULT_PULLDATA_SUCCESS, res);

                }
                @Override
                public void onFailure(Call<List<Item>> call, Throwable t){
                    onAsyncResult.onResultFail(Config.RESULT_PULLDATA_FAIL, t.getMessage());
                }
            });
        }
        else{
            this.onAsyncResult.onResultFail(Config.RESULT_INTERNET_FAIL, "");
        }
    }

    //This is our Interface for the RetrofitCall
    public interface PullTestData{
        @GET("assets/cmx/us/messages/collections.json")
        Call<List<Item>> getTestData();
    }

    //This is our Interface for listener on Activity
    public interface OnAsyncResult {
        void onResultSuccess(int resultCode, List<Item> objList);
        void onResultFail(int resultCode, String errorMessage);
    }
}
