package com.dieam.reactnativepushnotification;

import com.dieam.reactnativepushnotification.modules.RNPushNotification;
import com.dieam.reactnativepushnotification.modules.UMPushModule;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReactNativePushNotificationPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(
            ReactApplicationContext reactContext) {

        return Arrays.<NativeModule>asList(new RNPushNotification(reactContext), new UMPushModule(reactContext));
//        return Collections.<NativeModule>singletonList(new RNPushNotification(reactContext), new UMPushModule(reactContext));
    }

    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}


