package com.mabo.actionsheet

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.JavaScriptModule
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

import java.lang.annotation.Native
import java.util.Arrays
import java.util.Collections

/**
 * Created by maksim on 2/19/18.
 */

class ActionSheetPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return Arrays.asList<NativeModule>(
                ActionSheetModule(reactContext)
        )
    }

    // Deprecated RN 0.47
    @Suppress("unused")
    fun createJSModules(): List<Class<out JavaScriptModule>> {
        return emptyList()
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}
