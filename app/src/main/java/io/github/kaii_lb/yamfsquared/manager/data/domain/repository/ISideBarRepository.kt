package io.github.kaii_lb.yamfsquared.manager.data.domain.repository

import io.github.kaii_lb.yamfsquared.xposed.ui.window.model.AppInfo

interface ISideBarRepository {
    fun getSideBarApp(): List<AppInfo>

    fun insertSideBarApp(appInfo: AppInfo)

    fun deleteSideBarApp(appInfo: AppInfo)
}