package io.github.kaii_lb.yamfsquared.xposed.room

import android.content.Context
import androidx.room.Room
import io.github.kaii_lb.yamfsquared.manager.data.source.room.SideBarDatabase

object DatabaseBuilder {
    private var INSTANCE: SideBarDatabase? = null

    fun getInstance(context: Context): SideBarDatabase {
        if (INSTANCE == null) {
            synchronized(SideBarDatabase::class) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    SideBarDatabase::class.java,
                    "sideBarApp.db"
                ).build()
            }
        }
        return INSTANCE!!
    }
}