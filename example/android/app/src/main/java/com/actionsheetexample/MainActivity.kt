package com.actionsheetexample

import com.facebook.react.ReactActivity

class MainActivity : ReactActivity() {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    override fun getMainComponentName(): String? {
        return "ActionSheetExample"
    }

    override fun onStart() {
        super.onStart()

        window.decorView.setOnClickListener { (application as MainApplication).reactNativeHost.reactInstanceManager.showDevOptionsDialog() }
    }
}
