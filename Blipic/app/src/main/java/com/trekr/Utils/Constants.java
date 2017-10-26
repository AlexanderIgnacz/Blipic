package com.trekr.Utils;

import com.trekr.BuildConfig;

public class Constants {
    public static final String APP_NAME = BuildConfig.FLAVOR;
    public static final String CURRENT_USER = "current_user";
    public static final String TOKEN = "token";

    public static final String API_BASE_URL = "http://dev.blipic.co/v1/";

    public static final long CONS_IOS = 1000;
    public static final long kBLPAnimationDuration = (long)(0.3f * CONS_IOS);
    public static final long kBLPCategoryDuration = (long)(0.5f * CONS_IOS);
    public static final long kBLPSubCategoryDuration = (long)(0.3f * CONS_IOS);
    public static final long kBLPTextLocationDuration = (long)(0.4f * CONS_IOS);

    public static final float kBLPLoginSectionAnimationPosition = 40.0f;
    public static final float kBLPPassRecoverySectionAnimationPosition = 40.0f;

    public static final int kAlphaOn = 1;
    public static final int kAlphaOff = 0;
}
