package io.github.kaii_lb.yamfsquared.manager.data.repository

import io.github.kaii_lb.yamfsquared.manager.data.SideBarDataSource
import io.github.kaii_lb.yamfsquared.manager.data.domain.repository.ISideBarRepository
import io.github.kaii_lb.yamfsquared.manager.data.source.room.Entity.SideBarEntity
import io.github.kaii_lb.yamfsquared.manager.utils.DomainUtils.mapSideBarAppToDomain
import io.github.kaii_lb.yamfsquared.manager.utils.DomainUtils.mapSideBarToEntity
import io.github.kaii_lb.yamfsquared.xposed.ui.window.model.AppInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class SideBarRepository @Inject constructor(
    private val sideBarDataSource: SideBarDataSource
): ISideBarRepository {
    override fun getSideBarApp(): List<AppInfo> =
        mapSideBarAppToDomain(
            sideBarDataSource.getSideBarApp()
        )

    override fun insertSideBarApp(appInfo: AppInfo) {
        sideBarDataSource.insertSideBarApp(
            mapSideBarToEntity(appInfo)
        )
    }

    override fun deleteSideBarApp(appInfo: AppInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            sideBarDataSource.deleteSideBarApp(
                mapSideBarToEntity(appInfo)
            )
        }
    }
}