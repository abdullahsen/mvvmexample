package com.asen.mvvmexample.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.asen.mvvmexample.data.local.dao.MovieDao
import com.asen.mvvmexample.data.local.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}