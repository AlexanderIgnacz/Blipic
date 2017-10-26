package com.trekr.Common.Activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator;
import com.theartofdev.edmodo.cropper.CropImage;
import com.trekr.App;
import com.trekr.Blipic.Controllers.HomeMap.Components.CategoryView;
import com.trekr.Blipic.Controllers.HomeMap.Components.SubCategoryView;
import com.trekr.Blipic.Models.BLPCategory;
import com.trekr.Blipic.Models.BLPSubCategory;
import com.trekr.R;
import com.trekr.Utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.trekr.Utils.Constants.kBLPSubCategoryDuration;
import static java.security.AccessController.getContext;

public class SelectTypeActivity extends AppCompatActivity implements View.OnClickListener, CategoryView.OnOutClicked, CategoryView.OnCategoryItemClicked, SubCategoryView.OnSubCategoryItemClicked, ActionSheet.ActionSheetListener {

    private BLPCategory selectedCategory;
    SubCategoryView subCategoriesView;
    CategoryView select_type_category;
    ImageView select_type_back;
    View select_type_camera;

    private int screenWidth, screenHeight;
    private int selectState = 0;
    private boolean isAnimating = false;

    float originX;
    float originY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type);

        screenWidth = App.getInstance().screenWidth;
        screenHeight = App.getInstance().screenHeight;

        init();
    }

    public void init() {
        select_type_category = findViewById(R.id.select_type_category);
        select_type_category.unsetDanger();

        ViewGroup.LayoutParams lp = select_type_category.getLayoutParams();
        lp.width = screenWidth - 80;
        lp.height = screenWidth - 80;
        select_type_category.setLayoutParams(lp);
        select_type_category.setOnItemClickedListener(this);
        select_type_category.setOnOutClickedListener(this);

        subCategoriesView = findViewById(R.id.subCategoriesView1);
        subCategoriesView.setLayoutParams(lp);
        subCategoriesView.setVisibility(View.INVISIBLE);

        setSelectState(0);
        isAnimating = false;

        originX = (screenWidth - subCategoriesView.getLayoutParams().width) / 2;
        originY = (screenHeight - subCategoriesView.getLayoutParams().height) / 2;
        select_type_back = findViewById(R.id.select_type_back);

        select_type_camera = findViewById(R.id.select_type_camera);
        select_type_camera.setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (getSelectState() > 0) {
            setSelectState(0);
            animateWheelsWithCategory_reverse();
        } else {
            finish();
            overridePendingTransition(R.anim.transition_from_left, R.anim.transition_to_right);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.select_type_camera:
                ActionSheet.createBuilder(this, getSupportFragmentManager())
                        .setCancelButtonTitle("Cancel")
                        .setOtherButtonTitles("Take Photo or Video", "Choose Existing Photo or Video")
                        .setCancelableOnTouchOutside(true)
                        .setListener(this).show();
                break;
        }
    }

    @Override
    public void onOutClicked() {

    }

    @Override
    public void onCategoryItemClicked(Object object) {
        if (isAnimating) {
            return;
        }
        selectedCategory = App.getInstance().dictCategories.get(object.toString());
        initSubCategoriesWithCategory();
        animateWheelsWithCategory(Integer.parseInt(selectedCategory.id));
        setSelectState(1);
    }

    public void setSelectState(int state) {
        this.selectState = state;
    }

    public int getSelectState() {
        return this.selectState;
    }

    void initSubCategoriesWithCategory() {
        subCategoriesView.setVisibility(View.VISIBLE);
        subCategoriesView.setOnOutClickedListener(this);
        subCategoriesView.setOnItemClickedListener(this);

        int subCatBtnID[] = {R.id.firstCategory, R.id.secondCategory, R.id.thirdCategory, R.id.fourthCategory, R.id.fifthCategory,
                R.id.firstCategorySecond, R.id.secondCategorySecond, R.id.thirdCategorySecond, R.id.fourthCategorySecond, R.id.fifthCategorySecond};

        for (int i = 0; i < subCatBtnID.length; i++) {
            BLPSubCategory subCategory = selectedCategory.dictSubCategories.get(String.format("key-%d", i));
            ImageView imgView = findViewById(subCatBtnID[i]);

            imgView.setImageDrawable(subCategory.drawableStates);
            imgView.setTag(i);
        }
    }

    public void animateWheelsWithCategory(int categoryId) {

        float flyX = 0;
        float flyY = 0;

        float categoryflyX = 0;
        float categoryflyY = 0;

        switch (categoryId - 1) {
            case 0:
                flyX = -screenWidth;
                flyY = originY-select_type_category.getHeight();
                categoryflyY = originY+select_type_category.getHeight();
                select_type_back.setImageResource(R.drawable.wall_mountain_x2);
                break;
            case 1:
                flyX = screenWidth;
                flyY = originY-select_type_category.getHeight();
                categoryflyY = originY+select_type_category.getHeight();
                select_type_back.setImageResource(R.drawable.wall_snow_x2);
                break;
            case 2:
                flyX = screenWidth;
                flyY = originY+select_type_category.getHeight();
                categoryflyY = originY-select_type_category.getHeight();
                select_type_back.setImageResource(R.drawable.wall_wheel_x2);
                break;
            case 3:
                flyX = -screenWidth;
                flyY = originY+select_type_category.getHeight();
                categoryflyY = originY-select_type_category.getHeight();
                select_type_back.setImageResource(R.drawable.wall_water_x2);
                break;
            default:
                break;
        }
        select_type_back.setVisibility(View.VISIBLE);
        select_type_back.setAlpha((float) 0.0);
        categoryflyX = -flyX;

        subCategoriesView.setX(flyX);
        subCategoriesView.setTranslationY(flyY);

        subCategoriesView.setVisibility(View.VISIBLE);

        List<Animator> animatorList = new ArrayList<>();

        animatorList.add(ViewPropertyObjectAnimator
                .animate(subCategoriesView)
                .withLayer()
                .x(originX)
                .y(originY)
                .setDuration(kBLPSubCategoryDuration)
                .get());

        animatorList.add(ViewPropertyObjectAnimator
                .animate(select_type_category)
                .withLayer()
                .x(categoryflyX)
                .y(categoryflyY)
                .setDuration(kBLPSubCategoryDuration)
                .get());

        animatorList.add(ViewPropertyObjectAnimator
                .animate(select_type_back)
                .withLayer()
                .alpha((float) 1.0)
                .setDuration(kBLPSubCategoryDuration)
                .get());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new OvershootInterpolator(2.5f));
        animatorSet.playTogether(animatorList);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                setUserTouchEnable(false);
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setUserTouchEnable(true);
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animatorSet.start();
    }

    public void animateWheelsWithCategory_reverse() {

        float flyX = -select_type_category.getX();
        float flyY = -(select_type_category.getY() - originY) + originY;

        List<Animator> animatorList = new ArrayList<>();

        animatorList.add(ViewPropertyObjectAnimator
                .animate(subCategoriesView)
                .withLayer()
                .x(flyX)
                .y(flyY)
                .setDuration(kBLPSubCategoryDuration)
                .get());

        animatorList.add(ViewPropertyObjectAnimator
                .animate(select_type_category)
                .withLayer()
                .x(originX)
                .y(originY)
                .setDuration(kBLPSubCategoryDuration)
                .get());

        animatorList.add(ViewPropertyObjectAnimator
                .animate(select_type_back)
                .withLayer()
                .alpha((float) 0.0)
                .setDuration(kBLPSubCategoryDuration)
                .get());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new OvershootInterpolator(2.5f));
        animatorSet.playTogether(animatorList);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                setUserTouchEnable(false);
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setUserTouchEnable(true);
                isAnimating = false;
                subCategoriesView.setVisibility(View.INVISIBLE);
                subCategoriesView.setX(originX);
                subCategoriesView.setY(originY);
                select_type_back.setVisibility(View.GONE);
                select_type_back.setAlpha((float) 0.0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animatorSet.start();
    }

    @Override
    public void OnSubCategoryItemClicked(Object object) {
        BLPSubCategory subCategory = selectedCategory.dictSubCategories.get(String.format("key-%d", (int) object));
        if (subCategory.name.equals("empty")) {
            return;
        }
        App.getInstance().selectActivityType(subCategory.name);
        finish();
        overridePendingTransition(R.anim.transition_from_left, R.anim.transition_to_right);
    }

    public void setUserTouchEnable(boolean isSet) {
        if (isSet) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        switch(index) {
            case 0:
                CropImage.activity().start(this);
                break;
            case 1:
                break;
        }
    }
}
