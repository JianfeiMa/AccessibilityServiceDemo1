package com.buyuphk.accessibilityservicedemo1;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;


import com.buyuphk.accessibilityservicedemo1.utils.AccessibilityLog;

import java.util.List;

/**
 * Created by popfisher on 2017/7/6.
 */

@TargetApi(16)
public class AccessibilitySampleService extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        // 通过代码可以动态配置，但是可配置项少一点
//        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
//        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED
//                | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
//                | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
//                | AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
//        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
//        accessibilityServiceInfo.notificationTimeout = 0;
//        accessibilityServiceInfo.flags = AccessibilityServiceInfo.DEFAULT;
//        setServiceInfo(accessibilityServiceInfo);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 此方法是在主线程中回调过来的，所以消息是阻塞执行的
        // 获取包名
        String pkgName = "";
        if (event.getPackageName() != null) {
            pkgName = event.getPackageName().toString();
        }
        int eventType = event.getEventType();
        AccessibilityOperator.getInstance().updateEvent(this, event);
//        try {
//            Thread.sleep(200);
//        } catch (Exception e) {}
        //AccessibilityLog.printLog("eventType: " + eventType + " pkgName: " + pkgName);
        Log.d("debug", "eventType(事件类型): " + eventType + " pkgName(包名): " + pkgName);
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo != null) {
            CharSequence charSequence = accessibilityNodeInfo.getText();
            if (charSequence != null) {
                String a = charSequence.toString();
                Log.d("debug", "打印可访问节点信息的文本->" + a);
            }
            List<AccessibilityNodeInfo> accessibilityNodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText("微信红包");
            for (AccessibilityNodeInfo accessibilityNodeInfo1 : accessibilityNodeInfoList) {
                Log.d("debug", "遍历所有可访问的历史信息->" + accessibilityNodeInfo1.getText().toString());
            }
        }
        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED://type_notification_start_changed
                Log.d("debug", "<<<<<<<<<<触发通知类型的AccessibilityEvent>>>>>>>>>>");
                for (CharSequence text : event.getText()) {
                    String content = text.toString();
                    Log.d("debug", "打印从通知栏获取到的文本：" + content);
                    if (content.contains("[微信红包]") || content.contains("[QQ红包]")) {
                        Log.d("debug", "触发微信红包或者QQ红包");
                        if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
                            final PendingIntent contentIntent = ((Notification) event.getParcelableData()).contentIntent;
                            try {
                                contentIntent.send();
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (content.contains("微信支付")) {
                        int startIndex = content.indexOf("款");
                        int endIndex = content.indexOf("元");
                        String money = content.substring(startIndex + 1, endIndex);
                        Log.d("debug", "收款多少钱：" + money);
                        if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
                            final PendingIntent contentIntent = ((Notification) event.getParcelableData()).contentIntent;
                            try {
                                contentIntent.send();
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
            case AccessibilityEvent.TYPE_ANNOUNCEMENT://type_announcement

                break;
            case AccessibilityEvent.TYPE_ASSIST_READING_CONTEXT://type_assist_reading_context

                break;
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_END://type_gesture_detection_end

                break;
            case AccessibilityEvent.TYPE_GESTURE_DETECTION_START://type_gesture_detection_start

                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END://type_touch_exploration_gesture_end

                break;
            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START://type_touch_exploration_gesture_start

                break;
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_END://type_touch_interaction_end

                break;
            case AccessibilityEvent.TYPE_TOUCH_INTERACTION_START://type_touch_interaction_start

                break;
            case AccessibilityEvent.TYPE_VIEW_CLICKED://type_view_clicked

                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
//                if (event.getPackageName().toString().equals("com.tencent.mm")) {
//                    AccessibilityNodeInfo accessibilityNodeInfo1 = getRootInActiveWindow();
//                    int sumView = accessibilityNodeInfo1.getChildCount();
//                    int target = 0;
//                    for (int i = 0; i < sumView; i ++) {
//                        AccessibilityNodeInfo accessibilityNodeInfo2 = accessibilityNodeInfo1.getChild(i);
//                        if (accessibilityNodeInfo2 != null) {
//                            CharSequence charSequence = accessibilityNodeInfo2.getText();
//                            if (charSequence != null) {
//                                String c = charSequence.toString();
//                                if (c.equals("付款方备注"))
//                            }
//                        }
//                        if (accessibilityNodeInfo2.getText().toString().equals("付款方备注")) {
//                            target = i + 1;
//                        }
//                    }
//                    Log.d("debug", "目标：" + accessibilityNodeInfo1.getChild(target).getText().toString());
//                    //List<AccessibilityNodeInfo> accessibilityNodeInfoList = accessibilityNodeInfo1.findAccessibilityNodeInfosByText("付款方备注");
//                    //performClick(accessibilityNodeInfoList);
//                }
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }

    private boolean performClick(List<AccessibilityNodeInfo> nodeInfos) {
        if (nodeInfos != null && !nodeInfos.isEmpty()) {
            AccessibilityNodeInfo node;
            for (int i = 0; i < nodeInfos.size(); i++) {
                node = nodeInfos.get(i);
                // 获得点击View的类型
                AccessibilityLog.printLog("View类型：" + node.getClassName());
                String className = node.getClassName().toString();
                Log.d("debug", "打印类名：" + className);
                String z = node.getText().toString();
                Log.d("debug", "打印文本:" + z);
                boolean backResult = performGlobalAction(GLOBAL_ACTION_BACK);
                Log.d("debug", "back_result:" + backResult);
                boolean backResult1 = performGlobalAction(GLOBAL_ACTION_BACK);
                Log.d("debug", "back_result1:" + backResult1);
                // 进行模拟点击
                //if (node.isEnabled()) {
                //    return node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                //}
            }
        }
        return false;
    }
}
