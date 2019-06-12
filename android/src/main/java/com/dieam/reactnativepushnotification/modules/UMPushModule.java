package com.dieam.reactnativepushnotification.modules;

import android.util.Log;

import java.util.List;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.umeng.message.PushAgent;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;


/**
 * @author qiaojiayan
 * @date 2019/6/12
 **/
public class UMPushModule extends ReactContextBaseJavaModule {
    static String umengPushToken = "";
    private final String ERROR = "error";
    private final String SUCCESS = "success";

    private static final String TAG = UMPushModule.class.getSimpleName();
    private ReactApplicationContext context;
    private PushAgent mPushAgent;

    public UMPushModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
        mPushAgent = PushAgent.getInstance(context);
    }

    public static void setToken(String token) {
        umengPushToken = token;
    }

    public static String getToken() {
        return umengPushToken;
    }

    @Override
    public String getName() {
        return "UMPushModule";
    }

    @ReactMethod
    public void getAllTag(final Promise promise) {
        mPushAgent.getTagManager().getTags(new TagManager.TagListCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final List<String> result) {
                if (isSuccess) {
                    promise.resolve(resultToList(result));
                } else {
                    promise.reject(ERROR, ERROR);
                }
            }
        });
    }

    @ReactMethod
    public void addTag(ReadableArray tag, final Promise promise) {
        String[] params = raToArray(tag);
        mPushAgent.getTagManager().addTags(new TagManager.TCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
                if (isSuccess) {
                    promise.resolve(result.remain);
                } else {
                    promise.reject(ERROR, ERROR);
                }
            }
        }, params);
    }

    @ReactMethod
    public void deleteTag(ReadableArray tag, final Promise promise) {
        String[] params = raToArray(tag);
        mPushAgent.getTagManager().deleteTags(new TagManager.TCallBack() {
            @Override
            public void onMessage(boolean isSuccess, final ITagManager.Result result) {
                if (isSuccess) {
                    promise.resolve(result.remain);
                } else {
                    promise.reject(ERROR, ERROR);
                }
            }
        }, params);
    }

    @ReactMethod
    public void deleteAllTag(final Promise promise) {
        mPushAgent.getTagManager().getTags(new TagManager.TagListCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final List<String> result) {
                if (isSuccess && result != null && result.size() > 0) {
                    String[] params = result.toArray(new String[result.size()]);
                    mPushAgent.getTagManager().deleteTags(new TagManager.TCallBack() {
                        @Override
                        public void onMessage(boolean isSuccess, ITagManager.Result result) {
                            if (isSuccess) {
                                promise.resolve(result.remain);
                            } else {
                                promise.reject(ERROR, ERROR);
                            }
                        }
                    }, params);
                } else {
                    promise.reject(ERROR, ERROR);
                }
            }
        });
    }


//
//    @ReactMethod
//    public void addAlias(String alias, String aliasType, final Promise promise) {
//        mPushAgent.addAlias(alias, aliasType, new UTrack.ICallBack() {
//            @Override
//            public void onMessage(final boolean isSuccess, final String message) {
//                Log.i(TAG, "isSuccess:" + isSuccess + "," + message);
//                if (isSuccess) {
//                    promise.resolve(SUCCESS);
//                } else {
//                    promise.reject(ERROR, ERROR);
//                }
//            }
//        });
//    }
//
//    @ReactMethod
//    public void addExclusiveAlias(String exclusiveAlias, String aliasType, final Promise promise) {
//        mPushAgent.setAlias(exclusiveAlias, aliasType, new UTrack.ICallBack() {
//            @Override
//            public void onMessage(final boolean isSuccess, final String message) {
//                Log.i(TAG, "isSuccess:" + isSuccess + "," + message);
//                if (Boolean.TRUE.equals(isSuccess)) {
//                    promise.resolve(SUCCESS);
//                } else {
//                    promise.reject(ERROR, ERROR);
//                }
//            }
//        });
//    }
//
//    @ReactMethod
//    public void deleteAlias(String alias, String aliasType, final Promise promise) {
//        mPushAgent.deleteAlias(alias, aliasType, new UTrack.ICallBack() {
//            @Override
//            public void onMessage(boolean isSuccess, String s) {
//                if (Boolean.TRUE.equals(isSuccess)) {
//                    promise.resolve(SUCCESS);
//                } else {
//                    promise.reject(ERROR, ERROR);
//                }
//            }
//        });
//    }
//
//    @ReactMethod
//    public void appInfo(final Callback successCallback) {
//        String pkgName = context.getPackageName();
//        String info = String.format("DeviceToken:%s\n" + "SdkVersion:%s\nAppVersionCode:%s\nAppVersionName:%s",
//                mPushAgent.getRegistrationId(), MsgConstant.SDK_VERSION,
//                UmengMessageDeviceConfig.getAppVersionCode(context), UmengMessageDeviceConfig.getAppVersionName(context));
//        successCallback.invoke("应用包名:" + pkgName + "\n" + info);
//    }

    private WritableArray resultToList(List<String> result) {
        WritableArray list = Arguments.createArray();
        if (result != null) {
            for (String key : result) {
                list.pushString(key);
            }
        }
        return list;
    }

    private String[] raToArray(ReadableArray ra) {
        if (ra != null && ra.size() > 0) {
            String[] array = new String[ra.size()];
            for (int i = 0; i < ra.size(); i++) {
                array[i] = ra.getString(i);
            }
            return array;
        }
        return new String[]{};
    }
}


