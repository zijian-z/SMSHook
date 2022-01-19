package com.zijian.smshook;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookNotificationReply implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if ("com.android.messaging".equals(lpparam.packageName)) {
            XposedBridge.log("hooked sms");
            Class clazz = XposedHelpers.findClass("com.android.messaging.datamodel.BugleNotifications", lpparam.classLoader);
            Class p1 = XposedHelpers.findClass("androidx.core.app.NotificationCompat$WearableExtender", lpparam.classLoader);
            Class p2 = XposedHelpers.findClass("com.android.messaging.datamodel.NotificationState", lpparam.classLoader);

            Method clearMethod = p1.getMethod("clearActions", null);

            XposedHelpers.findAndHookMethod(
                    clazz, "addWearableVoiceReplyAction",
                    p1, p2, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    clearMethod.invoke(param.args[0]);
                }
            });
        }
    }
}
