package com.github.ebrahimi16153.mvvminxml.util.di

import android.content.Context
import androidx.room.Room
import com.github.ebrahimi16153.mvvminxml.data.local.FoodDB
import com.github.ebrahimi16153.mvvminxml.util.FOOD_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class LocalModule {


    @Provides
    fun provideDataBase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, FoodDB::class.java, FOOD_DATABASE)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideDao(database: FoodDB) = database.foodDao



}