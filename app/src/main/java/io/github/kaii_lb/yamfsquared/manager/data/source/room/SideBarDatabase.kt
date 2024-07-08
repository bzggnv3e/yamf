package io.github.kaii_lb.yamfsquared.manager.data.source.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.kaii_lb.yamfsquared.manager.data.converter.ComponentNameConverter
import io.github.kaii_lb.yamfsquared.manager.data.converter.DrawableConverter
import io.github.kaii_lb.yamfsquared.manager.data.source.room.Entity.SideBarEntity

@Database(entities = [SideBarEntity::class], version = 1, exportSchema = false)
@TypeConverters(DrawableConverter::class, ComponentNameConverter::class)
abstract class SideBarDatabase: RoomDatabase() {
    abstract fun sideBarDao(): SideBarDAO
}