package com.trekr.ApiService.ApiModel;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static com.trekr.ApiService.ApiModel.BLPUser.BLPUserGender.BLPUserGenderFemale;
import static com.trekr.ApiService.ApiModel.BLPUser.BLPUserGender.BLPUserGenderMale;

public class BLPUser {
    enum BLPUserGender{
        BLPUserGenderMale,
        BLPUserGenderFemale
    };

    public String _id;
    public String email;
    public String company;
    public String description;
    public String homeLocation;
    public String privacy;
    public String name;
    public Object coverPhoto;
    public UserModel.BLPImage profilePhoto;
    public boolean pending;
    public boolean isFirstTime;
    public Object gender;
    public List<String> viewedMarketingScreensIds;
    public List<String> photos;
    public List<UserModel.BLPVideo> videos;
    public List<BlipModel.BLPActivityType> favoriteActivityTypes;

    private BLPUserGender userGender;
    private UserModel.BLPImage _coverPhoto;

    public String getUserId() {
        return _id;
    }

    public BLPUserGender getUserGender() {
        if (userGender != null) {
            return userGender;
        }
        if (gender instanceof Boolean) {
            userGender = (boolean) gender ? BLPUserGenderFemale : BLPUserGenderMale;
        } else if (gender instanceof String){
            userGender = gender.equals("male") ? BLPUserGenderMale: BLPUserGenderFemale;
        } else if (gender != null) {
//            userGender = -1;
        }

        return userGender;
    }

    public UserModel.BLPImage getCoverPhoto() {
        if (_coverPhoto != null) {
            return _coverPhoto;
        }

        try {
            JSONObject jsonObject = new JSONObject((Map<Object, Object>)coverPhoto);
            String jsonString = jsonObject.toString();
            _coverPhoto = new Gson().fromJson(jsonString, UserModel.BLPImage.class);
        } catch (Exception e) {
        }
        return _coverPhoto;
    }

    public String getAccessRights() {
        return privacy;
    }

    public void updateWithUser(BLPUser user) {
        if (user != null) {
            _id = user.getUserId();
            name = TextUtils.isEmpty(user.name) ? name : user.name;
            email = user.email;
            description = user.description;
            homeLocation = user.homeLocation;

            isFirstTime = user.isFirstTime;

            photos                = user.photos;
            videos                = user.videos;
            favoriteActivityTypes = user.favoriteActivityTypes;

            profilePhoto = user.profilePhoto;
            coverPhoto   = user.coverPhoto;
        }
    }
}