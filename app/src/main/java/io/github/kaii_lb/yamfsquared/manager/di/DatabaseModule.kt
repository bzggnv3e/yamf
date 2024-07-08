package io.github.kaii_lb.yamfsquared.manager.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kaii_lb.yamfsquared.manager.data.source.room.SideBarDAO
import io.github.kaii_lb.yamfsquared.manager.data.source.room.SideBarDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SideBarDatabase {
        return Room.databaseBuilder(
            context,
            SideBarDatabase::class.java, "sideBarApp.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideSideBarDao(database: SideBarDatabase): SideBarDAO = database.sideBarDao()
}