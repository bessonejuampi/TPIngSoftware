package com.example.tpingsoftware.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tpingsoftware.utils.Constants

@Database(entities = [], version = 1)
abstract class AppDatabase : RoomDatabase(){
//abstract fun estimationInputDao(): EstimationInputDAO?

    companion object{

        private val LOCK = Any()

        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(LOCK) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, Constants.DATABASE_NAME
                    ).allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }
}