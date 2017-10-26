package com.trekr;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

import com.crashlytics.android.Crashlytics;
import com.trekr.ApiService.ApiManager;
import com.trekr.ApiService.ApiModel.BLPBlip;
import com.trekr.ApiService.ApiModel.BLPUser;
import com.trekr.Blipic.Models.BLPCategory;
import com.trekr.Blipic.Models.BLPSubCategory;
import com.trekr.BlipicWellness.Managers.SessionManager;
import com.trekr.Utils.Constants;

import io.fabric.sdk.android.Fabric;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {
    private static App instance;
    public static App getInstance() {
        return instance;
    }

    public ApiManager apiManager;
    public SharedPreferences settings;
    public SharedPreferences.Editor editor;

    // App Logic
    public BLPUser loggedUser;
    public String token;
    public Map<String, BLPCategory> dictCategories =  new HashMap<String, BLPCategory>();
    public Map<String, BLPSubCategory> dictSubCategories =  new HashMap<String, BLPSubCategory>();
    public BLPBlip selectedBlip;

    // App UI
    public int screenHeight;
    public int screenWidth;
    public String activityType;

    // Temp Values
    public String post_comment = "";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        instance = this;

        init();
    }

    public void init() {
        apiManager = new ApiManager();
        settings = getSharedPreferences(Constants.APP_NAME, 0);
        editor = settings.edit();

        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;


        dictCategories.put("key-0", new BLPCategory("1", "mountain", "cg_mountain", "wall_mountain"));
        dictCategories.put("key-1", new BLPCategory("2", "snow", "cg_snow", "wall_snow"));
        dictCategories.put("key-2", new BLPCategory("3", "wheels", "cg_wheels", "wall_wheels"));
        dictCategories.put("key-3", new BLPCategory("4", "water", "cg_water", "wall_water"));

    }

    public void selectActivityType(String s) {
        activityType = s;
    }

    //TODO: AppDelegate methods
//    private void initApp() {
//        [BLPLoggingController   kickoff];
//        [BLPAnalyticsController kickoff];
//
//#ifdef BLIPIC
//#else
//
//    [SessionManager configureMagicalRecords];
//
//
//#endif
//
//                [Fabric with:@[[Twitter class], [Crashlytics class]]];
//
//
//    [[UIApplication sharedApplication] setStatusBarHidden:NO];
//    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent];
//
//        self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
//
//        if ([BLPRestAPI sharedInstance].isLoggedIn &&
//        [BLPRestAPI sharedInstance].loggedUser) { // && [FBSDKAccessToken currentAccessToken].tokenString.length > 0) {
//
//        [self showHome];
//
//        } else {
//
//        [self showLogin];
//        }
//
//        if (![[NSUserDefaults standardUserDefaults] boolForKey:@"HasLaunchedOnce"])
//        {
//        [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"HasLaunchedOnce"];
//        [[NSUserDefaults standardUserDefaults] synchronize];
//        [[NSUserDefaults standardUserDefaults] setValue:@"hybrid" forKey:@"mapView"];
//        [[NSUserDefaults standardUserDefaults] setValue:@"public" forKey:@"viewProfile"];
//        }
//
//    [self.window makeKeyAndVisible];
//
//        return [[FBSDKApplicationDelegate sharedInstance] application:application
//        didFinishLaunchingWithOptions:launchOptions];
//    }


    //region Rotation rules

//- (UIInterfaceOrientationMask)application:(UIApplication *)application supportedInterfaceOrientationsForWindow:(nullable UIWindow *)window {
//
//        if (self.allowRotation) {
//            return UIInterfaceOrientationMaskAll;
//        } else {
//            return UIInterfaceOrientationMaskPortrait;
//        }

//    }

    //endregion


///**
// *  Shows the welcome screen with options to login and register.
// */
//- (void)showLogin
//    {
//        UIStoryboard *mystoryboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
//
//        BLPLoginViewController *loginViewController = [mystoryboard instantiateViewControllerWithIdentifier:@"BLPLoginViewController"];
//
//        self.window.rootViewController = loginViewController;
//
//    [BLPNewUserManager deleteNeedsForInputUserProfile];
//    }


    public void showHome(Activity prev) {
//    [self startNotifyLocationManager];

//#ifdef BLIPIC

//        BLPHomeViewController *homeController               = [BLPHomeViewController new];
//
//    [homeController view];
//
//        BLPNavigationController *navigationController       = [[BLPNavigationController alloc] initWithRootViewController:homeController];
//
//        BLPSlideMenuViewController *slideMenuViewController = [[BLPSlideMenuViewController sharedInstance] initWithLoadedController:navigationController];
//
//        self.frostedViewController = [[REFrostedViewController alloc] initWithContentViewController:navigationController
//        menuViewController:slideMenuViewController];
//
//        self.frostedViewController.direction                 = REFrostedViewControllerDirectionLeft;
//        self.frostedViewController.limitMenuViewSize         = YES;
//        self.frostedViewController.liveBlur                  = NO;
//        self.frostedViewController.blurRadius                = 4.0f;
//        self.frostedViewController.blurSaturationDeltaFactor = 4.0f;
//        self.frostedViewController.blurTintColor             = [UIColor slideMenuColor];
//
//        self.frostedViewController.menuViewSize              = CGSizeMake(self.frostedViewController.view.bounds.size.width - 65.0f,
//                self.frostedViewController.view.bounds.size.height);
//
//        self.window.rootViewController = self.frostedViewController;
//
//    [BLPAnimator animateWithRootViewController:self.frostedViewController];

//#else
        SessionManager.shared().showWellnessHome(prev);
//    [self clearGeofencingRegions];
//
//
//    [SessionManager registerLocalNotificationsWithApplication: [UIApplication sharedApplication]];
//
//#endif
    }

//#pragma mark - Get ViewController from MainStoryboard
//
//+ (UIViewController *)getViewControllerFromStoryBoardWithIdentifier:(NSString *)identifier {
//
//        UIStoryboard *mystoryboard = [UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil];
//
//        UIViewController *viewController = [mystoryboard instantiateViewControllerWithIdentifier:identifier];
//
//        return viewController;
//    }
//
//#pragma mark - Delegate methods for Facebook SDK Integration
//
//- (void)applicationDidBecomeActive:(UIApplication *)application
//    {
//#ifdef BLIPIC
//#else
//
//    [SessionManager showNotSyncGeotifications];
//
//#endif
//
//            [FBSDKAppEvents activateApp];
//
//    [[BLPAdditionalServerAction sharedInstance] checkVersionFailureBlock:^(BLPServerError *error) {
//
//    } successBlock:^(BOOL isVersionSupport) {
//
//        if (!isVersionSupport) {
//
//            UIViewController* updateVersionViewController = [BLPAppDelegate getViewControllerFromStoryBoardWithIdentifier:@"BLPUpdateVersionViewController"];
//
//            self.window.rootViewController = updateVersionViewController;
//        }
//
//    }];
//    }
//
//- (BOOL)application:(UIApplication *)application
//    openURL:(NSURL *)url
//    sourceApplication:(NSString *)sourceApplication
//    annotation:(id)annotation
//    {
//        return [[FBSDKApplicationDelegate sharedInstance] application:application
//        openURL:url
//        sourceApplication:sourceApplication
//        annotation:annotation];
//    }
//
//#pragma mark - UIApplicationDelegate
//
//- (void)application:(UIApplication *)application willChangeStatusBarFrame:(CGRect)newStatusBarFrame {
//
//        NSMutableDictionary *dict = [[NSMutableDictionary alloc] initWithObjectsAndKeys:[NSNumber numberWithFloat:CGRectGetHeight(newStatusBarFrame)], BLPAppDelegateStatusBarUserInfoKey, nil];
//
//    [[NSNotificationCenter defaultCenter] postNotificationName:BLPAppDelegateStatusBarWillChangeNotification
//        object:self
//        userInfo:dict];
//
//    }
//
//}



}