package com.trekr.Common.Activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator;
import com.trekr.App;
import com.trekr.R;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class NewCommentActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout notification_listview;
    private int screenWidth, screenHeight;
    private boolean isKeyboardPresent;
    private int keyboardHeight;
    Unregistrar unregistrar;
    TextView newcomment_post;
    TextView comment_remained_cnt;
    EditText comment_edittext;
    private TextWatcher searchBarWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_comment);
        getKeyboardHeight();
        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.transition_from_left, R.anim.transition_to_right);
    }

    public void init() {
        newcomment_post = findViewById(R.id.newcomment_post);
        newcomment_post.setAlpha((float) 0.3);
        newcomment_post.setOnClickListener(this);
        newcomment_post.setClickable(false);
        notification_listview = findViewById(R.id.newcomment_listview);
        notification_listview.setY(0-5000);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) notification_listview.getLayoutParams();
        lp.width = (int) (App.getInstance().screenWidth * 0.725);
        lp.height = lp.width;
        notification_listview.setLayoutParams(lp);

        comment_edittext = findViewById(R.id.comment_edittext);

        searchBarWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateComments(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        comment_edittext.addTextChangedListener(searchBarWatcher);
        comment_remained_cnt = findViewById(R.id.comment_remained_cnt);

        doDelay();
    }

    public void updateComments(String s) {
        int length = s.length();
        comment_remained_cnt.setText(String.valueOf(20 - length));
        if (length == 20) {
            comment_remained_cnt.setTextColor(Color.RED);
        } else {
            comment_remained_cnt.setTextColor(Color.GRAY);
        }
        if (Objects.equals(s, "")) {
            setEditablePost(false);
        } else {
            setEditablePost(true);
        }
    }

    public void setEditablePost(boolean b) {
        if (b) {
            newcomment_post.setClickable(true);
            newcomment_post.setAlpha((float) 1.0);
        } else {
            newcomment_post.setClickable(false);
            newcomment_post.setAlpha((float) 0.3);
        }
    }

    public void doDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.w("~~~~~keyboard height~~~~~~", String.valueOf(keyboardHeight));
                doAnimation();
            }
        }, 1500);
    }

    public void doAnimation() {
        List<Animator> animatorList = new ArrayList<>();

        animatorList.add(ViewPropertyObjectAnimator
                .animate(notification_listview)
                .withLayer()
                .y((App.getInstance().screenHeight - keyboardHeight - (int) (App.getInstance().screenWidth * 0.725)) / 2)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(500)
                .get());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new OvershootInterpolator(1.1f));
        animatorSet.playTogether(animatorList);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
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

    public void getKeyboardHeight() {
        final View myLayout = findViewById(R.id.home_root);
        myLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                myLayout.getWindowVisibleDisplayFrame(r);
                screenHeight = myLayout.getHeight();
                screenWidth = myLayout.getWidth();
                int heightDifference = screenHeight - (r.bottom - r.top);
                if (heightDifference > 100) {
                    isKeyboardPresent = true;
                    if (keyboardHeight == 0) {
                        keyboardHeight = heightDifference;

                    }
                } else if (isKeyboardPresent && myLayout != null &&
                        !myLayout.isFocused()) {
                    isKeyboardPresent = false;
                }
            }
        });

        unregistrar = KeyboardVisibilityEvent.registerEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        myLayout.requestFocus();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregistrar.unregister();
        comment_edittext.removeTextChangedListener(searchBarWatcher);
        comment_edittext.setText("");
        comment_edittext.addTextChangedListener(searchBarWatcher);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.newcomment_post:
                App.getInstance().post_comment = comment_edittext.getText().toString();
                finish();
                overridePendingTransition(R.anim.transition_from_left, R.anim.transition_to_right);
                break;
        }
    }
}
