package io.github.kaii_lb.yamfsquared.manager.data

import io.github.kaii_lb.yamfsquared.manager.data.source.room.Entity.SideBarEntity
import io.github.kaii_lb.yamfsquared.manager.data.source.room.SideBarDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SideBarDataSource @Inject constructor(
    private val sideBarDAO: SideBarDAO
) {
    fun getSideBarApp() = sideBarDAO.getSideBarApp()

    fun insertSideBarApp(sideBarEntity: SideBarEntity) {
        sideBarDAO.insertSideBarApp(sideBarEntity)
    }

    fun deleteSideBarApp(sideBarEntity: SideBarEntity) {
        sideBarDAO.deleteSideBarApp(sideBarEntity)
    }
}