package com.trekr.BlipicWellness.Managers;

import android.app.Activity;

import com.trekr.Blipic.BLPAnimator;
import com.trekr.BlipicWellness.Controllers.HomeActivity;

//TODO: SessionManager
public class SessionManager {
    private static SessionManager instance;

    public static SessionManager shared() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

//        var dashboard = EmployeeDashboardViewController()
//
    public void showWellnessHome(Activity prev) {


//    let menuController = MenuViewController(dashboard: dashboard)
//
//    let slideMenuController = SlideMenuController(mainViewController: dashboard,
//    leftMenuViewController: menuController)
//
//    SlideMenuOptions.contentViewScale = 1
//
        BLPAnimator.animateWithRootViewController(prev, HomeActivity.class);
    }
//
//    func showDiscoveryHome() {
//
//    let menuController = MenuViewController(dashboard: dashboard)
//
//    let slideMenuController = SlideMenuController(mainViewController: menuController.controllers[0]!,
//    leftMenuViewController: menuController)
//
//    SlideMenuOptions.contentViewScale = 1
//
//    BLPAnimator.animate(withRootViewController: slideMenuController)
//    }
//
//static func enterActivationCode(handler: @escaping (String) -> Void) {
//
//    var textField: UITextField?
//
//    let okAction = UIAlertAction.init(title: "Ok", style: .default) { (_) in
//
//    handler((textField?.text)!)
//    }
//
//    UIWindow.visibleViewController()?.showAlertController(title: "Enter Activation Code",
//    cancelButtonAction: .cancel {  },
//    actions: [okAction],
//    textFieldsConfigurationHandlers: [{ field in
//
//    textField = field
//
//    }])
//    }
//
//static func configureMagicalRecords() {
//
//    MagicalRecord.enableShorthandMethods()
//    MagicalRecord.setupCoreDataStack(withStoreNamed: "BlipicWellness")
//    MagicalRecord.setupCoreDataStack(withAutoMigratingSqliteStoreNamed: "BlipicWellness")
//    }
//
//static func registerLocalNotifications(application: UIApplication) {
//
//    let settings = UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
//    application.registerUserNotificationSettings(settings)
//
//    }
//
//static func handlerGeofence(geotification: Geotification, type: EventType) {
//
//    if type == EventType.onEntry {
//
//    let notification = UILocalNotification()
//    notification.alertBody = "You entered to \(geotification.blipName ?? "") region"
//    notification.soundName = "Default"
//    UIApplication.shared.presentLocalNotificationNow(notification)
//
//    if UIApplication.shared.applicationState == .active {
//
//    SessionManager.showGeficationDetails(geotification: geotification)
//
//    }
//    } else {
//
//    let notification = UILocalNotification()
//    notification.alertBody = "You exited from \(geotification.blipName ?? "") region"
//    notification.soundName = "Default"
//    UIApplication.shared.presentLocalNotificationNow(notification)
//
//    if UIApplication.shared.applicationState == .active {
//
//    geotification.sendVisites()
//
//    }
//    }
//    }
//
//static func geotification(fromRegionIdentifier identifier: String,
//    eventType: EventType,
//    handler: @escaping (Geotification) -> Void) {
//
//    guard let geotification = Geotification.mr_findFirst(with: NSPredicate(format: "identifier == %@", identifier))
//    else { return }
//
//    geotification.visitedBlip(typeEvent: eventType) {
//
//    handler(geotification)
//    }
//    }
//
//static func showNotSyncGeotifications() {
//
//    guard let geotifications = Geotification.notSyncGeotification()
//    else { return }
//
//    showDetailsGeotifications(geotifications: geotifications,
//    position: 0)
//
//    for geotification in geotifications {
//
//    geotification.sendVisites()
//    }
//    }
//
//static func showDetailsGeotifications(geotifications: [Geotification], position: Int) {
//
//    if position < geotifications.count {
//
//    showGeficationDetails(geotification: geotifications[position]) {
//
//    showDetailsGeotifications(geotifications: geotifications,
//    position: position + 1)
//    }
//
//    }
//
//    }
//
//static func showGeficationDetails(geotification: Geotification,
//    closeHandler: (() -> Void)? = .none) {
//
//    if geotification.showed {
//
//    } else {
//
//    BLPBlipServerAction.sharedInstance().getMergeBlip(withPointId: geotification.identifier,
//    failureBlock: { (error) in
//
//    }) { (blips) in
//
//    guard let blipDict = blips?.first as? [AnyHashable : Any],
//    let blip = DataBlip(jsonDictionary: blipDict)
//    else {
//    return
//    }
//
//    let detailViewController = GeofecingDetailsViewController(blip: blip)
//
//    detailViewController.handlerHide = {
//
//    geotification.change(toShowed: true)
//
//    if let handler = closeHandler {
//
//    handler()
//    }
//    }
//    SRMModalViewController.sharedInstance().enableTapOutsideToDismiss = false
//    SRMModalViewController.sharedInstance().showView(with: detailViewController)
//    }
//    }
//    }
}