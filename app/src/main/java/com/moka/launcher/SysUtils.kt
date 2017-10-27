package com.moka.launcher

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager

/**
 * Created by wangjinpeng on 2017/10/19.
 */

object SysUtils {

    fun setFullScreen(activity: Activity) {
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN) // 隐藏android系统的状态栏
    }

    fun removeFullScreen(activity: Activity) {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun setStatysBarPadding(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setPadding(0, SysUtils.getStatusBarHeight(view.context), 0, 0)
        }
    }

    /**
     * 设置小米手机状态栏字体图标颜色模式，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    fun MIUISetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            val clazz = window.javaClass
            try {
                var darkModeFlag = 0
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                if (dark) {//状态栏透明且黑色字体
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
                } else {//清除黑色字体
                    extraFlagField.invoke(window, 0, darkModeFlag)
                }
                result = true
            } catch (e: Exception) {

            }

        }
        return result
    }

    /**
     * 设置魅族手机状态栏图标颜色风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    fun FlymeSetStatusBarLightMode(window: Window?, dark: Boolean): Boolean {
        var result = false
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                        .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                if (dark) {
                    value = value or bit
                } else {
                    value = value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (e: Exception) {

            }

        }
        return result
    }

    // This snippet hides the system bars.
    fun hideSystemUI(activity: Activity) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        val decorView = activity.window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            var visible = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar

                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                visible = (visible
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) // hide nav bar;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                decorView.systemUiVisibility = visible
        }
    }

    // 这个方法处理显示的系统ui
    // 设置全屏模式，状态栏设置为透明色
    fun showSystemUI(activity: Activity) {
        val window = activity.window
        var visible = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 设置系统layout全屏  并且  系统ui的图标显示（状态栏和系统图标）
            visible = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            // 移除原有的透明状态栏配置
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            // 设置可以绘制系统bar背景
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            var statusColor = Color.TRANSPARENT

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 6.0以上,设置背景颜色和状态栏文字颜色
                // 如果背景是浅颜色，则设置状态栏为浅色主题，也就是文字是深色的
                visible = visible or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                if (Build.BRAND.equals("Xiaomi", ignoreCase = true)) {
                    MIUISetStatusBarLightMode(window, true)
                }
                // FIXME: 2017/5/26 此处要处理魅族等国产手机
                /*else if (FlymeSetStatusBarLightMode(window, true)) {

            }*/
            } else {
                statusColor = Color.parseColor("#60000000")
            }
            window.statusBarColor = statusColor
        }
        window.decorView.systemUiVisibility = visible
    }

    fun getStatusBarHeight(context: Context): Int {
        val res = context.resources
        var result = 0
        val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun enterLauncher(activity: Activity) {
        val window = activity.window
        var visible = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 设置系统layout全屏  并且  系统ui的图标显示（状态栏和系统图标）
            visible = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            // 移除原有的透明状态栏配置
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            // 设置可以绘制系统bar背景
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            var statusColor = Color.TRANSPARENT

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 6.0以上,设置背景颜色和状态栏文字颜色
                // 如果背景是浅颜色，则设置状态栏为浅色主题，也就是文字是深色的
                visible = visible or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                if (Build.BRAND.equals("Xiaomi", ignoreCase = true)) {
                    MIUISetStatusBarLightMode(window, true)
                }
                // FIXME: 2017/5/26 此处要处理魅族等国产手机
                *//*else if (FlymeSetStatusBarLightMode(window, true)) {

            }*//*
            } else {
                statusColor = Color.parseColor("#60000000")
            }*/
            window.statusBarColor = statusColor
        }
        window.decorView.systemUiVisibility = visible
    }
}
