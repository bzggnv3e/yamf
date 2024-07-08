package io.github.kaii_lb.yamfsquared.manager.utils

import io.github.kaii_lb.yamfsquared.manager.data.source.room.Entity.SideBarEntity
import io.github.kaii_lb.yamfsquared.xposed.ui.window.model.AppInfo

object DomainUtils {
    fun mapSideBarAppToDomain(sideBarApp: List<SideBarEntity>) = run {
        sideBarApp.map {
            AppInfo(
                it.id,
                it.icon,
                it.label,
                it.componentName,
                it.userId
            )
        }
    }

    fun mapSideBarToEntity(sideBarApp: AppInfo) = run {
        SideBarEntity(
            sideBarApp.id,
            sideBarApp.icon,
            sideBarApp.label.toString(),
            sideBarApp.componentName,
            sideBarApp.userId
        )
    }
}