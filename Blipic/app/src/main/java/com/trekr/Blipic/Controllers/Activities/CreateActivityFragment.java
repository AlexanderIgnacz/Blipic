package com.trekr.Blipic.Controllers.Activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator;
import com.trekr.App;
import com.trekr.Blipic.Controllers.HomeMap.RepetitionOption;
import com.trekr.Common.Activities.CommentActivity;
import com.trekr.Common.Activities.SelectTypeActivity;
import com.trekr.Common.AlphaBetRecycler.adapter.RecyclerViewAdapter;
import com.trekr.Common.AlphaBetRecycler.helper.DataHelper;
import com.trekr.Common.AlphaBetRecycler.utility.AlphabetItem;
import com.trekr.R;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;


public class CreateActivityFragment extends Fragment implements View.OnClickListener, ActionSheet.ActionSheetListener, CompoundButton.OnCheckedChangeListener {

    View rootView;
    ImageView addPhotoBtn;
    View inviteFriendBtn;
    View create_activity_google_datepicker;
    Calendar myCalendar = Calendar.getInstance();
    TextView create_activity_date_text;
    View black_back;
    View create_activity_invite_dlg;
    View invite_modal_backbtn;
    IndexFastScrollRecyclerView mRecyclerView;
    Switch create_activity_repetition_switch;
    RepetitionOption create_activity_repetition;
    View create_activity_select_type;
    View fragment_header_rightbtn;
    TextView create_activity_select_type_text;
    EditText create_activity_invitefriend_search;
    private TextWatcher searchBarWatcher;
    View create_activity_invitefriend_cancelfind;

    private boolean isAnimating = false;
    private List<String> mDataArray;
    private List<AlphabetItem> mAlphabetItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_create_activity, container, false);

        setHeaderOptions();
        setupViews();

        return rootView;
    }

    public void setHeaderOptions() {
        TextView center = rootView.findViewById(R.id.fragment_header_centertitle);
        center.setText("Create An Activity");
        TextView prev = rootView.findViewById(R.id.fragment_header_leftbtn);
        prev.setVisibility(View.GONE);
        TextView next = rootView.findViewById(R.id.fragment_header_rightbtn);
        next.setVisibility(View.VISIBLE);
        next.setText("Next");
    }

    public void setupViews() {
        addPhotoBtn = rootView.findViewById(R.id.create_activity_addphoto);
        addPhotoBtn.setOnClickListener(this);

        inviteFriendBtn = rootView.findViewById(R.id.create_activity_invitefriend);
        inviteFriendBtn.setOnClickListener(this);

        create_activity_google_datepicker = rootView.findViewById(R.id.create_activity_google_datepicker);
        create_activity_google_datepicker.setOnClickListener(this);

        create_activity_date_text = rootView.findViewById(R.id.create_activity_date_text);
        black_back = rootView.findViewById(R.id.black_back);
        black_back.setOnClickListener(this);
        black_back.setVisibility(View.GONE);
        black_back.setAlpha((float) 0.0);

        create_activity_invite_dlg = rootView.findViewById(R.id.create_activity_invite_dlg);
        create_activity_invite_dlg.setY(0-4000);
        ViewGroup.LayoutParams lp = create_activity_invite_dlg.getLayoutParams();
        lp.width = App.getInstance().screenWidth - 120;
        lp.height = App.getInstance().screenHeight - 400;
        create_activity_invite_dlg.setLayoutParams(lp);

        invite_modal_backbtn = rootView.findViewById(R.id.invite_modal_backbtn);
        invite_modal_backbtn.setOnClickListener(this);

        create_activity_repetition = rootView.findViewById(R.id.create_activity_repetition);

        create_activity_repetition_switch = rootView.findViewById(R.id.create_activity_repetition_switch);
        create_activity_repetition_switch.setOnCheckedChangeListener(this);
        create_activity_repetition_switch.setChecked(true);

        create_activity_select_type = rootView.findViewById(R.id.create_activity_select_type);
        create_activity_select_type.setOnClickListener(this);

        fragment_header_rightbtn = rootView.findViewById(R.id.fragment_header_rightbtn);
        fragment_header_rightbtn.setOnClickListener(this);

        create_activity_invitefriend_search = rootView.findViewById(R.id.create_activity_invitefriend_search);

        create_activity_invitefriend_cancelfind = rootView.findViewById(R.id.create_activity_invitefriend_cancelfind);
        create_activity_invitefriend_cancelfind.setOnClickListener(this);

        create_activity_select_type_text = rootView.findViewById(R.id.create_activity_select_type_text);
        App.getInstance().selectActivityType("Empty");


        searchBarWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        create_activity_invitefriend_search.addTextChangedListener(searchBarWatcher);
    }

    @Override
    public void onClick(View v) {
        if (isAnimating) {
            return;
        }
        int id = v.getId();
        switch (id) {
            case R.id.create_activity_addphoto:
                ActionSheet.createBuilder(getContext(), getFragmentManager())
                        .setCancelButtonTitle("Cancel")
                        .setOtherButtonTitles("Take Photo", "Take Video", "Choose Existing Photo or Video")
                        .setCancelableOnTouchOutside(true)
                        .setListener(this).show();
                break;
            case R.id.create_activity_invitefriend:
                isAnimating = true;
                initFriendSearch("");
                doShowAnimation();
                break;
            case R.id.create_activity_google_datepicker:

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }

                };
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.invite_modal_backbtn:
                doHideAnimation();
                break;
            case R.id.black_back:
                break;
            case R.id.create_activity_select_type:
                if (Objects.equals(App.getInstance().activityType, "Empty")) {
                    Intent intent;
                    intent = new Intent(getActivity(), SelectTypeActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.transition_from_right, R.anim.transition_to_left);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Change activity type?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent;
                                    intent = new Intent(getActivity(), SelectTypeActivity.class);
                                    getActivity().startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.transition_from_right, R.anim.transition_to_left);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                break;
//            case R.id.fragment_header_rightbtn:
//                Intent intent;
//                intent = new Intent(getActivity(), CommentActivity.class);
//                getActivity().startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.transition_from_bottom, R.anim.transition_to_top);
//                break;
            case R.id.create_activity_invitefriend_cancelfind:
                create_activity_invitefriend_search.removeTextChangedListener(searchBarWatcher);
                create_activity_invitefriend_search.setText("");
                create_activity_invitefriend_search.addTextChangedListener(searchBarWatcher);
                break;
        }
    }

    public void doShowAnimation() {
        black_back.setVisibility(View.VISIBLE);
        black_back.setAlpha((float) 0.0);
        List<Animator> animatorList = new ArrayList<>();

        animatorList.add(ViewPropertyObjectAnimator
                .animate(create_activity_invite_dlg)
                .withLayer()
                .y((App.getInstance().screenHeight) / 2 - create_activity_invite_dlg.getHeight() / 2)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(300)
                .get());
        animatorList.add(ViewPropertyObjectAnimator
                .animate(black_back)
                .withLayer()
                .alpha((float) 0.8)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(300)
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

    public void doHideAnimation() {
        isAnimating = true;
        black_back.setVisibility(View.VISIBLE);
        black_back.setAlpha((float) 0.8);
        List<Animator> animatorList = new ArrayList<>();

        animatorList.add(ViewPropertyObjectAnimator
                .animate(create_activity_invite_dlg)
                .withLayer()
                .y(4000)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(300)
                .get());
        animatorList.add(ViewPropertyObjectAnimator
                .animate(black_back)
                .withLayer()
                .alpha((float) 0.0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(300)
                .get());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorList);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                black_back.setVisibility(View.GONE);
                create_activity_invite_dlg.setY(0-4000);
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

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        create_activity_date_text.setText(sdf.format(myCalendar.getTime()));
    }

    protected  void initFriendSearch(String filter) {
        initialiseData(filter);
        initialiseUI();
    }

    protected void initialiseData(String filter) {
        //Recycler view data
        mDataArray = DataHelper.getAlphabetData(filter);

        //Alphabet fast scroller data
        mAlphabetItems = new ArrayList<>();
        List<String> strAlphabets = new ArrayList<>();
        for (int i = 0; i < mDataArray.size(); i++) {
            String name = mDataArray.get(i);
            if (name == null || name.trim().isEmpty()) {
                continue;
            }

            String word = name.substring(0, 1);
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word);
                mAlphabetItems.add(new AlphabetItem(i, word, false));
            }
        }
    }

    protected void initialiseUI() {
        mRecyclerView = rootView.findViewById(R.id.fast_scroller_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new RecyclerViewAdapter(mDataArray));

        mRecyclerView.setIndexTextSize(12);
        mRecyclerView.setIndexBarColor("#33334c");
        mRecyclerView.setIndexBarCornerRadius(0);
        mRecyclerView.setIndexBarTransparentValue((float) 0.4);
        mRecyclerView.setIndexbarMargin(0);
        mRecyclerView.setIndexbarWidth(40);
        mRecyclerView.setPreviewPadding(0);
        mRecyclerView.setIndexBarTextColor("#FFFFFF");

        mRecyclerView.setIndexBarVisibility(true);
        mRecyclerView.setIndexbarHighLateTextColor("#33334c");
        mRecyclerView.setIndexBarHighLateTextVisibility(true);
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.create_activity_repetition_switch) {
            create_activity_repetition.setComponentEnable(b);
            if (b) {
                create_activity_repetition.setAlpha((float) 1.0);
            } else {
                create_activity_repetition.setAlpha((float) 0.5);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String s = App.getInstance().activityType;
        if (!Objects.equals(s, "Empty")) {
            create_activity_select_type_text.setText(s);
        }
    }

    void filter(String filter) {
        initFriendSearch(filter);
    }
}
