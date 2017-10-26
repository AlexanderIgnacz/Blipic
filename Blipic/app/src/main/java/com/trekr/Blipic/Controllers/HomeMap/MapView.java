package com.trekr.Blipic.Controllers.HomeMap;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;
import com.trekr.ApiService.ApiModel.BLPBlip;
import com.trekr.ApiService.ApiModel.BlipModel;
import com.trekr.App;
import com.trekr.Blipic.Controllers.HomeMap.Callout.ActivityCalloutView;
import com.trekr.Blipic.Controllers.HomeMap.Callout.BlipDetailActivity;
import com.trekr.Blipic.Controllers.HomeMap.Callout.DestinationCalloutView;
import com.trekr.Blipic.Controllers.HomeMap.Callout.MergeDestinationCallout;
import com.trekr.Blipic.Controllers.HomeMap.Components.BLPAnimatedImageView;
import com.trekr.Blipic.Controllers.HomeMap.Components.CategoryView;
import com.trekr.Blipic.Controllers.HomeMap.Components.SubCategoryView;
import com.trekr.Blipic.Controllers.HomeMap.Model.BlipRenderer;
import com.trekr.Blipic.Models.BLPCategory;
import com.trekr.Blipic.Models.BLPSubCategory;
import com.trekr.BlipicWellness.Controllers.HomeActivity;
import com.trekr.Common.Activities.CommentActivity;
import com.trekr.Common.HorizontalPicker.HorizontalPicker;
import com.trekr.R;
import com.trekr.Utils.TapOpacityHighlightLayout;
import com.trekr.Utils.Utils;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.trekr.Utils.Constants.kAlphaOff;
import static com.trekr.Utils.Constants.kAlphaOn;
import static com.trekr.Utils.Constants.kBLPCategoryDuration;
import static com.trekr.Utils.Constants.kBLPSubCategoryDuration;
import static com.trekr.Utils.Constants.kBLPTextLocationDuration;

public class MapView extends RelativeLayout implements OnMapReadyCallback,
        View.OnFocusChangeListener,
        ClusterManager.OnClusterClickListener<BLPBlip>,
        ClusterManager.OnClusterInfoWindowClickListener<BLPBlip>,
        ClusterManager.OnClusterItemClickListener<BLPBlip>,
        ClusterManager.OnClusterItemInfoWindowClickListener<BLPBlip>,
        CategoryView.OnOutClicked,
        CategoryView.OnCategoryItemClicked,
        SubCategoryView.OnSubCategoryItemClicked, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnMapClickListener, ActionSheet.ActionSheetListener {
    //region Variables

    //region UI variables
    Unbinder unbinder;
    @BindView(R.id.searchAreaButton)
    TextView searchAreaButton;
    @BindView(R.id.locationButton)
    TapOpacityHighlightLayout locationButton;
    @BindView(R.id.viewSerchAndDay)
    LinearLayout viewSerchAndDay;
    @BindView(R.id.searchResultsTableView)
    ListView searchResultsTableView;
    @BindView(R.id.categoriesView)
    CategoryView categoriesView;
    @BindView(R.id.subCategoriesView)
    SubCategoryView subCategoriesView;
    @BindView(R.id.textFieldSearchBar)
    EditText textFieldSearchBar;
    @BindView(R.id.home_view)
    RelativeLayout homeView;
    @BindView(R.id.textFieldSearchLocation)
    PlacesAutocompleteTextView textFieldSearchLocation;
    @BindView(R.id.blurView)
    ImageView blurView;
    @BindView(R.id.calendar_horizontal_picker)
    HorizontalPicker calendarView;
    @BindView(R.id.home_daypicker_container)
    RelativeLayout calendarViewContainer;
    @BindView(R.id.searchbarSideLogo)
    BLPAnimatedImageView searchbarSideLogo;
    @BindView(R.id.btnClearLocation)
    TapOpacityHighlightLayout btnClearLocation;
    @BindView(R.id.btnClearSearch)
    TapOpacityHighlightLayout btnClearSearch;
    @BindView(R.id.lay_textFieldSearchLocation)
    LinearLayout layTextFieldSearchLocation;
    //endregion

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int DEFAULT_ZOOM = 10;
    private Location mLastKnownLocation = null;
    private static final String KEY_LOCATION = "location";
    private static final String kBLPCurrentLocationText = "Current Location";
    private ReactiveLocationProvider reactiveLocationProvider;

    private int keyboardHeight;
    private int screenWidth, screenHeight;
    private boolean isKeyboardPresent;
    private boolean isloadingBlips;
    private Location bestEffortAtLocation;
    private int bottomOffsetForViewSerchAndDay;
    private BLPCategory selectedCategory;
    private ArrayList<LatLng> visitedCoordsArray;

    private boolean animatings = false;

    private int mMarkerSize;
    private ArrayList<BLPBlip> blips;
    private BLPBlip blipToShow;
    private boolean isCanShowBlip;
    private String selectedLocation;
    private TextWatcher searchLocationWatcher, searchBarWatcher;
    private String prevLocationText;

    Unregistrar unregistrar;

    private Marker selectedMarker;
    private PopupWindow mCallout;
    private BLPBlip selectedBlip;

    private int currentCalendarIndex;
    private Date currentCalendarDate;
    private String selectedDate;

    private LatLng searchCoordinates;

    private Context context;

    private int IMAGE_PICKER_SELECT = 998;
    //endregion

    //region Constructors
    public MapView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }
    //endregion

    //region Init

    private void initView() {
        RelativeLayout view = (RelativeLayout) inflate(getContext(), R.layout.view_map, null);;
        addView(view);

        unbinder = ButterKnife.bind(this);

        ((HomeActivity) context).setStatusBar(false, getResources().getColor(android.R.color.black));

        // Load saved current location
//        if (savedInstanceState != null) {
//            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
//        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        final View myLayout = view.findViewById(R.id.home_view);
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
                        setCategoriesLayout();

                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(animationWithAllFilters(true, false));
                        animatorSet.start();
                    }
                } else if (isKeyboardPresent && homeView != null &&
                        !homeView.isFocused() && viewSerchAndDay.getVisibility() == View.VISIBLE) {
                    isKeyboardPresent = false;
                }
            }
        });

        unregistrar = KeyboardVisibilityEvent.registerEventListener(
                (HomeActivity)context,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen) {
                        } else if (!homeView.isFocused() && viewSerchAndDay.getVisibility() == View.VISIBLE) {
                            btnClearSearch.setVisibility(View.INVISIBLE);
                            homeView.requestFocus();
                            viewSerchAndDay.setVisibility(View.INVISIBLE);
                        }
                    }
                });

        ViewGroup.LayoutParams layoutParams = textFieldSearchLocation.getLayoutParams();
        layoutParams.width = screenWidth / 2 - 30;
        textFieldSearchLocation.setLayoutParams(layoutParams);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        unbinder.unbind();
        unregistrar.unregister();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        initControls();
        setUpMap();
    }

    public void initControls() {
        mMarkerSize = (int) getResources().getDimension(R.dimen.custom_profile_image);

        isKeyboardPresent = false;
        isloadingBlips = false;

        viewSerchAndDay.setVisibility(View.INVISIBLE);
        searchAreaButton.setVisibility(View.INVISIBLE);
        viewSerchAndDay.setAlpha(kAlphaOff);
        blurView.setVisibility(View.INVISIBLE);
        blurView.setAlpha((float) kAlphaOff);
        reactiveLocationProvider = new ReactiveLocationProvider(getContext());

        initCategoriesFilters();
        setupCalendarView();

        btnClearSearch.setVisibility(View.INVISIBLE);

        selectedLocation = kBLPCurrentLocationText;
        searchLocationWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                prevLocationText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    btnClearLocation.setVisibility(View.INVISIBLE);
                } else if (btnClearLocation.getVisibility() != View.VISIBLE) {
                    btnClearLocation.setVisibility(View.VISIBLE);
                }

                if (prevLocationText.equals(kBLPCurrentLocationText)) {
                    if (!TextUtils.isEmpty(s.toString())) {
                        textFieldSearchLocation.removeTextChangedListener(searchLocationWatcher);
                        textFieldSearchLocation.setText("");
                        btnClearLocation.setVisibility(View.INVISIBLE);
                        textFieldSearchLocation.addTextChangedListener(searchLocationWatcher);
                    }
                } else {
//                    String text = [textField.text stringByReplacingCharactersInRange:range
//                    withString:string];
//
//                    [self fetchGooglePlacesSearchResultsWithText:text];
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        textFieldSearchLocation.addTextChangedListener(searchLocationWatcher);

        searchBarWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    btnClearSearch.setVisibility(View.INVISIBLE);

                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(animationWithAllFilters(true, false));
                    animatorSet.start();
                } else {
                    if (btnClearSearch.getVisibility() != View.VISIBLE) {
                        btnClearSearch.setVisibility(View.VISIBLE);
                    }
                    if (s.length() == 1) {
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(animationWithAllFilters(false, true));
                        animatorSet.start();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        textFieldSearchBar.addTextChangedListener(searchBarWatcher);

        textFieldSearchLocation.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(@NonNull final Place place) {
                textFieldSearchLocation.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(PlaceDetails placeDetails) {
                        searchAreaButton.setVisibility(View.INVISIBLE);

                        selectedLocation = place.description;

                        searchCoordinates = new LatLng(placeDetails.geometry.location.lat, placeDetails.geometry.location.lng);

                        searchBlipsWithProgresHUD(false);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
            }
        });
//        calloutView = [BLPSMCalloutView new];

//        calloutView.delegate = self;
//        calloutView = nil;

//        mapView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
//        mapView.showsPointsOfInterest = false;
//        mapView.delegate = self;
//        mapView.rotateEnabled = false;
//        mapView.calloutView = calloutView;
//
//        mapClusterController = [[CCHMapClusterController alloc]initWithMapView:
//        mapView];
//        mapClusterController.cellSize = 70;
//
//        mapClusterController.marginFactor = 0.7;
//        mapClusterController.delegate = self;


//        cameraButton = [[BLPQuickAccessCamera alloc]initWithViewController:
//        self];
//    [view addSubview:cameraButton];

//        UIPanGestureRecognizer * mapViewPanGesture = [[UIPanGestureRecognizer alloc]initWithTarget:
//        self action:nil];
//        UITapGestureRecognizer * mapViewTapGesture = [[UITapGestureRecognizer alloc]initWithTarget:
//        self action:nil];
//        UIPanGestureRecognizer * panGestureResultsTable = [[UIPanGestureRecognizer alloc]
//        initWithTarget:
//        self action:nil];
//        mapViewPanGesture.delegate = self;
//        mapViewTapGesture.delegate = self;
//        panGestureResultsTable.delegate = self;

//    [mapView addGestureRecognizer:mapViewPanGesture];
//    [mapView addGestureRecognizer:mapViewTapGesture];
//    [searchResultsTableView addGestureRecognizer:panGestureResultsTable];
//
//        mapViewPanGesture = mapViewPanGesture;
//        mapViewTapGesture = mapViewTapGesture;
//        searchResultsTableViewPanGesture = panGestureResultsTable;

//    [self initGoogleQuery];
//
//        selectedLocation = kBLPCurrentLocationText;
//
        visitedCoordsArray = new ArrayList<>();
        blips = new ArrayList<>();
//        searchingLocationQueue = dispatch_queue_create("searchingLocationQueue", NULL);
//        isNeedZoomOut = false;

//#ifdef BLIPIC
//
//    [self showMarketingScreensIfNeed];
//    [self showInstractionsIfNeed];
//        screenName = kBLPGAViewHome;
//#else
//
//    [[BLPAppDelegate sharedInstance]checkUserLocation];
//#endif
    }

    private void setUpMap() {
        ((SupportMapFragment) (((HomeActivity) context).getSupportFragmentManager().findFragmentById(R.id.mapView))).getMapAsync(this);
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        if (mMap != null) {
//            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
//        }
//        super.onSaveInstanceState(outState);
//    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (map == null) {
            return;
        }
        mMap = map;

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);

        getDeviceLocation();
        startClusering();
    }

    private void getDeviceLocation() {

        textFieldSearchLocation.removeTextChangedListener(searchLocationWatcher);
        textFieldSearchLocation.setText(kBLPCurrentLocationText);
        textFieldSearchLocation.addTextChangedListener(searchLocationWatcher);

        selectedLocation = kBLPCurrentLocationText;

        Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
        locationResult.addOnCompleteListener((HomeActivity)context, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    bestEffortAtLocation = task.getResult();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(bestEffortAtLocation.getLatitude(),
                                    bestEffortAtLocation.getLongitude()), DEFAULT_ZOOM));
                }
            }
        });
    }

    //endregion

    //region UI Delegates
    @OnEditorAction({ R.id.textFieldSearchBar, R.id.textFieldSearchLocation })
    public boolean onEditorAction(EditText v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            homeView.requestFocus();
            Utils.hideSoftKeyboard((HomeActivity)context);

            mClusterManager.clearItems();
            mMap.clear();
            blips.clear();
//            self.isNeedZoomOut = YES;
            searchAreaButton.setVisibility(View.INVISIBLE);
            fetchSearchResults();

            viewSerchAndDay.setVisibility(View.INVISIBLE);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animationWithAllFilters(false, true));
            animatorSet.start();

            return true;
        }
        return false;
    }

    @OnClick({R.id.locationButton, R.id.blurView, R.id.textFieldSearchLocation, R.id.btnClearLocation, R.id.btnClearSearch, R.id.lay_textFieldSearchLocation, R.id.searchAreaButton, R.id.btn_camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.locationButton:
                getDeviceLocation();
                break;
            case R.id.blurView:
                onOutClicked();
                break;
            case R.id.btnClearLocation:
                textFieldSearchLocation.removeTextChangedListener(searchLocationWatcher);
                textFieldSearchLocation.setText("");
                btnClearLocation.setVisibility(View.INVISIBLE);
                textFieldSearchLocation.addTextChangedListener(searchLocationWatcher);

//                [self fetchGooglePlacesSearchResultsWithText:nil];
//                [self refreshBlips];
                break;
            case R.id.btnClearSearch:
                textFieldSearchBar.removeTextChangedListener(searchBarWatcher);
                textFieldSearchBar.setText("");
                btnClearSearch.setVisibility(View.INVISIBLE);
                textFieldSearchBar.addTextChangedListener(searchBarWatcher);

//                [self fetchGooglePlacesSearchResultsWithText:nil];
//                [self refreshBlips];

            case R.id.lay_textFieldSearchLocation:
            case R.id.textFieldSearchLocation: {
                if (textFieldSearchLocation.isFocusable()) {
                    if (!textFieldSearchLocation.isFocused()) {
                        textFieldSearchLocation.requestFocus();
                    }
                    break;
                }
                textFieldSearchLocation.setFocusable(true);
                textFieldSearchLocation.setFocusableInTouchMode(true);

                subCategoriesView.setVisibility(View.INVISIBLE);

                List<Animator> animatorList = new ArrayList<>();

                animatorList.addAll(animationsTextFieldLocationEdit(true, false));
                animatorList.addAll(animationWithAllFilters(true, false));

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animatorList);
                animatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (currentCalendarDate != null) {
                            calendarView.setSelectedItem(currentCalendarIndex);
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

                break;
            }
            case R.id.searchAreaButton:
                searchAreaButton.setVisibility(View.INVISIBLE);
//                isNeedZoomOut = true;
                refreshBlips();
                break;
            case R.id.btn_camera:
                ActionSheet.createBuilder(getContext(), ((HomeActivity)getContext()).getSupportFragmentManager())
                        .setCancelButtonTitle("Cancel")
                        .setOtherButtonTitles("Take Photo", "Take Video", "Choose Existing Photo or Video")
                        .setCancelableOnTouchOutside(true)
                        .setListener(this).show();
                break;
        }
    }

    @OnFocusChange({R.id.textFieldSearchBar, R.id.textFieldSearchLocation})
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            switch (v.getId()) {
                case R.id.textFieldSearchBar:
                    if (textFieldSearchBar.getText().toString().length() > 0) {
                        btnClearSearch.setVisibility(View.VISIBLE);
                    }
                    if (keyboardHeight != 0) {

                        hideCallout();

                        List<Animator> animatorList = new ArrayList<>();

                        if (subCategoriesView.getVisibility() != View.VISIBLE) {
                            animatorList.addAll(animationWithAllFilters(true, false));
                        }
                        animatorList.addAll(animationsTextFieldLocationEdit(false, true));

                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(animatorList);
                        animatorSet.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                if (currentCalendarDate != null) {
                                    calendarView.setSelectedItem(currentCalendarIndex);
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

                        if (TextUtils.isEmpty(textFieldSearchLocation.getText().toString()) && TextUtils.isEmpty(selectedLocation)) {
                            selectedLocation = kBLPCurrentLocationText;
                            textFieldSearchLocation.removeTextChangedListener(searchLocationWatcher);
                            textFieldSearchLocation.setText(selectedLocation);
                            textFieldSearchLocation.addTextChangedListener(searchLocationWatcher);
                        } else {
                            textFieldSearchLocation.removeTextChangedListener(searchLocationWatcher);
                            textFieldSearchLocation.setText(selectedLocation);
                            textFieldSearchLocation.addTextChangedListener(searchLocationWatcher);
                        }
                    }
                    break;
                case R.id.textFieldSearchLocation:
                    textFieldSearchLocation.setCursorVisible(true);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(animationsTextFieldLocationEdit(true, false));
                    animatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (currentCalendarDate != null) {
                                calendarView.setSelectedItem(currentCalendarIndex);
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
                    break;
            }
        } else {
            switch (v.getId()) {
                case R.id.textFieldSearchBar: {
                    if (subCategoriesView.getVisibility() == View.VISIBLE && viewSerchAndDay.getVisibility() == View.VISIBLE) {
                        subCategoriesView.setVisibility(View.INVISIBLE);
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(animationWithAllFilters(true, false));
                        animatorSet.start();
                        return;
                    }
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(animationWithAllFilters(false, true));
                    animatorSet.start();

                    break;
                }
                case R.id.textFieldSearchLocation: {
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(animationsTextFieldLocationEdit(false, true));
                    animatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (currentCalendarDate != null) {
                                calendarView.setSelectedItem(currentCalendarIndex);
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
                    if (TextUtils.isEmpty(textFieldSearchLocation.getText().toString()) && TextUtils.isEmpty(selectedLocation)) {
                        selectedLocation = kBLPCurrentLocationText;
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        switch(index) {
            case 0:

                break;
            case 1:
                break;
            case 2:
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/* video/*");
                ((HomeActivity)context).startActivityForResult(pickIntent, IMAGE_PICKER_SELECT);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri selectedMediaUri = data.getData();
            if (selectedMediaUri.toString().contains("image")) {
                //handle image
            } else  if (selectedMediaUri.toString().contains("video")) {
                //handle video
            }
        }
    }

    //endregion

    //region Map
    private ClusterManager<BLPBlip> mClusterManager;

    @Override
    public boolean onClusterClick(Cluster<BLPBlip> cluster) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        final LatLngBounds bounds = builder.build();

        mMap.setPadding(0, 200, 0, 0);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        mMap.setPadding(0, 0, 0, 0);

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<BLPBlip> cluster) {
    }

    @Override
    public boolean onClusterItemClick(final BLPBlip item) {
        selectedBlip = item;

        hideCallout();

        View contentView = null;
        if (selectedBlip.isKindOfBlip(BLPBlip.BLPBlipTypes.BLPBlipTypeActivity)) {
            contentView = new ActivityCalloutView((HomeActivity)context, item, new DestinationCalloutView.CalloutListener() {
                @Override
                public void onClose() {
                    hideCallout();
                }

                @Override
                public void onShowDetails() {

                }

                @Override
                public void onShowComments() {

                }
            });
        } else if (selectedBlip.isKindOfBlip(BLPBlip.BLPBlipTypes.BLPBlipTypeDestination)) {
            contentView = new MergeDestinationCallout((HomeActivity)context, item, new DestinationCalloutView.CalloutListener() {
                @Override
                public void onClose() {
                    hideCallout();
                }

                @Override
                public void onShowDetails() {
                    App.getInstance().selectedBlip = item;
                    context.startActivity(new Intent(context, BlipDetailActivity.class));
                }

                @Override
                public void onShowComments() {
                    context.startActivity(new Intent(context, CommentActivity.class));
                }
            });
        }

        mCallout = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mCallout.showAsDropDown(textFieldSearchBar
                ,(screenWidth - getResources().getDimensionPixelSize(R.dimen.callout_width)) / 2 - getResources().getDimensionPixelSize(R.dimen.home_margin)
                ,20);

        int calloutBottomY = getResources().getDimensionPixelSize(R.dimen.callout_height) + textFieldSearchBar.getHeight() + getResources().getDimensionPixelSize(R.dimen.home_searchbar_margin) + 20;

        Projection projection = mMap.getProjection();
        Point markerPoint = projection.toScreenLocation(selectedMarker.getPosition());
        Point targetPoint = new Point(markerPoint.x, markerPoint.y - mMarkerSize - calloutBottomY + screenHeight / 2 - 2);
        LatLng targetPosition = projection.fromScreenLocation(targetPoint);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(targetPosition), 500, null);

        return true;
    }

    @Override
    public void onClusterItemInfoWindowClick(BLPBlip item) {
    }

    protected void startClusering() {
        mClusterManager = new ClusterManager<>((HomeActivity)context, mMap, new MarkerManager(mMap) {
            @Override
            public boolean onMarkerClick(Marker marker) {
                selectedMarker = marker;
                return super.onMarkerClick(marker);
            }
        });
        mClusterManager.setRenderer(new BlipRenderer((HomeActivity)context, mMap, mClusterManager));
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnMapClickListener(this);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
    }

    public void hideCallout() {
        if (mCallout != null) {
            mCallout.dismiss();
            mCallout = null;
        }
    }

    private void addGeofence(List<BLPBlip> blips) {
        for (BLPBlip blip : blips) {
            final GeofencingRequest geofencingRequest = createGeofencingRequest(blip.getCoordinate().longitude, blip.getCoordinate().latitude, 25);
            if (geofencingRequest == null) return;

            final PendingIntent pendingIntent = createNotificationBroadcastPendingIntent();

            reactiveLocationProvider
                    .removeGeofences(pendingIntent)
                    .flatMap(new Function<Status, Observable<Status>>() {
                        @Override
                        public Observable<Status> apply(Status status) throws Exception {
                            return reactiveLocationProvider.addGeofences(pendingIntent, geofencingRequest);
                        }

                    })
                    .subscribe(new Consumer<Status>() {
                        @Override
                        public void accept(Status addGeofenceResult) {
//                            toast("Geofence added, success: " + addGeofenceResult.isSuccess());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
//                            toast("Error adding geofence.");
//                            Log.d(TAG, "Error adding geofence.", throwable);
                        }
                    });
        }
    }

    private void clearGeofence() {
        reactiveLocationProvider
                .removeGeofences(createNotificationBroadcastPendingIntent())
                .subscribe(new Consumer<Status>() {
                    @Override
                    public void accept(Status status) throws Exception {
//                        toast("Geofences removed");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        toast("Error removing geofences");
//                        Log.d(TAG, "Error removing geofences", throwable);
                    }
                });
    }

    private PendingIntent createNotificationBroadcastPendingIntent() {
        return PendingIntent.getBroadcast(getContext(), 0, new Intent(getContext(), GeofenceBroadcastReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private GeofencingRequest createGeofencingRequest(double longitude, double latitude, float radius) {
        Geofence geofence = new Geofence.Builder()
                .setRequestId("GEOFENCE")
                .setCircularRegion(latitude, longitude, radius)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
        return new GeofencingRequest.Builder().addGeofence(geofence).build();
    }
    //endregion

    //region Category & SubCategory & CalendarView
    int getBottomOffsetForViewSerchAndDay() {
        if (bottomOffsetForViewSerchAndDay == 0) {
            bottomOffsetForViewSerchAndDay = screenHeight - keyboardHeight - (int) viewSerchAndDay.getY() - viewSerchAndDay.getHeight() - getResources().getDimensionPixelSize(R.dimen.home_margin);
        }
        return bottomOffsetForViewSerchAndDay;
    }

    public void setCategoriesLayout() {
        int showHeight = getBottomOffsetForViewSerchAndDay() + getResources().getDimensionPixelSize(R.dimen.home_location_margin);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) categoriesView.getLayoutParams();
        int lpHeight = getBottomOffsetForViewSerchAndDay() + getResources().getDimensionPixelSize(R.dimen.home_location_margin);
        lp.width = lpHeight > screenWidth ? screenWidth : lpHeight;
        lp.height = lp.width;
        categoriesView.setLayoutParams(lp);
        categoriesView.setX((screenWidth - lp.width) / 2);
        categoriesView.setY((showHeight - lp.height) / 2 + textFieldSearchBar.getHeight() + getResources().getDimensionPixelSize(R.dimen.home_searchbar_margin));

        RelativeLayout.LayoutParams lpSub = (RelativeLayout.LayoutParams) subCategoriesView.getLayoutParams();
        lpSub.width = screenWidth;
        lpSub.height = ((screenWidth - getResources().getDimensionPixelSize(R.dimen.home_subcategory_margin) * 2) * 307) / 342 + getResources().getDimensionPixelSize(R.dimen.home_subcategory_margin) * 2;
        if (lpSub.height > lp.height) {
            lpSub.height = lp.height;
            lpSub.width = ((lpSub.height - getResources().getDimensionPixelSize(R.dimen.home_subcategory_margin) * 2) * 342) / 307 + getResources().getDimensionPixelSize(R.dimen.home_subcategory_margin) * 2;
        }
        subCategoriesView.setLayoutParams(lpSub);
        subCategoriesView.setX((screenWidth - lpSub.width) / 2);
        subCategoriesView.setY((showHeight - lpSub.height) / 2 + textFieldSearchBar.getHeight() + getResources().getDimensionPixelSize(R.dimen.home_searchbar_margin));
    }

    void initCategoriesFilters() {
        categoriesView.setOnOutClickedListener(this);
        categoriesView.setOnItemClickedListener(this);

        categoriesView.setVisibility(View.INVISIBLE);
        categoriesView.setAlpha(kAlphaOff);
        subCategoriesView.setVisibility(View.INVISIBLE);
        subCategoriesView.setAlpha(kAlphaOff);
    }

    // Category, SubCategory outside click
    @Override
    public void onOutClicked() {
        if (subCategoriesView.getVisibility() == View.VISIBLE) {
            textFieldSearchLocation.setFocusable(true);
            textFieldSearchLocation.setFocusableInTouchMode(true);
            textFieldSearchLocation.setCursorVisible(true);

            subCategoriesView.setVisibility(View.INVISIBLE);
            subCategoriesView.setAlpha(kAlphaOff);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(animationWithAllFilters(true, false));
            animatorSet.start();
            return;
        }
        btnClearSearch.setVisibility(View.INVISIBLE);
        viewSerchAndDay.setVisibility(View.INVISIBLE);
        blurView.setVisibility(View.INVISIBLE);
        homeView.requestFocus();
        Utils.hideSoftKeyboard((HomeActivity)context);
    }

    @Override
    public void onCategoryItemClicked(Object object) {
        textFieldSearchLocation.setFocusable(false);
        textFieldSearchLocation.setFocusableInTouchMode(false);
        textFieldSearchLocation.setCursorVisible(false);

        selectedCategory = App.getInstance().dictCategories.get((String) object);
        initSubCategoriesWithCategory();
        animateWheelsWithCategory(Integer.parseInt(selectedCategory.id));
    }

    @Override
    public void OnSubCategoryItemClicked(Object object) {
        BLPSubCategory subCategory = selectedCategory.dictSubCategories.get(String.format("key-%d", (int) object));
        if (subCategory.name.equals("empty")) {
            return;
        }

        searchAreaButton.setVisibility(View.INVISIBLE);
        viewSerchAndDay.setVisibility(View.INVISIBLE);
//        isNeedZoomOut = YES;

        textFieldSearchBar.removeTextChangedListener(searchBarWatcher);
        textFieldSearchBar.setText(subCategory.name);
        btnClearSearch.setVisibility(View.INVISIBLE);
        textFieldSearchBar.addTextChangedListener(searchBarWatcher);

        blurView.setVisibility(View.INVISIBLE);
        homeView.requestFocus();
        Utils.hideSoftKeyboard((HomeActivity)context);

        if (textFieldSearchBar.getText().toString().length() > 0) {
            refreshBlips();
        }
    }

    void initSubCategoriesWithCategory() {
        subCategoriesView.setOnOutClickedListener(this);
        subCategoriesView.setOnItemClickedListener(this);

        int subCatBtnID[] = {R.id.firstCategory, R.id.secondCategory, R.id.thirdCategory, R.id.fourthCategory, R.id.fifthCategory,
                R.id.firstCategorySecond, R.id.secondCategorySecond, R.id.thirdCategorySecond, R.id.fourthCategorySecond, R.id.fifthCategorySecond};

        for (int i = 0; i < subCatBtnID.length; i++) {
            BLPSubCategory subCategory = selectedCategory.dictSubCategories.get(String.format("key-%d", i));
            ImageView imgView = (ImageView) (findViewById(subCatBtnID[i]));

            imgView.setImageDrawable(subCategory.drawableStates);
            imgView.setTag(i);
        }
    }

    public void refreshBlips() {
        mMap.clear();
        mClusterManager.clearItems();
        blips.clear();

        LatLng target = mMap.getCameraPosition().target;
        loadBlipsNearAt(target);
    }

    public void removeAnnotations(List<BLPBlip> blips) {
        for (BLPBlip blp : blips) {
            mClusterManager.removeItem(blp);
        }
    }

    public void setupCalendarView() {
        calendarView.setDateRange(-604800, 2592000);
        calendarView.setOnItemSelectedListener(new HorizontalPicker.OnItemSelected() {
            @Override
            public void onDayPickerItemSelected(int index, Date date) {
                currentCalendarIndex = index;
                currentCalendarDate = date;
                selectedDate = Utils.timestampFromDate(date.getTime(), "yyyy-MM-dd");
            }
        });

        calendarView.setOnTouchedListener(new HorizontalPicker.OnTouched() {
            @Override
            public void onDayPickerTouched() {
                if (calendarView.getWidth() > screenWidth / 2) {
                    return;
                }
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animationsTextFieldLocationEdit(false, false));
                animatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (currentCalendarDate != null) {
                            calendarView.setSelectedItem(currentCalendarIndex);
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
            }
        });

//        _dateFormatterForCell = [[NSDateFormatter alloc] init];
//    [_dateFormatterForCell setDateFormat:@"EEE dd"];
//
//        _dateFormatter = [[NSDateFormatter alloc] init];
//    [_dateFormatter setDateFormat:@"yyyy-MM-dd"];
//
        selectedDate = Utils.timestampFromDate(new Date().getTime(), "yyyy-MM-dd");
    }
    //endregion

    //region Animations
    public void animateWheelsWithCategory(int categoryId) {
        float originX = subCategoriesView.getX();
        float originY = subCategoriesView.getY();

        switch (categoryId - 1) {
            case 0:
                subCategoriesView.setX(-screenWidth);
                subCategoriesView.setTranslationY(-categoriesView.getHeight());
                break;
            case 1:
                subCategoriesView.setX(screenWidth);
                subCategoriesView.setTranslationY(-categoriesView.getHeight());
                break;
            case 2:
                subCategoriesView.setX(screenWidth);
                subCategoriesView.setTranslationY(categoriesView.getHeight());
                break;
            case 3:
                subCategoriesView.setX(-screenWidth);
                subCategoriesView.setTranslationY(categoriesView.getHeight());
                break;

            default:
                break;
        }

        categoriesView.setVisibility(View.INVISIBLE);
        subCategoriesView.setVisibility(View.VISIBLE);
        subCategoriesView.setAlpha(kAlphaOn);

        List<Animator> animatorList = new ArrayList<>();

        animatorList.add(ViewPropertyObjectAnimator
                .animate(subCategoriesView)
                .withLayer()
                .x(originX)
                .y(originY)
                .setDuration(kBLPSubCategoryDuration)
                .get());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new OvershootInterpolator(2.5f));
        animatorSet.playTogether(animatorList);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                setUserTouchEnable(false);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setUserTouchEnable(true);
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

    public void setUserTouchEnable(boolean isSet) {
        if (isSet) {
            ((HomeActivity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            ((HomeActivity)context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    public List<Animator> animationWithAllFilters(boolean allFilters, boolean hide) {
        if (hide && !allFilters) {
            List<Animator> animatorList = new ArrayList<>();

            if (categoriesView.getVisibility() == View.VISIBLE) {
                animatorList.add(ViewPropertyObjectAnimator
                        .animate(categoriesView)
                        .withLayer()
                        .alpha(kAlphaOff)
                        .setInterpolator(new DecelerateInterpolator())
                        .setDuration(kBLPCategoryDuration)
                        .addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {
                                setUserTouchEnable(false);
                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                categoriesView.setVisibility(View.INVISIBLE);
                                setUserTouchEnable(true);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {
                            }
                        })
                        .get());
            } else if (subCategoriesView.getVisibility() == View.VISIBLE) {
                animatorList.add(ViewPropertyObjectAnimator
                        .animate(subCategoriesView)
                        .withLayer()
                        .alpha(kAlphaOff)
                        .setInterpolator(new DecelerateInterpolator())
                        .setDuration(kBLPCategoryDuration)
                        .addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {
                                setUserTouchEnable(false);
                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                subCategoriesView.setVisibility(View.INVISIBLE);
                                setUserTouchEnable(true);
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {
                            }
                        })
                        .get());
            }

            animatorList.add(ViewPropertyObjectAnimator
                    .animate(blurView)
                    .withLayer()
                    .alpha(kAlphaOff)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(kBLPCategoryDuration)
                    .addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            setUserTouchEnable(false);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            setUserTouchEnable(true);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    })
                    .get());

            if (categoriesView.getVisibility() == View.VISIBLE || subCategoriesView.getVisibility() == View.VISIBLE) {
                animatorList.add(ViewPropertyObjectAnimator
                        .animate(viewSerchAndDay)
                        .withLayer()
                        .y(viewSerchAndDay.getY() - getBottomOffsetForViewSerchAndDay())
                        .setInterpolator(new DecelerateInterpolator())
                        .setDuration(kBLPCategoryDuration)
                        .get());
            }

            return animatorList;

        } else if (!allFilters && !hide) {
        } else if (allFilters && !hide) {
            if (blurView.getAlpha() == kAlphaOff) {
                mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        blurView.setAlpha((float) kAlphaOn);
                        blurView.setVisibility(View.VISIBLE);
                        blurView.setImageBitmap(Utils.fastblur(bitmap, 0.1f, 1));
                    }
                });
            }

            categoriesView.setVisibility(View.VISIBLE);
            categoriesView.setAlpha(kAlphaOff);
            viewSerchAndDay.setVisibility(View.VISIBLE);

            List<Animator> animatorList = new ArrayList<>();

            animatorList.add(ViewPropertyObjectAnimator
                    .animate(categoriesView)
                    .withLayer()
                    .alpha(kAlphaOn)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(kBLPCategoryDuration)
                    .get());

            if (blurView.getAlpha() == kAlphaOff) {
                animatorList.add(ViewPropertyObjectAnimator
                        .animate(viewSerchAndDay)
                        .withLayer()
                        .y(viewSerchAndDay.getY() + getBottomOffsetForViewSerchAndDay())
                        .alpha(kAlphaOn)
                        .setInterpolator(new DecelerateInterpolator())
                        .setDuration(kBLPCategoryDuration)
                        .get());
            }

            return animatorList;
        }
        return new ArrayList<>();
    }

    public List<Animator> animationsTextFieldLocationEdit(boolean value, boolean defaultValue) {
        if (defaultValue) {
            textFieldSearchLocation.setTextColor(ContextCompat.getColor((HomeActivity)context, android.R.color.black));
            btnClearLocation.setVisibility(View.VISIBLE);

            List<Animator> animatorList = new ArrayList<>();

            animatorList.add(ViewPropertyObjectAnimator
                    .animate(layTextFieldSearchLocation)
                    .withLayer()
                    .width(screenWidth / 2 - 14)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(kBLPTextLocationDuration)
                    .get());

            animatorList.add(ViewPropertyObjectAnimator
                    .animate(calendarViewContainer)
                    .withLayer()
                    .x(screenWidth / 2)
                    .width(screenWidth / 2 - 14)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(kBLPTextLocationDuration)
                    .get());

            return animatorList;
        }

        if (value) {
            textFieldSearchLocation.setTextColor(ContextCompat.getColor((HomeActivity)context, android.R.color.black));
            btnClearLocation.setVisibility(View.VISIBLE);

            List<Animator> animatorList = new ArrayList<>();

            animatorList.add(ViewPropertyObjectAnimator
                    .animate(layTextFieldSearchLocation)
                    .withLayer()
                    .width(3 * screenWidth / 4 - 7)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(kBLPTextLocationDuration)
                    .get());

            animatorList.add(ViewPropertyObjectAnimator
                    .animate(calendarViewContainer)
                    .withLayer()
                    .x(3 * screenWidth / 4)
                    .width(screenWidth / 4 - 21)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(kBLPTextLocationDuration)
                    .get());

            return animatorList;
        } else {
            textFieldSearchLocation.setTextColor(ContextCompat.getColor((HomeActivity)context, android.R.color.transparent));
            btnClearLocation.setVisibility(View.INVISIBLE);

            List<Animator> animatorList = new ArrayList<>();

            animatorList.add(ViewPropertyObjectAnimator
                    .animate(layTextFieldSearchLocation)
                    .withLayer()
                    .width(screenWidth / 6 - 7)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(kBLPTextLocationDuration)
                    .get());

            animatorList.add(ViewPropertyObjectAnimator
                    .animate(calendarViewContainer)
                    .withLayer()
                    .x(screenWidth / 6)
                    .width(5 * screenWidth / 6 - 21)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(kBLPTextLocationDuration)
                    .get());

            return animatorList;
        }
    }
    //endregion

    public void loadBlipsNearAt(final LatLng coords) {
//        if (calloutView){
//            [self dismissProgressHUD];
//            return;
//        }

//        if (searchAreaButton.alpha == 1)
//            return;
//
//        [visitedCoordsArray addObject:coords];
//        if (visitedCoordsArray.count>2)
//        [ visitedCoordsArray removeObjectAtIndex:0];
        if (!isloadingBlips) {
            isloadingBlips = true;
            searchbarSideLogo.stopAnimation();
            searchbarSideLogo.startAnimation();
        } else {
            return;
        }

        App.getInstance().apiManager.blipsNearbyWithLatitude(coords.latitude,
                coords.longitude,
                0,
                10,
                Utils.getMapRadius(mMap),
                textFieldSearchBar.getText().toString(),
                selectedDate,
                new Callback<BlipModel>() {
                    @Override
                    public void onResponse(Call<BlipModel> call, Response<BlipModel> response) {
                        BlipModel model = response.body();
                        for (BLPBlip blip : model.data) {
                            mClusterManager.addItem(blip);
                        }
                        mClusterManager.cluster();

//                [self dismissProgressHUD];

                        List<BLPBlip> pointsArray = response.body().data;
                        if (pointsArray.size() > 0) {
                            refreshMapWithBlips(pointsArray, false);
                            isloadingBlips = false;
                            searchbarSideLogo.stopAnimation();
                        } else {
                            if (textFieldSearchBar.getText().toString().length() > 0) {
//                        [self showErrorWithStatus:@"Sorry, no blips match your search. Try something different."];
                            }

                            isloadingBlips = false;
                            searchbarSideLogo.stopAnimation();
                        }

//                DDLogInfo(@"Success getting Blips nearby, %lu Blips retrieved - %s", (unsigned long)pointsArray.count, __PRETTY_FUNCTION__);
                    }

                    @Override
                    public void onFailure(Call<BlipModel> call, Throwable t) {
                        visitedCoordsArray.remove(coords);
                        searchbarSideLogo.stopAnimation();
                        isloadingBlips = false;
//                [self dismissProgressHUD];
//                if ([[error messageForAlertView] isEqualToString:@"The Internet connection appears to be offline."]) {
//
//                                                    [self showErrorWithStatus:[error messageForAlertView]];
//
//                }
                    }
                });
    }

    public void fetchSearchResults() {
        if (!selectedLocation.equals(kBLPCurrentLocationText)) {
            refreshBlips();
        } else {
            Utils.showLoading((HomeActivity)context);
//            [self fetchSearchResultsWithCoordinates:self.locationPlacemark.location.coordinate];
            fetchSearchResultsWithCoordinates(searchCoordinates);
        }
    }

    public void fetchSearchResultsWithCoordinates(LatLng coordinates) {
        App.getInstance().apiManager.blipsNearbyWithLatitude(coordinates.latitude,
                coordinates.longitude,
                0,
                10,
                0,
                textFieldSearchBar.getText().toString(),
                selectedDate,
                new Callback<BlipModel>() {
                    @Override
                    public void onResponse(Call<BlipModel> call, Response<BlipModel> response) {
                        Utils.hideLoading();
                        hideCallout();

                        List<BLPBlip> blipsArray = response.body().data;


                        if (blipsArray.size() > 0) {
                            if (textFieldSearchBar.getText().toString().length() > 0) {
                                refreshMapWithBlips(blipsArray, true);
                            } else {
                                refreshMapWithBlips(blipsArray, false);
                            }
                        } else {
//                            [self showErrorWithStatus:@"Sorry, no blips match your search. Try something different."];
                        }
                    }

                    @Override
                    public void onFailure(Call<BlipModel> call, Throwable t) {
                        Utils.hideLoading();
                    }
                });
    }

    public void refreshMapWithBlips(List<BLPBlip> blips, boolean showFirstBlip) {
        if (showFirstBlip) {
            LatLng coords = new LatLng(blips.get(0).getCoordinate().latitude, blips.get(0).getCoordinate().longitude);

//            MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(
//                    coords,
//                    kBLPRegionMetersPerMile,
//                    kBLPRegionMetersPerMile
//            );
//            if ([self isAllBlipsFromGoogle:blips]) {
//
//            [self showAllAnotations];
//
//            } else
//            [self.mapView setRegion:region animated:YES];
        }

        ///Remove existing blips
        ArrayList<BLPBlip> blipsToAdd = new ArrayList<>(blips);
        ArrayList<BLPBlip> arrayToremove = new ArrayList<>();

        ArrayList<BLPBlip> blipArrayCopy = new ArrayList<>(this.blips);
        ArrayList<BLPBlip> reversedBlipsArray = new ArrayList<>();
        for (int index = blipArrayCopy.size() - 1; index >= 0; index--) {
            reversedBlipsArray.add(blipArrayCopy.get(index));
        }

        for (BLPBlip exitingBlip : reversedBlipsArray) {
            int count = 0;

            for (BLPBlip blip : blips) {
                if (exitingBlip.getBlipId().equals(blip.getBlipId()) || (exitingBlip.getPointId() != null && exitingBlip.getPointId().equals(blip.getPointId()))) {
                    blipsToAdd.remove(blip);
                    count++;
                }
            }

            if (count == 0) {
                this.blips.remove(exitingBlip);
                arrayToremove.add(exitingBlip);
            }
        }

        if (!blipsToAdd.contains(selectedBlip) && selectedBlip != null && !this.blips.contains(selectedBlip)
//                && !mapClusterController annotations] allObjects] containsObject:self.selectedBlip]) {
                ) {
            blipsToAdd.add(selectedBlip);
        }

        if (arrayToremove.contains(selectedBlip)) {
            arrayToremove.remove(selectedBlip);
        }

        for (BLPBlip blp : arrayToremove) {
            mClusterManager.removeItem(blp);
        }

//    [WSELF.mapClusterController removeAnnotations:arrayToremove withCompletionHandler:^{
//
//        [WSELF.mapView removeAnnotations:arrayToremove];
//
//#ifdef BLIPIC
//#else
//        [WSELF removeOverlaysIfNeed];
//#endif
        ArrayList<BLPBlip> newArrayBlips = addBlips(blipsToAdd);

        mClusterManager.addItems(newArrayBlips);

        this.blips.addAll(newArrayBlips);

        sortedArrayAsSeverSent(blips);


        List<BLPBlip> aryDeletedItems = deleteBlipsIfNeed();
        for (BLPBlip blp : aryDeletedItems) {
            mClusterManager.removeItem(blp);
        }

        geoFacing();
//
//#ifdef BLIPIC
//#else
//                                    [WSELF removeOverlaysIfNeed];
//#endif
//
//        if (blipToShow != null) {
//            isCanShowBlip = true;
//            [WSELF openBlip:WSELF.blipToShow];
//            isCanShowBlip = false;
//        }
        getAnotationsInVisibleMapRectangle();
    }

    public void geoFacing() {
        clearGeofence();
        addGeofence(blips);
    }

    public void sortedArrayAsSeverSent(List<BLPBlip> blips) {
        ArrayList<BLPBlip> currentArray = new ArrayList<>();

        for (BLPBlip serverBlip : blips) {
            for (BLPBlip blipOnMap : this.blips) {
                if (serverBlip.getBlipId().equals(blipOnMap.getBlipId())) {
                    currentArray.add(blipOnMap);
                }
            }
        }

        this.blips = currentArray;
    }

    public List<BLPBlip> deleteBlipsIfNeed() {
        int countPoints = (int) (mClusterManager.getClusterMarkerCollection().getMarkers().size() - 11 + (isPresentUserLocation() ? 1 : 0));
        ArrayList<BLPBlip> needDeleteBlips = new ArrayList<>();

        for (int i = 0; i < countPoints; i++) {
            if (blips.size() > 1) {
                needDeleteBlips.add(blips.get(blips.size() - 1));
                blips.remove(blips.get(blips.size() - 1));

                for (int index = blips.size() - 1; index >= 0; index--) {
                    BLPBlip currentBlip = blips.get(index);

                    if (isInRadiusPoint(blips.get(blips.size() - 1), currentBlip)) {
                        needDeleteBlips.add(currentBlip);
                        blips.remove(currentBlip);
                    }
                }
            } else if (blips.size() == 1) {
                blips.add(blips.get(blips.size() - 1));
                needDeleteBlips.remove(blips.get(blips.size() - 1));
            }
        }

        return needDeleteBlips;
    }

    public ArrayList<BLPBlip> addBlips(List<BLPBlip> blipsToAdd) {
        int countPoints = 10 - mClusterManager.getClusterMarkerCollection().getMarkers().size() + (isPresentUserLocation() ? 1 : 0);
        ArrayList<BLPBlip> needAddBlips = new ArrayList<>();

        for (int i = 0; i < countPoints; i++) {
            if (blipsToAdd.size() > 1) {
                needAddBlips.add(blipsToAdd.get(0));
                blipsToAdd.remove(blipsToAdd.get(0));
                for (int index = 0; index < blipsToAdd.size(); index++) {
                    if (isInRadiusPoint(needAddBlips.get(needAddBlips.size() - 1), blipsToAdd.get(index))) {
                        needAddBlips.add(blipsToAdd.get(index));
                        blipsToAdd.remove(blipsToAdd.get(index));
                    }
                }
            } else if (blipsToAdd.size() == 1) {
                needAddBlips.add(blipsToAdd.get(0));
                blipsToAdd.remove(blipsToAdd.get(0));
            }
        }
        return needAddBlips;
    }

    public boolean isInRadiusPoint(BLPBlip firstBlip, BLPBlip secondBlip) {
//        float[] distance = new float[2];
//        Location.distanceBetween(firstBlip.location.getLat(), firstBlip.location.getLon(),
//                secondBlip.location.getLat(), secondBlip.location.getLon(), distance);
//        if (distance[0] < mMarkerSize) {
//            return true;
//        } else {
//            return false;
//        }
        return false;
    }

    public boolean isPresentUserLocation() {
//        for (id<MKAnnotation> annotation in [self.mapView annotations]) {
//            if ([annotation isKindOfClass:[MKUserLocation class]])
//            return true;
//        }
//
//        return false;
        return false;
    }

    public void getAnotationsInVisibleMapRectangle() {
//        NSSet *annotationSet = [self.mapView annotationsInMapRect:self.mapView.visibleMapRect];
//        NSArray *array = [annotationSet allObjects];
//        NSMutableArray* annotationArray = [NSMutableArray new];
//        for (id object in array) {
//            if (![object isKindOfClass:[MKUserLocation class]])
//                [annotationArray addObject:object];
//        }
//        BLPBlip* blip = [self.blips firstObject];
//        if (annotationArray.count==0 && [self isInRadius:50000] && self.textFieldSearchBar.text.length>0) {
//            if (self.isNeedZoomOut)
//                [self showAllAnotations];
//
//        }
//        else if (annotationArray.count==0 && ![self isInRadius:50000] && self.textFieldSearchBar.text.length>0) {
//
//
//            CLLocationCoordinate2D coords = CLLocationCoordinate2DMake(
//                    [blip coordinate].latitude,
//                                                                       [blip coordinate].longitude
//                                                                       );
//
//            MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(
//                    coords,
//                    kBLPRegionMetersPerMile,
//                    kBLPRegionMetersPerMile
//            );
//            [self.mapView setRegion:region animated:YES];
//
//        }
//        self.isNeedZoomOut = NO;
        if (blips.size() == 0) {
            return;
        }
        BLPBlip blip = blips.get(0);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                blip.getCoordinate(), DEFAULT_ZOOM));
    }

    public void searchBlipsWithProgresHUD(boolean showProgressHUD) {
        if (showProgressHUD) {
//            Utils.showLoading(getActivity());
        }

//        MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(
//                coordinates,
//                kBLPRegionMetersPerMile,
//                kBLPRegionMetersPerMile
//        );
//                                       [self.mapView setRegion:region animated:YES];

        fetchSearchResultsWithCoordinates(searchCoordinates);
    }

    public BLPBlip findBlipInBlipsArray(BLPBlip blip) {
        for (BLPBlip existedBlip : blips) {
            if (existedBlip.getBlipId().equals(blip.getBlipId()) || existedBlip.getPointId().equals(blip.getPointId())) {
                return existedBlip;
            }
        }
        return null;
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason != GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) {
            hideCallout();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        hideCallout();
    }

//#
//    pragma mark
//    Location Manager
//    Delegate
//
//-(void)managerDidUpdateWithNewLocation:(CLLocation *)newLocation
//
//    {
//        NSTimeInterval locationAge = -[newLocation.timestamp timeIntervalSincefalsew];
//
//        if (locationAge > 10.0) {
//
//            return;
//
//        }
//
//        if (newLocation.horizontalAccuracy < 0) {
//
//            return;
//
//        }
//
//        if (bestEffortAtLocation == nil || bestEffortAtLocation.horizontalAccuracy > newLocation.horizontalAccuracy) {
//
//            bestEffortAtLocation = newLocation;
//
//            MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(bestEffortAtLocation.coordinate,
//                    kBLPRegionMetersPerMile,
//                    kBLPRegionMetersPerMile);
//
//        [mapView setRegion:region animated:true];
//            locationButton.hidden = false;
//
//            //[[BLPGeocoderController sharedInstance] reverseGeocodeLocationWithLocation:bestEffortAtLocation];
//
//            //        dispatch_after(dispatch_time(DISPATCH_TIME_falseW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//            //            [self refreshBlips];
//            //        });
//        }
//    }
//

}
