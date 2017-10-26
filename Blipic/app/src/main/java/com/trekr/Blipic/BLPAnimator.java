package com.trekr.Blipic;

import android.app.Activity;
import android.content.Intent;

//TODO: BLPAnimator
public class BLPAnimator {

//    public static void animateToHomeViewController() {
//
////    #ifdef BLIPIC
////
////    NSString *className = NSStringFromClass([BLPHomeViewController class]);
////
////    [BLPAnimator animateWithRootViewController:[REFrostedViewController frostedWithContentViewOfKindClass:className]];
////    #else
////
////    #endif
//    }
//
    public static void animateWithRootViewController (Activity prev, Class next) {
        Intent i = new Intent(prev, next);
        prev.startActivity(i);
        prev.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        prev.finish();
    }
//
//    public void animateCrossDissolveFromViewController(Activity :(UIViewController *)fromViewController withAnimationBlock:(void(^)(void))block
//    {
//    [UIView transitionWithView:fromViewController.view
//    duration:0.4f
//    options:UIViewAnimationOptionTransitionCrossDissolve
//    animations:block
//    completion:nil
//    ];
//    }

}
