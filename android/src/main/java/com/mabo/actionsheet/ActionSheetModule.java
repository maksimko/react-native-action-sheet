package com.mabo.actionsheet;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maksim on 2/19/18.
 */

public class ActionSheetModule extends ReactContextBaseJavaModule {
    private static final int REQUEST_CODE = 4236543;

    private Sheet sheet;
    private List<Intent> shareReceiversList = new ArrayList<>();

    @Override
    public String getName() {
        return "ActionSheet";
    }

    public ActionSheetModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @SuppressWarnings("unused")
    @ReactMethod
    public void showActionSheetWithOptions(@NonNull ReadableMap parameters, @Nullable final Callback callback) {
        if (sheet != null && sheet.isShowing()) {
            return;
        }

        Activity currentActivity = getReactApplicationContext().getCurrentActivity();

        if (currentActivity == null) { return; }

        (sheet = new Sheet(currentActivity, parameters, callback)).show();
    }

    @SuppressWarnings("unused")
    @ReactMethod
    public void showShareActionSheetWithOptions(@NonNull ReadableMap parameters, @Nullable final Callback failureCallback, @Nullable final Callback successCallback) {
        ArrayList<Object> inclusionList = MapUtils.get(parameters, "android.includedActivityTypes", new ArrayList<>());
        ArrayList<Object> exclusionList = MapUtils.get(parameters, "excludedActivityTypes", new ArrayList<>());
        String subject = MapUtils.get(parameters, "subject");
        String message = MapUtils.get(parameters, "message");
        String url = MapUtils.get(parameters, "url");
        String dialogTitle = MapUtils.get(parameters, "android.dialogTitle");

        Activity currentActivity = getCurrentActivity();

        if (currentActivity == null) {
            if (failureCallback != null) {
                failureCallback.invoke();
            }
            return;
        }

        Intent intent =
                ShareCompat.IntentBuilder
                .from(currentActivity)
                .setType("text/plain")
                .setSubject(subject)
                .setText(String.format("%s %s", message, url))
                .setChooserTitle(dialogTitle)
                .getIntent();

        PackageManager pm = getCurrentActivity().getPackageManager();

        if (intent.resolveActivity(pm) == null) {
            if (failureCallback != null) {
                failureCallback.invoke();
            }
            return;
        }

        if (shareReceiversList.isEmpty()) {
            List<ResolveInfo> resInfo = pm.queryIntentActivities(intent, 0);

            for(int i = 0; i < resInfo.size(); i++) {
                ResolveInfo info = resInfo.get(i);

                String packageName = info.activityInfo.packageName;

                Intent specificIntent = new Intent(intent);
                specificIntent.setComponent(new ComponentName(packageName, info.activityInfo.name));
                specificIntent.setPackage(packageName);
                shareReceiversList.add(specificIntent);
            }
        }

        if (BuildConfig.DEBUG) {
            StringBuilder stringBuilder = new StringBuilder("Available actions: ");

            for (int i = 0; i < shareReceiversList.size(); i++) {
                Intent foundIntent = shareReceiversList.get(i);
                stringBuilder.append(foundIntent.getPackage()).append(", ");
            }

            Log.d("ActionSheetModule", stringBuilder.toString().trim());
        }

        ArrayList<Intent> intentList = new ArrayList<>();

        for(int i = 0; i < shareReceiversList.size(); i++) {
            Intent foundIntent = shareReceiversList.get(i);

            if (!inclusionList.isEmpty() && !inclusionList.contains(foundIntent.getPackage())) {
                continue;
            }

            if (exclusionList.contains(foundIntent.getPackage())) {
                continue;
            }

            intentList.add(foundIntent);
        }

        if (shareReceiversList.isEmpty()) {
            if (failureCallback != null) {
                failureCallback.invoke();
            }
            return;
        }

        getReactApplicationContext().addActivityEventListener(new ActivityEventListener() {
            @Override
            public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
                if (requestCode != REQUEST_CODE) {
                    return;
                }

                if (resultCode == Activity.RESULT_OK && successCallback != null) {
                    successCallback.invoke();
                } else if (failureCallback != null) {
                    failureCallback.invoke();
                }
            }

            @Override
            public void onNewIntent(Intent intent) {
            }
        });

        Intent chooserIntent = Intent.createChooser(intentList.remove(0), dialogTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        getCurrentActivity().startActivityForResult(chooserIntent, REQUEST_CODE);
    }
}
