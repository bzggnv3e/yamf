package io.github.kaii_lb.yamfsquared.manager

import com.google.android.material.color.DynamicColors
import io.github.kaii_lb.yamfsquared.manager.utils.AppContext

lateinit var application: Application

open class Application: android.app.Application() {
    init {
        application = this
        AppContext.context = this
    }

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}