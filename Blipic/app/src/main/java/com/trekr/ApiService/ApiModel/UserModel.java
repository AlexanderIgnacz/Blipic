package com.trekr.ApiService.ApiModel;

public class UserModel {
    public static class DataUser {
        public BLPUser user;
        public String token;
    }

    public DataUser data;

    public static class BLPImage extends BLPMediaContent {}
    public static class BLPVideo extends BLPMediaContent {}

}
