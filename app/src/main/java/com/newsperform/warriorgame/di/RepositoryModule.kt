package com.newsperform.warriorgame.di

import com.newsperform.warriorgame.data.repository.WarriorRepositoryImp
import com.newsperform.warriorgame.domain.repository.WarriorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideWarriorRepository(
        repositoryImp: WarriorRepositoryImp
    ): WarriorRepository = repositoryImp
}