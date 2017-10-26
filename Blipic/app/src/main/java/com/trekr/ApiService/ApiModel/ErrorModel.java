package com.trekr.ApiService.ApiModel;

public class ErrorModel {
    public static class Error {
        public int status;
        public String message;
        public String code;
    }

    public static class DataError {
        public Error error;
    }
    public DataError data;
}