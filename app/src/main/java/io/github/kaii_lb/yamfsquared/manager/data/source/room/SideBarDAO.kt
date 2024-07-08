package io.github.kaii_lb.yamfsquared.manager.data.source.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.kaii_lb.yamfsquared.manager.data.source.room.Entity.SideBarEntity

@Dao
interface SideBarDAO {
    @Query("SELECT * FROM sideBarApp")
    fun getSideBarApp(): List<SideBarEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = SideBarEntity::class)
    fun insertSideBarApp(sideBarEntity: SideBarEntity)

    @Delete
    fun deleteSideBarApp(sideBarEntity: SideBarEntity)
}