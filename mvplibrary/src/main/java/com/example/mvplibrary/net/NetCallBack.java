package com.example.mvplibrary.net;

import android.util.Log;

import com.example.mvplibrary.base.BaseResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 网络请求回调
 */
public abstract class NetCallBack<T> implements Callback<T> {
    public static final String TAG = "CallBackFromMvp";
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response != null && response.body() != null && response.isSuccessful()){
            BaseResponse baseResponse = new Gson().fromJson(new Gson().toJson(response.body()),BaseResponse.class);
            if (baseResponse.getCode() == 404){
                Log.e(TAG, baseResponse.getData().toString());
            } else if (baseResponse.getCode() == 500){
                Log.e(TAG, baseResponse.getData().toString());
            } else {
                onSuccess(call,response);
            }
        }else {
            onFailed();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailed();
    }
    public abstract void onSuccess(Call<T> call,Response<T> response);

    public abstract void onFailed();
}
