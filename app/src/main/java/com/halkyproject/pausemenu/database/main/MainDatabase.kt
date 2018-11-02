package com.halkyproject.pausemenu.database.main

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.halkyproject.pausemenu.PauseApp
import com.halkyproject.pausemenu.database.main.dao.GeneralConfigDao
import com.halkyproject.pausemenu.database.main.entities.GeneralConfig


@Database(entities = [(GeneralConfig::class)], version = 5, exportSchema = false)
abstract class MainDatabase() : RoomDatabase() {
    abstract fun generalConfigDao(): GeneralConfigDao

    companion object {
        private var instance: MainDatabase? = null
        fun getInstance(): MainDatabase {
            if (instance == null) {
                val db = Room.databaseBuilder(PauseApp.getApp().applicationContext, MainDatabase::class.java, "main-db").fallbackToDestructiveMigration()
                instance = db.build()
            }
            return instance as MainDatabase
        }


    }


}
