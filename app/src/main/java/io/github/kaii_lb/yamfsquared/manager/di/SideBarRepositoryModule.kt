package io.github.kaii_lb.yamfsquared.manager.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kaii_lb.yamfsquared.manager.data.domain.repository.ISideBarRepository
import io.github.kaii_lb.yamfsquared.manager.data.repository.SideBarRepository

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class SideBarRepositoryModule {

    @Binds
    abstract fun provideSideBarRepository(
        sideBarRepository: SideBarRepository
    ): ISideBarRepository
}