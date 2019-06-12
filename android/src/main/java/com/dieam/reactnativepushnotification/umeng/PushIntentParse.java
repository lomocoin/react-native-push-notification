package com.dieam.reactnativepushnotification.umeng;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.utl.ALog;
import com.taobao.agoo.BaseNotifyClickActivity;
import com.taobao.agoo.a;
import com.taobao.agoo.b;
import com.taobao.agoo.c;
import com.taobao.agoo.d;

import org.android.agoo.common.MsgDO;
import org.android.agoo.control.AgooFactory;
import org.android.agoo.control.NotifManager;
import org.android.agoo.message.MessageService;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 *
 * @author qiaojiayan
 * @date 2019/6/11
 **/
public class PushIntentParse {
    final String TAG = "isme29";
    private String msgSource;
    private AgooFactory agooFactory;
    private NotifManager notifyManager;
    private Context mContext;


    public JSONObject parse(Context context, Intent intent) {
        this.mContext = context;
        if (intent == null) {
            return null;
        }

        if (intent != null && intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                Log.e("isme29", "key2:" + key + "  value2:" + intent.getExtras().getString(key, "null value"));
            }
        }


        return this.buildMessage(intent);
    }

    private JSONObject buildMessage(final Intent var1) {
//        ThreadPoolExecutorFactory.execute(new Runnable() {
//            public void run() {
        Intent var1x = null;
        try {
            if (var1 != null) {
                String var2 = parseMsgByThirdPush(var1);
                if (!TextUtils.isEmpty(var2) && !TextUtils.isEmpty(msgSource)) {
                    if (notifyManager == null) {
                        notifyManager = new NotifManager();
                    }

                    if (agooFactory == null) {
                        agooFactory = new AgooFactory();
                        agooFactory.init(mContext, notifyManager, (MessageService) null);
                    }

                    Bundle var3 = agooFactory.msgReceiverPreHandler(var2.getBytes("UTF-8"), msgSource, (TaoBaseService.ExtraInfo) null, false);
                    String var4 = var3.getString("body");
                    Log.d(TAG, "begin parse EncryptedMsg");
                    String var5 = AgooFactory.parseEncryptedMsg(var4);
                    if (!TextUtils.isEmpty(var5)) {
                        var3.putString("body", var5);
                    } else {
                        Log.d(TAG, "parse EncryptedMsg fail, empty");
                    }

                    var1x = new Intent();
                    var1x.putExtras(var3);
                    agooFactory.saveMsg(var2.getBytes("UTF-8"), "2");
                    reportClickNotifyMsg(var1x);
                } else {
                    Log.d(TAG, "parseMsgFromNotifyListener null!!,msgSource:" + msgSource);
                }
            }
        } catch (Throwable var14) {
            Log.d(TAG, "buildMessage");
        } finally {
            try {
                return onMessage(var1x);
            } catch (Throwable var13) {
                Log.d(TAG, "onMessage");
            }
        }
        return null;
//            }
//        });
    }

    private JSONObject onMessage(Intent intent) {
        if (intent != null && intent.hasExtra("body")) {
            String str = intent.getStringExtra("body");
            Log.e("isme", "body:" + str);
            try {
                JSONObject object = new JSONObject(str);
                JSONObject result = object.getJSONObject("extra");
                return result;
            } catch (JSONException e) {
            }
//            for (String key : intent.getExtras().keySet()) {
//                Log.e("isme29", "key:" + key + "  value:" + intent.getExtras().getString(key, "null value"));
//            }
        }
        return null;
    }


    private String parseMsgByThirdPush(Intent var1) {
        String var2 = null;

        Log.d(TAG, "no impl, try use default impl to parse intent!");
        Object var3 = new a();
        var2 = ((BaseNotifyClickActivity.INotifyListener) var3).parseMsgFromIntent(var1);
        if (TextUtils.isEmpty(var2)) {
            var3 = new d();
            var2 = ((BaseNotifyClickActivity.INotifyListener) var3).parseMsgFromIntent(var1);
        }

        if (TextUtils.isEmpty(var2)) {
            var3 = new c();
            var2 = ((BaseNotifyClickActivity.INotifyListener) var3).parseMsgFromIntent(var1);
        }

        if (TextUtils.isEmpty(var2)) {
            var3 = new b();
            var2 = ((BaseNotifyClickActivity.INotifyListener) var3).parseMsgFromIntent(var1);
        }

        if (TextUtils.isEmpty(var2)) {
            Log.d(TAG, "var2: is null");
//            com.taobao.accs.utl.b.a("accs", "error", "parse 3push error", 0.0D);
        } else {
            Log.e("BaseNotifyClickActivity", "var2:" + var2);
            this.msgSource = ((BaseNotifyClickActivity.INotifyListener) var3).getMsgSource();
//            com.taobao.accs.utl.b.a("accs", "error", "parse 3push default " + this.msgSource, 0.0D);
        }

        ALog.i(TAG, "parseMsgByThirdPush", new Object[]{"result", var2, "msgSource", this.msgSource});
        return var2;
    }

    private void reportClickNotifyMsg(Intent var1) {
        try {
            String var2 = var1.getStringExtra("id");
            String var3 = var1.getStringExtra("message_source");
            String var4 = var1.getStringExtra("report");
            String var5 = var1.getStringExtra("extData");
            MsgDO var6 = new MsgDO();
            var6.msgIds = var2;
            var6.extData = var5;
            var6.messageSource = var3;
            var6.reportStr = var4;
            var6.msgStatus = "8";
            ALog.i(TAG, "reportClickNotifyMsg messageId:" + var2 + " source:" + var3 + " reportStr:" + var4 + " status:" + var6.msgStatus, new Object[0]);
            this.notifyManager.report(var6, (TaoBaseService.ExtraInfo) null);
        } catch (Exception var7) {
            ALog.e(TAG, "reportClickNotifyMsg exception: " + var7, new Object[0]);
        }

    }

}
