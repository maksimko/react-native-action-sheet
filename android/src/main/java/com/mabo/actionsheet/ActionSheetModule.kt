package com.mabo.actionsheet

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Parcelable
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.core.app.ShareCompat
import android.util.Log

import com.facebook.react.bridge.ActivityEventListener
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.mabo.actionsheet.utils.get

import java.util.ArrayList

/**
 * Created by maksim on 2/19/18.
 */

class ActionSheetModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    private var sheet: Sheet? = null
    private val shareReceiversList = ArrayList<Intent>()

    override fun getName(): String {
        return "ActionSheet"
    }

    @ReactMethod
    @Suppress("unused")
    fun showActionSheetWithOptions(parameters: ReadableMap, callback: Callback?) {
        if (sheet?.isShowing == true) { return }

        val currentActivity = reactApplicationContext.currentActivity ?: return

        sheet = Sheet(currentActivity, parameters, callback)
        sheet!!.show()
    }

    @ReactMethod
    @Suppress("unused")
    fun showShareActionSheetWithOptions(parameters: ReadableMap, failureCallback: Callback?, successCallback: Callback?) {
        val inclusionList = parameters.get("android.includedActivityTypes", ArrayList<Any>())
        val exclusionList = parameters.get("excludedActivityTypes", ArrayList<Any>())
        val subject = parameters.get<String>("subject")
        val message = parameters.get<String>("message")
        val url = parameters.get<String>("url")
        val dialogTitle = parameters.get<String>("android.dialogTitle")

        val currentActivity = currentActivity

        if (currentActivity == null) {
            failureCallback?.invoke()
            return
        }

        val intentBuilder = ShareCompat.IntentBuilder
                .from(currentActivity)
                .setType("text/plain")
                .setText(url)

        dialogTitle.let { intentBuilder.setChooserTitle(it) }
        subject.let { intentBuilder.setSubject(it) }
        message.let { intentBuilder.setText(String.format("%s %s", message, url)) }

        val intent = intentBuilder.intent

        val pm = currentActivity?.packageManager ?: return

        if (intent.resolveActivity(pm) == null) {
            failureCallback?.invoke()
            return
        }

        if (shareReceiversList.isEmpty()) {
            val resInfo = pm.queryIntentActivities(intent, 0)

            for (i in resInfo.indices) {
                val info = resInfo[i]

                val packageName = info.activityInfo.packageName

                val specificIntent = Intent(intent)
                specificIntent.component = ComponentName(packageName, info.activityInfo.name)
                specificIntent.setPackage(packageName)
                shareReceiversList.add(specificIntent)
            }
        }

        if (BuildConfig.DEBUG) {
            val stringBuilder = StringBuilder("Available actions: ")

            for (i in shareReceiversList.indices) {
                val foundIntent = shareReceiversList[i]
                stringBuilder.append(foundIntent.getPackage()).append(", ")
            }

            Log.d("ActionSheetModule", stringBuilder.toString().trim { it <= ' ' })
        }

        val intentList = ArrayList<Intent>()

        for (i in shareReceiversList.indices) {
            val foundIntent = shareReceiversList[i]

            if (!inclusionList.isEmpty() && !inclusionList.contains(foundIntent.getPackage())) {
                continue
            }

            if (exclusionList.contains(foundIntent.getPackage())) {
                continue
            }

            intentList.add(foundIntent)
        }

        if (shareReceiversList.isEmpty()) {
            failureCallback?.invoke()
            return
        }

        reactApplicationContext.addActivityEventListener(object : ActivityEventListener {
            override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
                if (requestCode != REQUEST_CODE) {
                    return
                }

                if (resultCode == Activity.RESULT_OK && successCallback != null) {
                    successCallback.invoke()
                } else failureCallback?.invoke()

                reactApplicationContext.removeActivityEventListener(this)
            }

            override fun onNewIntent(intent: Intent) {}
        })

        val chooserIntent = Intent.createChooser(intentList.removeAt(0), dialogTitle)
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toTypedArray<Parcelable>())
        getCurrentActivity()!!.startActivityForResult(chooserIntent, REQUEST_CODE)
    }

    companion object {
        private val REQUEST_CODE = 321
    }
}
