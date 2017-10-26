package com.trekr.ApiService;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.trekr.ApiService.ApiModel.BLPBlip;
import com.trekr.ApiService.ApiModel.BlipModel;
import com.trekr.ApiService.ApiModel.ErrorModel;
import com.trekr.ApiService.ApiModel.GeoActivitiesModel;
import com.trekr.ApiService.ApiModel.UserModel;
import com.trekr.App;
import com.trekr.Utils.Utils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static com.trekr.Utils.Constants.API_BASE_URL;

public class ApiManager {
    private BlipicApi blipicApi, blipicApiAuth;
    private Retrofit retrofit, retrofitAuth;
    ArrayList<Call<Object>> aryCall = new ArrayList<>();

    public ApiManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        blipicApi = retrofit.create(BlipicApi.class);

        OkHttpClient builder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder().addHeader("Authorization", String.format("Bearer %s", App.getInstance().token)).build();
                        return chain.proceed(request);
                    }
                }).build();

        retrofitAuth = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(builder)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        blipicApiAuth = retrofitAuth.create(BlipicApi.class);
    }

    public void cancelRequests() {
        for (Call<Object> call : aryCall) {
            call.cancel();
        }
        aryCall.clear();
    }
    private <T> Call<Object> convertInstanceOfObject(Call<T> object) {
        return object.getClass().cast(object);
    }

    public void signUpUser(String first_name, String last_name, String email, String password, final Callback<Object> callback){
        Call<Object> call = blipicApi.signUpUser(String.format("%s %s", first_name, last_name), email, password);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(call, response);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        callback.onFailure(call, new Throwable(jObjError.getString("message")));
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                callback.onFailure(call, t);
            }
        });


    }
    public void resetPassword(String email, Callback<Object> callback){
        blipicApi.resetPassword(email).enqueue(callback);
    }

    public void loginUser(String email, String password, final Callback<UserModel> callback){
        if (!Utils.isBlipic()) {
            Call<UserModel> call = blipicApi.loginUser(email, password, true);
            aryCall.add(convertInstanceOfObject(call));

            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                    aryCall.remove(call);
                    if (response.isSuccessful()) {
                        callback.onResponse(call, response);
                    } else {
                        ErrorModel error = Utils.parseError(response, retrofit);
                        callback.onFailure(call, new Throwable(error.data.error.message));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                    aryCall.remove(call);
                    if (!call.isCanceled()) {
                        callback.onFailure(call, t);
                    }
                }
            });
        }
    }
    public void resendConfirmationCodeWithEmail(String email, Callback<Object> callback){
        blipicApi.resendConfirmationCodeWithEmail(email).enqueue(callback);
    }
    public void geofenceActivites(String fromDate, final Callback<GeoActivitiesModel> callback){
        if (!Utils.isBlipic()) {
            Call<GeoActivitiesModel> call = blipicApiAuth.geofenceActivites(fromDate);
            aryCall.add(convertInstanceOfObject(call));
            call.enqueue(new Callback<GeoActivitiesModel>() {
                @Override
                public void onResponse(@NonNull Call<GeoActivitiesModel> call, @NonNull Response<GeoActivitiesModel> response) {
                    aryCall.remove(call);
                    if (response.isSuccessful()) {
                        callback.onResponse(call, response);
                    } else {
                        ErrorModel error = Utils.parseError(response, retrofitAuth);
                        callback.onFailure(call, new Throwable(error.data.error.message));
                    }
                }
                @Override
                public void onFailure(@NonNull Call<GeoActivitiesModel> call, @NonNull Throwable t) {
                    aryCall.remove(call);
                    if (!call.isCanceled()) {
                        callback.onFailure(call, t);
                    }
                }
            });
        }
    }

    public void blipsNearbyWithLatitude(double latitude, double longitude, int offset, int limit, int radius, String text, String selectedDate, final Callback<BlipModel> callback){
        if (!Utils.isBlipic()) {
            Call<BlipModel> call = blipicApiAuth.blipsNearbyWithLatitude(offset, limit, radius, String.format("%g, %g", latitude, longitude), selectedDate, text);
            aryCall.add(convertInstanceOfObject(call));
            call.enqueue(new Callback<BlipModel>() {
                @Override
                public void onResponse(@NonNull Call<BlipModel> call, @NonNull Response<BlipModel> response) {
                    aryCall.remove(call);
                    if (response.isSuccessful()) {
                        response.body().data = loadBlipsRequest(response.body().data);
                        callback.onResponse(call, response);
                    } else {
                        ErrorModel error = Utils.parseError(response, retrofitAuth);
                        callback.onFailure(call, new Throwable(error.data.error.message));
                    }
                }
                @Override
                public void onFailure(@NonNull Call<BlipModel> call, @NonNull Throwable t) {
                    aryCall.remove(call);
                    if (!call.isCanceled()) {
                        callback.onFailure(call, t);
                    }
                }
            });
        }
    }
    public ArrayList<BLPBlip> loadBlipsRequest(List<BLPBlip> blipsJSONArray) {
        String blipKey = "blip";
        String idKey = "id";

        ArrayList<BLPBlip> arrayBlips = new ArrayList<>();

        for (BLPBlip recievedBlip : blipsJSONArray) {
            if (recievedBlip != null) {
                arrayBlips.add(recievedBlip);
            }
        }

        for (int i = 0; i < arrayBlips.size(); i++) {
            BLPBlip blip = arrayBlips.get(i);

            double latSelcBlip = blip.getCoordinate().latitude;
            double lonSelcBlip = blip.getCoordinate().longitude;

            ArrayList<Map<String, Object>> arrayNearedBlips = new ArrayList<>();

            for (int j = i + 1; j < arrayBlips.size(); j++) {
                if (j != i) {
                    BLPBlip nearedBlip = arrayBlips.get(j);

                    double latBlip = nearedBlip.getCoordinate().latitude;
                    double lonBlip = nearedBlip.getCoordinate().longitude;

                    double vectorX = latSelcBlip - latBlip;
                    double vectorY = lonSelcBlip - lonBlip;

                    double distance = Math.sqrt(vectorX * vectorX + vectorY * vectorY);

                    if (distance < 0.0003) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put(blipKey, nearedBlip);
                        map.put(idKey, j);

                        arrayNearedBlips.add(map);
                    }
                }
            }

            if (!arrayBlips.contains(blip)) {
                arrayBlips.add(blip);
            }

            for (int k = 0; k < arrayNearedBlips.size(); k++) {
                double lattitude = latSelcBlip;
                double longitude = lonSelcBlip;

                double additionalValue = 0.00031;

                switch (k) {
                    case 0:
                        lattitude += additionalValue;
                        break;

                    case 1:
                        lattitude += additionalValue;
                        longitude += additionalValue;
                        break;

                    case 2:
                        longitude += additionalValue;
                        break;

                    case 3:
                        lattitude -= additionalValue;
                        longitude += additionalValue;
                        break;

                    case 4:
                        lattitude -= additionalValue;
                        break;

                    case 5:
                        lattitude -= additionalValue;
                        longitude -= additionalValue;
                        break;

                    case 6:
                        longitude -= additionalValue;
                        break;

                    case 7:
                        lattitude += additionalValue;
                        longitude -= additionalValue;
                        break;

                    default:
                        lattitude += additionalValue;
                        longitude += additionalValue;
                        break;
                }

                Map<String, Object> currentDict = arrayNearedBlips.get(k);

                BLPBlip nearedBlip = (BLPBlip) currentDict.get(blipKey);
                nearedBlip.setCoordinate(new LatLng(lattitude, longitude));

                int index = (int) currentDict.get(idKey);

                arrayBlips.set(index, nearedBlip);
            }

            if (arrayNearedBlips.size() > 0) {
                //start loop from the beginning again to be sure there aren't covers anymore
                i = -1;
            }
        }

        return arrayBlips;
    }

    public void getMergeBlipWithPointId(String id, final Callback<BlipModel> callback){
        if (!Utils.isBlipic()) {
            Call<BlipModel> call = blipicApiAuth.getMergeBlipWithPointId(id);
            aryCall.add(convertInstanceOfObject(call));
            call.enqueue(new Callback<BlipModel>() {
                @Override
                public void onResponse(@NonNull Call<BlipModel> call, @NonNull Response<BlipModel> response) {
                    aryCall.remove(call);
                    if (response.isSuccessful()) {
                        callback.onResponse(call, response);
                    } else {
                        ErrorModel error = Utils.parseError(response, retrofitAuth);
                        callback.onFailure(call, new Throwable(error.data.error.message));
                    }
                }
                @Override
                public void onFailure(@NonNull Call<BlipModel> call, @NonNull Throwable t) {
                    aryCall.remove(call);
                    if (!call.isCanceled()) {
                        callback.onFailure(call, t);
                    }
                }
            });
        }
    }

    public void likeBlipWithId(String id, final Callback<Object> callback){
        if (!Utils.isBlipic()) {
            Call<Object> call = blipicApiAuth.likeBlipWithId(id);
            aryCall.add(convertInstanceOfObject(call));

            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    aryCall.remove(call);
                    if (response.isSuccessful()) {
                        callback.onResponse(call, response);
                    } else {
                        ErrorModel error = Utils.parseError(response, retrofit);
                        callback.onFailure(call, new Throwable(error.data.error.message));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    aryCall.remove(call);
                    if (!call.isCanceled()) {
                        callback.onFailure(call, t);
                    }
                }
            });
        }
    }

    public void dislikeBlipWithId(String id, final Callback<Object> callback){
        if (!Utils.isBlipic()) {
            Call<Object> call = blipicApiAuth.dislikeBlipWithId(id);
            aryCall.add(convertInstanceOfObject(call));

            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    aryCall.remove(call);
                    if (response.isSuccessful()) {
                        callback.onResponse(call, response);
                    } else {
                        ErrorModel error = Utils.parseError(response, retrofit);
                        callback.onFailure(call, new Throwable(error.data.error.message));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    aryCall.remove(call);
                    if (!call.isCanceled()) {
                        callback.onFailure(call, t);
                    }
                }
            });
        }
    }

    public void favoriteBlipWithId(String id, final Callback<Object> callback){
        if (!Utils.isBlipic()) {
            Call<Object> call = blipicApiAuth.favoriteBlipWithId(id);
            aryCall.add(convertInstanceOfObject(call));

            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    aryCall.remove(call);
                    if (response.isSuccessful()) {
                        callback.onResponse(call, response);
                    } else {
                        ErrorModel error = Utils.parseError(response, retrofit);
                        callback.onFailure(call, new Throwable(error.data.error.message));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    aryCall.remove(call);
                    if (!call.isCanceled()) {
                        callback.onFailure(call, t);
                    }
                }
            });
        }
    }

    public void removeFavoriteBlipWithId(String id, final Callback<Object> callback){
        if (!Utils.isBlipic()) {
            Call<Object> call = blipicApiAuth.removeFavoriteBlipWithId(id);
            aryCall.add(convertInstanceOfObject(call));

            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    aryCall.remove(call);
                    if (response.isSuccessful()) {
                        callback.onResponse(call, response);
                    } else {
                        ErrorModel error = Utils.parseError(response, retrofit);
                        callback.onFailure(call, new Throwable(error.data.error.message));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    aryCall.remove(call);
                    if (!call.isCanceled()) {
                        callback.onFailure(call, t);
                    }
                }
            });
        }
    }
}
