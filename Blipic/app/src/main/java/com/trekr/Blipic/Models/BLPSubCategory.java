package com.trekr.Blipic.Models;

import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;

import com.trekr.App;

public class BLPSubCategory {
    public String subCategoryId;
    public String name;
    public String buttonImage;
    public StateListDrawable drawableStates;

    public BLPSubCategory (String subCategoryId, String name, String buttonImage) {
        this.subCategoryId = subCategoryId;
        this.name = name;
        this.buttonImage = buttonImage;

        Context context = App.getInstance().getApplicationContext();

        int resourceId = context.getResources().getIdentifier(buttonImage, "drawable",
                context.getPackageName());
        int resourcePressedId = context.getResources().getIdentifier(String.format("%s_press", buttonImage), "drawable",
                context.getPackageName());

        drawableStates = new StateListDrawable();
        drawableStates.addState(new int[]{android.R.attr.state_pressed},
                ContextCompat.getDrawable(context, resourcePressedId));
        drawableStates.addState(new int[]{},
                ContextCompat.getDrawable(context, resourceId));
    }
}
