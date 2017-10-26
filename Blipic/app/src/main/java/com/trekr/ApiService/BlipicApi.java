package com.trekr.ApiService;

import com.trekr.ApiService.ApiModel.BlipModel;
import com.trekr.ApiService.ApiModel.GeoActivitiesModel;
import com.trekr.ApiService.ApiModel.UserModel;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BlipicApi {
    @FormUrlEncoded
    @POST("auth/signup/jwt")
    Call<Object> signUpUser(@Field("name") String name, @Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/reset-password")
    Call<Object> resetPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("auth/email-confirmation/resend?email={email}")
    Call<Object> resendConfirmationCodeWithEmail(@Path("email") String email);

    @FormUrlEncoded
    @POST("auth/login/jwt")
    Call<UserModel> loginUser(@Field("email") String email, @Field("password") String password, @Field("isWellnessUser") boolean isWellnessUser);

    @GET("geofence-activities/stats")
    Call<GeoActivitiesModel> geofenceActivites(@Query("fromDate") String fromDate);

    @GET("points/search")
    Call<BlipModel> blipsNearbyWithLatitude(@Query("offset") int offset,
                                            @Query("limit") int limit,
                                            @Query("radius") int radius,
                                            @Query("center") String center,
                                            @Query("date") String date,
                                            @Query("text") String text);

    @GET("blips/point/{id}")
    Call<BlipModel> getMergeBlipWithPointId(@Path("id") String id);

    @POST("blips/{id}/like")
    Call<Object> likeBlipWithId(@Path("id") String id);

    @DELETE("blips/{id}/like")
    Call<Object> dislikeBlipWithId(@Path("id") String id);

    @POST("blips/{id}/favorite")
    Call<Object> favoriteBlipWithId(@Path("id") String id);

    @DELETE("blips/{id}/favorite")
    Call<Object> removeFavoriteBlipWithId(@Path("id") String id);
}
