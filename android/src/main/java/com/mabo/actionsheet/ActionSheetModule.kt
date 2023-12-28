package com.mabo.actionsheet

import android.app.Activity
import android.app.PendingIntent
import android.content.ClipData
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.service.chooser.ChooserTarget
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
    private var handler: Handler = Handler(Looper.getMainLooper())

    override fun getName(): String {
        return "ActionSheet"
    }

    @ReactMethod
    @Suppress("unused")
    fun showActionSheetWithOptions(parameters: ReadableMap, callback: Callback?) {
        if (sheet?.isShowing == true) { return }

        val currentActivity = reactApplicationContext.currentActivity ?: return

        handler.post {
            sheet = Sheet(currentActivity, parameters, callback)
            sheet!!.show()
        }
    }

    @ReactMethod
    @Suppress("unused")
    fun showShareActionSheetWithOptions(parameters: ReadableMap, failureCallback: Callback?, successCallback: Callback?) {
//        val inclusionList = parameters.get("android.includedActivityTypes", ArrayList<String>())
//        val exclusionList = parameters.get("excludedActivityTypes", ArrayList<String>())
        val subject = parameters.get<String>("subject")
        val message = parameters.get<String>("message")
        val url = parameters.get<String>("url")
        val dialogTitle = parameters.get<String>("android.dialogTitle")
//        val shareReceiversList = ArrayList<Intent>()

        val currentActivity = currentActivity

        if (currentActivity == null) {
            failureCallback?.invoke()
            return
        }

        val pm = currentActivity.packageManager ?: return

        val intentBuilder = ShareCompat.IntentBuilder
                .from(currentActivity)
                .setChooserTitle(dialogTitle ?: "")
                .setSubject(subject ?: "")
                .setType("text/plain")
                .setText(listOfNotNull(message, url).joinToString(" "))

        val shareIntent = intentBuilder.intent;

//        shareIntent.clipData = ClipData.newRawUri("test", Uri.parse(url))

        if (shareIntent.resolveActivity(pm) == null) {
            failureCallback?.invoke()
            return
        }

//        val resInfo = pm.queryIntentActivities(shareIntent, 0)
//
//        for (i in resInfo.indices) {
//            val info = resInfo[i]
//
//            val packageName = info.activityInfo.packageName
//
//            val specificIntent = Intent(shareIntent)
//            specificIntent.component = ComponentName(packageName, info.activityInfo.name)
//            specificIntent.setPackage(packageName)
//            shareReceiversList.add(specificIntent)
//        }
//
//        if (BuildConfig.DEBUG) {
//            val logMessage = shareReceiversList.fold(StringBuilder("Available actions: ")) {
//                sb, foundIntent -> sb.append(foundIntent.`package`).append(", ")
//            }
//            Log.d("ActionSheetModule", logMessage.toString().trim { it <= ' ' })
//        }
//
//        val intentList = shareReceiversList.filter { foundIntent ->
//            val foundPackage = foundIntent.`package`
//
//            if (foundPackage.isNullOrBlank()) {
//                return@filter false
//            }
//
//            if (inclusionList.isNotEmpty() && !inclusionList.contains(foundPackage)) {
//                return@filter false
//            }
//
//            if (exclusionList.isNotEmpty() && exclusionList.contains(foundPackage)) {
//                return@filter false
//            }
//
//            true
//        }

//        if (shareReceiversList.isEmpty()) {
//            failureCallback?.invoke()
//            return
//        }

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

        currentActivity.startActivityForResult(intentBuilder.createChooserIntent(), REQUEST_CODE)
    }

    companion object {
        private const val REQUEST_CODE = 32134
    }
}
