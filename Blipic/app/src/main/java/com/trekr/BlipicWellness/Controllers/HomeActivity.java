package com.trekr.BlipicWellness.Controllers;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.SupportMapFragment;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.trekr.App;
import com.trekr.Blipic.Controllers.Activities.CreateActivityFragment;
import com.trekr.Blipic.Controllers.Activities.MyStuffFragment;
import com.trekr.Blipic.Controllers.Activities.NotificationsFragment;
import com.trekr.Blipic.Controllers.Activities.SettingsFragment;
import com.trekr.BlipicWellness.Controllers.Dashboard.EmployeeDashboardFragment;
import com.trekr.BlipicWellness.Controllers.Discover.DiscoverFragment;
import com.trekr.Common.GlideApp;
import com.trekr.R;
import com.trekr.Utils.TapOpacityHighlightLayout;
import com.trekr.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    public interface OnBackPressedListener {
        void doBack();
    }

    @BindView(R.id.status_bar)
    View statusBar;
    @BindView(R.id.btn_tab_quick_access)
    TapOpacityHighlightLayout btnTabQuickAccess;
    //save our header or mDrawer
    private AccountHeader drawerHeader = null;
    private Drawer mDrawer = null;
    private CircleImageView img_profile;
    private String CURRENT_TAG;

    public OnBackPressedListener OnBackPressedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        drawerHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withAccountHeader(R.layout.sidemenu_nav_header)
                .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                    @Override
                    public boolean onClick(View view, IProfile profile) {
                        loadFragment(0);
                        mDrawer.deselect();
                        mDrawer.closeDrawer();
                        return true;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withFullscreen(true)
                .withAccountHeader(drawerHeader)
                .withHeaderHeight(DimenHolder.fromDp(230))
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.nav_discover).withIcon(R.drawable.sm_activities).withIdentifier(1),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.nav_create_activity).withIcon(R.drawable.sm_activities).withIdentifier(2),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.nav_stuff).withIcon(R.drawable.sm_stuff).withIdentifier(3),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.nav_notification).withIcon(R.drawable.sm_notification).withIdentifier(4)
                )
                .addStickyDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.nav_settings).withIcon(R.drawable.sm_settings).withIdentifier(5)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        loadFragment((int) drawerItem.getIdentifier());
                        return false;
                    }
                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        btnTabQuickAccess.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        btnTabQuickAccess.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        Utils.hideSoftKeyboard(HomeActivity.this);
                    }
                })
                .withSelectedItem(-1)
                .withSavedInstance(savedInstanceState)
                .build();
        mDrawer.getStickyFooter().setPadding(0,0,0,0);

        if (Build.VERSION.SDK_INT >= 19) {
            mDrawer.getDrawerLayout().setFitsSystemWindows(false);
        }

        if (savedInstanceState == null) {
            CURRENT_TAG = getResources().getString(R.string.nav_dashboard);
            Fragment f = new EmployeeDashboardFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.base_content, f).commit();
        }

        initControls();
    }

    public void initControls() {
        img_profile = (CircleImageView) drawerHeader.getView().findViewById(R.id.profile_image);
        GlideApp
                .with(this)
                .load(App.getInstance().loggedUser.profilePhoto.url)
                .fitCenter()
                .placeholder(R.drawable.empty_profile_photo)
                .into(img_profile);
    }

    public void loadFragment(int id) {
        Fragment fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

        switch (id) {
            case 0:
                CURRENT_TAG = getResources().getString(R.string.nav_dashboard);
//                setStatusBar(true, 0);
                fragment = new EmployeeDashboardFragment();
                break;
            case 1:
                CURRENT_TAG = getResources().getString(R.string.nav_discover);
//                setStatusBar(false, getResources().getColor(android.R.color.black));
                fragment = new DiscoverFragment();

                SupportMapFragment f = ((SupportMapFragment) (getSupportFragmentManager().findFragmentById(R.id.mapView)));
                if (f != null)
                    fragmentTransaction.remove(f);

                break;
            case 2:
                CURRENT_TAG = getResources().getString(R.string.nav_create_activity);
//                setStatusBar(false, getResources().getColor(android.R.color.black));
                fragment = new CreateActivityFragment();
                break;
            case 3:
                CURRENT_TAG = getResources().getString(R.string.nav_stuff);
//                setStatusBar(false, getResources().getColor(android.R.color.black));
                fragment = new MyStuffFragment();
                break;
            case 4:
                CURRENT_TAG = getResources().getString(R.string.nav_notification);
//                setStatusBar(false, getResources().getColor(android.R.color.black));
                fragment = new NotificationsFragment();
                break;
            case 5:
                CURRENT_TAG = getResources().getString(R.string.nav_settings);
//                setStatusBar(false, getResources().getColor(android.R.color.black));
                fragment = new SettingsFragment();
                break;
            default:
//                setStatusBar(true, 0);
                fragment = new EmployeeDashboardFragment();
        }

        fragmentTransaction.replace(R.id.base_content, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void setStatusBar(boolean isHidden, int color) {
        if (isHidden || Build.VERSION.SDK_INT < 19) {
            statusBar.setVisibility(View.GONE);
        } else {
            statusBar.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
            statusBar.setVisibility(View.VISIBLE);
            statusBar.setBackgroundColor(color);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = mDrawer.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = drawerHeader.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (OnBackPressedListener != null) {
            OnBackPressedListener.doBack();
            return;
        }

        if (mDrawer != null && mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.btn_tab_quick_access)
    public void onViewClicked() {
        mDrawer.openDrawer();
    }
}
