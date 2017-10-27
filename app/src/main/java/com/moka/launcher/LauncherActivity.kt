package com.moka.launcher

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SysUtils.enterLauncher(this);
        if (savedInstanceState == null) {
            var fragment = DrawerFragment()
            supportFragmentManager
                    .beginTransaction()
                    .replace(Window.ID_ANDROID_CONTENT, fragment, "")
                    .commit()
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }

}
