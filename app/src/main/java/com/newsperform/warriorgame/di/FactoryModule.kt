package com.newsperform.warriorgame.di

import com.newsperform.warriorgame.domain.factory.WarriorFactory
import com.newsperform.warriorgame.domain.factory.WarriorFactoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FactoryModule {

    @Provides
    @Singleton
    fun provideWarriorFactory(): WarriorFactory {
        return WarriorFactoryImp()
    }
}