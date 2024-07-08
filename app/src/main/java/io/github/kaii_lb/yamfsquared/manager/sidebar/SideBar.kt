package io.github.kaii_lb.yamfsquared.manager.sidebar

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.IPackageManagerHidden
import android.content.pm.PackageManagerHidden
import android.content.pm.UserInfo
import android.graphics.PixelFormat
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.kyuubiran.ezxhelper.utils.argTypes
import com.github.kyuubiran.ezxhelper.utils.args
import com.github.kyuubiran.ezxhelper.utils.invokeMethodAs
import io.github.kaii_lb.yamfsquared.common.model.StartCmd
import io.github.kaii_lb.yamfsquared.common.onException
import io.github.kaii_lb.yamfsquared.common.resetAdapter
import io.github.kaii_lb.yamfsquared.common.runIO
import io.github.kaii_lb.yamfsquared.common.runMain
import io.github.kaii_lb.yamfsquared.databinding.SidebarLayoutBinding
import io.github.kaii_lb.yamfsquared.manager.adapter.SideBarAdapter
import io.github.kaii_lb.yamfsquared.xposed.services.YAMFManager
import io.github.kaii_lb.yamfsquared.xposed.ui.window.AppListWindow
import io.github.kaii_lb.yamfsquared.xposed.ui.window.model.AppInfo
import io.github.kaii_lb.yamfsquared.xposed.utils.AppInfoCache
import io.github.kaii_lb.yamfsquared.xposed.utils.Instances
import io.github.kaii_lb.yamfsquared.xposed.utils.TipUtil
import io.github.kaii_lb.yamfsquared.xposed.utils.componentName
import io.github.kaii_lb.yamfsquared.xposed.utils.getActivityInfoCompat
import io.github.kaii_lb.yamfsquared.xposed.utils.log
import io.github.kaii_lb.yamfsquared.xposed.utils.startActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@SuppressLint("ClickableViewAccessibility")
class SideBar(val context: Context, private val displayId: Int? = null) {
    companion object {
        const val TAG = "YAMF_SideBar"
    }

    private var windowManager = Instances.windowManager
    private var initialX: Int = 0
    private var initialY: Int = 0
    private var initialTouchX: Float = 0.toFloat()
    private var initialTouchY: Float = 0.toFloat()
    private lateinit var params : WindowManager.LayoutParams
    private var job: Job? = null
    private var movable = false
    private var swipeX = 0

    private lateinit var binding: SidebarLayoutBinding
    private val users = mutableMapOf<Int, String>()
    var userId = 0
    private var apps = emptyList<ActivityInfo>()
    private var showApps: MutableList<AppInfo> = mutableListOf()

    init {
        runCatching {
            binding = SidebarLayoutBinding.inflate(LayoutInflater.from(context))
        }.onException { e ->
            Log.e(AppListWindow.TAG, "new app list failed: ", e)
            TipUtil.showToast("new app list failed\nmay you forget reboot")
        }.onSuccess {
            doInit()
        }
    }

    private fun doInit() {
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.NO_GRAVITY
        params.x = -100000
        params.y = 0

        binding.root.let { layout ->
            windowManager.addView(layout, params)
        }

        binding.closeClickMask.setOnClickListener {
            hideMenu()
        }

        binding.clickMask.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN ->  storeTouchs(event)

                MotionEvent.ACTION_MOVE ->  moveBubble(event)

                MotionEvent.ACTION_UP   ->  openApp(event)

                else ->  false
            }
        }

        getAppList()
    }

    private fun openApp(event: MotionEvent): Boolean {
        job?.cancel()
        movable = false
        if (initialTouchY == event.rawY) {
            showMenu()
            vibratePhone()
        }

        if (swipeX > 200) {
            showMenu()
            swipeX = 0
            vibratePhone()
        }
        return true
    }

    private fun moveBubble(event: MotionEvent): Boolean {
        swipeX = event.rawX.toInt()
        params.y = initialY + (event.rawY - initialTouchY).toInt()

        if (movable) {
            windowManager.updateViewLayout(binding.root, params)
        }
        return true
    }


    private fun storeTouchs(event: MotionEvent): Boolean {
        Log.d("test", "Down")
        startCounter()
        initialX = params.x
        initialY = params.y
        initialTouchX = (event.rawX)
        initialTouchY = (event.rawY)
        return true
    }

    private fun startCounter() {
        job = CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            movable = true
            vibratePhone()
        }
    }

    private fun showMenu() {
        binding.bolhaImg.visibility = View.INVISIBLE
        binding.sideBarMenu.visibility = View.VISIBLE
        binding.closeLayout.visibility = View.VISIBLE
    }

    private fun hideMenu() {
        binding.bolhaImg.visibility = View.VISIBLE
        binding.sideBarMenu.visibility = View.GONE
        binding.closeLayout.visibility = View.GONE
    }

    private fun getAppList() {
        runIO {
            Instances.userManager.invokeMethodAs<List<UserInfo>>(
                "getUsers",
                args(true, true, true),
                argTypes(java.lang.Boolean.TYPE, java.lang.Boolean.TYPE, java.lang.Boolean.TYPE)
            )!!
                .filter { it.isProfile || it.isPrimary }
                .forEach {
                    users[it.id] = it.name
                }
            log(TAG, users.toString())

            for ((key, _) in users) {
                getAppListByID(key)
            }

            showApps.sortBy { it.label.toString().lowercase(Locale.ROOT) }
            log(TAG, showApps.toString())

            val listener: (AppInfo) -> Unit = {
                if (displayId == null)
                    YAMFManager.createWindow(StartCmd(it.componentName, it.userId))
                else
                    startActivity(context, it.componentName, it.userId, displayId)
                hideMenu()
            }

            runMain {
                binding.cpiLoading.visibility = View.INVISIBLE
                binding.rvSideBarMenu.layoutManager = LinearLayoutManager(context)
                val rvAdapter = SideBarAdapter(listener, arrayListOf())
                binding.rvSideBarMenu.adapter = rvAdapter
                binding.rvSideBarMenu.resetAdapter()
                rvAdapter.setData(showApps)
                rvAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun getAppListByID(uId: Int): List<ActivityInfo> {
        val apps = (Instances.packageManager as PackageManagerHidden).queryIntentActivitiesAsUser(
            Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }, 0, uId
        ).map {
            (Instances.iPackageManager as IPackageManagerHidden).getActivityInfoCompat(
                ComponentName(it.activityInfo.packageName, it.activityInfo.name),
                0, uId
            )
        }
        apps.forEach { activityInfo ->
            val appInfoCache = AppInfoCache.getIconLabel(activityInfo)
            showApps.add(
                AppInfo(
                    0, appInfoCache.first, appInfoCache.second, activityInfo.componentName, uId
                )
            )
        }

        this.apps = listOf(this.apps, apps).flatten()

        return apps
    }

    private fun vibratePhone() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}