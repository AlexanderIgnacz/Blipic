package com.trekr.Screens;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trekr.ApiService.ApiModel.BLPUser;
import com.trekr.ApiService.ApiModel.UserModel;
import com.trekr.App;
import com.trekr.R;
import com.trekr.Utils.Constants;
import com.trekr.Utils.TapOpacityHighlightLayout;
import com.trekr.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.trekr.Utils.Constants.kAlphaOff;
import static com.trekr.Utils.Constants.kAlphaOn;
import static com.trekr.Utils.Constants.kBLPAnimationDuration;
import static com.trekr.Utils.Constants.kBLPLoginSectionAnimationPosition;
import static com.trekr.Utils.Constants.kBLPPassRecoverySectionAnimationPosition;

public class LoginActivity extends AppCompatActivity implements View.OnFocusChangeListener, TextView.OnEditorActionListener {

    //region Variable declaration
    @BindView(R.id.btn_login)
    TapOpacityHighlightLayout loginButton;
    @BindView(R.id.btn_create_account)
    TextView createAccountButton;
    @BindView(R.id.form_welcome)
    RelativeLayout sectionCreateAccount;
    @BindView(R.id.txt_signup_firstName)
    EditText firstNameSignUpTextField;
    @BindView(R.id.txt_signup_lastName)
    EditText lastNameSignUpTextField;
    @BindView(R.id.txt_signup_email)
    EditText emailSignUpTextField;
    @BindView(R.id.txt_signup_password)
    EditText passwordSignUpTextField;
    @BindView(R.id.btn_signup)
    TapOpacityHighlightLayout signUpButton;
    @BindView(R.id.form_signup)
    RelativeLayout sectionCreateAccountFields;
    @BindView(R.id.txt_login_email)
    EditText emailLoginTextField;
    @BindView(R.id.txt_login_password)
    EditText passwordLoginTextField;
    @BindView(R.id.form_login)
    RelativeLayout sectionLoginFields;
    @BindView(R.id.txt_recover_pass_email)
    EditText passRecoveryTextField;
    @BindView(R.id.btn_send)
    TapOpacityHighlightLayout passwordRecoveryButton;
    @BindView(R.id.form_pass_recovery)
    RelativeLayout sectionPassRecovery;

    private boolean isLoginIn, isPassRecovery, isCreatingAccount;
    private EditText currentTextField;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Splash screen delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.AppTheme_TransparentStatus);

        super.onCreate(savedInstanceState);

        // Check if logged User
        if (Utils.loadObject(Constants.CURRENT_USER, BLPUser.class) != null) {
            App.getInstance().token = Utils.loadData(Constants.TOKEN);
            App.getInstance().loggedUser = Utils.loadObject(Constants.CURRENT_USER, BLPUser.class);
            App.getInstance().showHome(LoginActivity.this);
            return;
        }

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        EditText[] aryEditText = {firstNameSignUpTextField, lastNameSignUpTextField, emailSignUpTextField, passwordSignUpTextField,
                emailLoginTextField, passwordLoginTextField, passRecoveryTextField};

        for (EditText editText: aryEditText) {
            editText.setOnFocusChangeListener(this);
            editText.setOnEditorActionListener(this);
        }

        View[] aryView = {sectionLoginFields, sectionCreateAccountFields, sectionPassRecovery};

        for (View view: aryView) {
            view.setVisibility(View.GONE);
            view.setAlpha(kAlphaOff);
        }

        isLoginIn = false;
        isPassRecovery = false;
        isCreatingAccount = false;
    }

    //region Registration Actions & Animations

    @OnClick({R.id.btn_login, R.id.btn_create_account, R.id.btn_signup, R.id.btn_forgot_password, R.id.btn_cancel, R.id.btn_send, R.id.img_background})
    public void onViewClicked(View view) {
        boolean isHidden;
        switch (view.getId()) {
            // Tap outside gesture
            case R.id.img_background:
                dismissWithGesture();
                break;

            case R.id.btn_login:
                isLoginIn = true;
                isHidden = sectionLoginFields.getAlpha() == kAlphaOff;
                if (isHidden) {
                    startLoginAnimationWithValue(true);
                } else if (checkMandatoryFields()){
                    sendLoginRequest();
                } else {
                    showBlankFieldsAlert();
                }
                break;

            case R.id.btn_create_account:
                isCreatingAccount = true;
                isHidden = sectionCreateAccountFields.getAlpha() == kAlphaOff;
                if (isHidden) {
                    startSignUpAnimationWithValue(true);
                }
                break;

            case R.id.btn_signup:
                if (checkMandatoryFields()) {
                    registerUser();
                } else {
                    showBlankFieldsAlert();
                }
                break;

            case R.id.btn_forgot_password:
                animateScreenToDefault();

                isPassRecovery = true;

                isHidden = sectionPassRecovery.getAlpha() == kAlphaOff;

                if (isHidden) {
                    startPassRecoveryAnimationWithValue(true);
                }
                break;

            case R.id.btn_cancel:
                dismissWithGesture();
                break;

            case R.id.btn_send:
                if (checkMandatoryFields()) {
                    sendPassRecoveryEmail();
                } else {
                    showBlankFieldsAlert();
                }
                break;
        }
    }

    public void sendLoginRequest() {
        Utils.showLoading(this);

        App.getInstance().apiManager.loginUser(emailLoginTextField.getText().toString(), passwordLoginTextField.getText().toString(),
                new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                Utils.hideLoading();

                UserModel model = response.body();

                App.getInstance().loggedUser = model.data.user;
                App.getInstance().token = model.data.token;
                Utils.saveObject(Constants.CURRENT_USER, model.data.user);
                Utils.saveData(Constants.TOKEN, model.data.token);
                App.getInstance().showHome(LoginActivity.this);
            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                Utils.hideLoading();
                Utils.showAlertDialog(LoginActivity.this, getString(R.string.REQUEST_UNPERFORMED), t.getMessage());
            }
        });
    }

    public void resendCodeWithEmail(String emailString) {
        Utils.showLoading(this);

        App.getInstance().apiManager.resendConfirmationCodeWithEmail(emailString, new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Utils.hideLoading();
                if (response.isSuccessful()) {
                    Utils.showAlertDialog(LoginActivity.this, getString(R.string.ALERT_RESENT_EMAIL_TITLE), getString(R.string.ALERT_CONFIRM_MESSAGE));
                } else {
                    // TODO: Error message
                    Utils.showAlertDialog(LoginActivity.this, getString(R.string.REQUEST_UNPERFORMED), "Error occured!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Utils.hideLoading();
                Utils.showAlertDialog(LoginActivity.this, getString(R.string.REQUEST_UNPERFORMED), t.getMessage());
            }
        });
    }

    public void registerUser() {
        Utils.showLoading(this);
        App.getInstance().apiManager.signUpUser(firstNameSignUpTextField.getText().toString(), lastNameSignUpTextField.getText().toString(),
            emailSignUpTextField.getText().toString(), passwordSignUpTextField.getText().toString(), new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    Utils.hideLoading();
                    if (response.isSuccessful()) {
                        dismissWithGesture();
                        Utils.showAlertDialog(LoginActivity.this, getString(R.string.ALERT_CONFIRM_TITLE), getString(R.string.ALERT_CONFIRM_MESSAGE));
                    } else {
                        // TODO: Error Message
                        Utils.showAlertDialog(LoginActivity.this, getString(R.string.REQUEST_UNPERFORMED), "Error occured!");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    Utils.hideLoading();
                    Utils.showAlertDialog(LoginActivity.this, getString(R.string.REQUEST_UNPERFORMED), t.getMessage());
                }
            });

    };

    public void sendPassRecoveryEmail() {
        Utils.showLoading(this);

        App.getInstance().apiManager.resetPassword(passRecoveryTextField.getText().toString(), new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                Utils.hideLoading();
                if (response.isSuccessful()) {
                    Utils.showAlertDialog(LoginActivity.this, getString(R.string.ALERT_FORGOT_TITLE), getString(R.string.ALERT_FORGOT_MESSAGE));
                    animateScreenToDefault();
                } else {
                    // TODO:Error Message
                    Utils.showAlertDialog(LoginActivity.this, getString(R.string.REQUEST_UNPERFORMED), "Error Occured!");

                    if (isPassRecovery) {
                        passRecoveryTextField.requestFocus();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                Utils.hideLoading();
                Utils.showAlertDialog(LoginActivity.this, getString(R.string.REQUEST_UNPERFORMED), t.getMessage());

                if (isPassRecovery) {
                    passRecoveryTextField.requestFocus();
                }
            }
        });
    }

    public void dismissWithGesture() {
        if (isLoginIn || isCreatingAccount || isPassRecovery) {
            clearForms();
            animateScreenToDefault();
        }
    }

    public void animateScreenToDefault() {
        if (isLoginIn) {
            startLoginAnimationWithValue(false);
        } else if (isCreatingAccount) {
            startSignUpAnimationWithValue(false);
        }else if (isPassRecovery) {
            startPassRecoveryAnimationWithValue(false);
        }
    }

    public void startPassRecoveryAnimationWithValue(final boolean value) {
        final List<Animator> animatorList = new ArrayList<>();
        if (!value) {
            sectionCreateAccount.setVisibility(View.VISIBLE);
        } else {
            sectionPassRecovery.setVisibility(View.VISIBLE);
        }

        animatorList.add(ObjectAnimator
                .ofFloat(sectionCreateAccount, "alpha", (value) ? kAlphaOff  : kAlphaOn)
                .setDuration(kBLPAnimationDuration));

        animatorList.add(ObjectAnimator
                .ofFloat(sectionPassRecovery, "alpha", (value) ? kAlphaOn  : kAlphaOff)
                .setDuration(kBLPAnimationDuration));

        animatorList.add(ObjectAnimator
                .ofFloat(sectionPassRecovery, "translationY", ((value) ? kBLPPassRecoverySectionAnimationPosition : -kBLPPassRecoverySectionAnimationPosition))
                .setDuration(kBLPAnimationDuration));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(animatorList);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (value) {
                    sectionCreateAccount.setVisibility(View.GONE);
                } else {
                    sectionPassRecovery.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animatorSet.start();

        if (value) {
            passRecoveryTextField.requestFocus();
        } else {
            currentTextField.requestFocus();
        }

        if (!value) {
            isPassRecovery = false;
        }
    }

    public void startLoginAnimationWithValue(final boolean value) {
        List<Animator> animatorList = new ArrayList<>();
        if (!value) {
            sectionCreateAccount.setVisibility(View.VISIBLE);
        } else {
            sectionLoginFields.setVisibility(View.VISIBLE);
        }

        animatorList.add(ObjectAnimator
                .ofFloat(sectionCreateAccount, "alpha", (value) ? kAlphaOff  : kAlphaOn)
                .setDuration(kBLPAnimationDuration));

        animatorList.add(ObjectAnimator
                .ofFloat(sectionLoginFields, "alpha", (value) ? kAlphaOn  : kAlphaOff)
                .setDuration(kBLPAnimationDuration));

        animatorList.add(ObjectAnimator
                .ofFloat(sectionLoginFields, "translationY", ((value) ? kBLPLoginSectionAnimationPosition : -kBLPLoginSectionAnimationPosition))
                .setDuration(kBLPAnimationDuration));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(animatorList);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (value) {
                    sectionCreateAccount.setVisibility(View.GONE);
                } else {
                    sectionLoginFields.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animatorSet.start();

        if (value) {
            emailLoginTextField.requestFocus();
        } else {
            currentTextField.requestFocus();
        }

        if (!value) {
            isLoginIn = false;
        }
    }

    public void startSignUpAnimationWithValue(final boolean value) {
        List<Animator> animatorList = new ArrayList<>();
        if (!value){
            sectionCreateAccount.setVisibility(View.VISIBLE);
        } else {
            sectionCreateAccountFields.setVisibility(View.VISIBLE);
        }

        animatorList.add(ObjectAnimator
                .ofFloat(sectionCreateAccount, "alpha", (value) ? kAlphaOff  : kAlphaOn)
                .setDuration(kBLPAnimationDuration));

        animatorList.add(ObjectAnimator
                .ofFloat(sectionCreateAccountFields, "alpha", (value) ? kAlphaOn  : kAlphaOff)
                .setDuration(kBLPAnimationDuration));

        animatorList.add(ObjectAnimator
                .ofFloat(sectionCreateAccountFields, "translationY", ((value) ? kBLPLoginSectionAnimationPosition : -kBLPLoginSectionAnimationPosition))
                .setDuration(kBLPAnimationDuration));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(animatorList);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!value){
                    sectionCreateAccountFields.setVisibility(View.GONE);
                } else {
                    sectionCreateAccount.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animatorSet.start();

        if (value) {
            firstNameSignUpTextField.requestFocus();
        } else {
            currentTextField.requestFocus();
        }

        if (!value) {
            isCreatingAccount = false;
        }
    }
    //endregion

    //region UITextField Delegate Methods
    @Override
    public void onFocusChange(View view, boolean b) {
        if (b && view instanceof EditText){
            currentTextField = (EditText)view;
        }
    }

    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
            if (checkMandatoryFields()) {
                if (isLoginIn) {
                    sendLoginRequest();
                } else if (isCreatingAccount) {
                    registerUser();
                } else if (isPassRecovery) {
                    sendPassRecoveryEmail();
                }
            }
            else {
                showBlankFieldsAlert();
            }
        }
        return false;
    }
    //endregion

    //region Forms Methods

    public boolean checkMandatoryFields() {
        if (isLoginIn) {
            return emailLoginTextField.getText().length() > 0 && passwordLoginTextField.getText().length() > 0;
        } else if (isCreatingAccount) {
            return firstNameSignUpTextField.getText().length() > 0 && lastNameSignUpTextField.getText().length() > 0
                    && emailSignUpTextField.getText() .length() > 0 && passwordSignUpTextField.getText().length() > 0;
        } else if (isPassRecovery) {
            return passRecoveryTextField.getText().length() > 0;
        }

        return false;
    }

    public void clearForms() {
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(currentTextField.getWindowToken(), 0);

        emailLoginTextField.setText("");
        passwordLoginTextField.setText("");
        firstNameSignUpTextField.setText("");
        lastNameSignUpTextField.setText("");
        emailSignUpTextField.setText("");
        passwordSignUpTextField.setText("");
        passRecoveryTextField.setText("");

    }

    public void showBlankFieldsAlert() {
        Utils.showAlertDialog(this, getString(R.string.ALERT_TITLE_WARNING), getString(R.string.ALERT_BLANK_FIELDS));

        if (isLoginIn) {
            emailLoginTextField.requestFocus();
        } else if (isCreatingAccount) {
            firstNameSignUpTextField.requestFocus();
        } else if (isPassRecovery) {
            passRecoveryTextField.requestFocus();
        }
    }
    //endregion

}
