package com.halkyproject.pausemenu.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.halkyproject.pausemenu.PauseApp
import com.halkyproject.pausemenu.database.dao.GeneralConfigDao
import com.halkyproject.pausemenu.database.entities.GeneralConfig


@Database(entities = [(GeneralConfig::class)], version = 1, exportSchema = false)
abstract class MainDatabase() : RoomDatabase() {
    abstract fun generalConfigDao(): GeneralConfigDao

    companion object {
        private var _instance: MainDatabase? = null
        fun getInstance(): MainDatabase {
            if (_instance == null) {
                val db = Room.databaseBuilder(PauseApp.getApp().applicationContext, MainDatabase::class.java, "main-db");
                _instance = db.build()
            }
            return _instance as MainDatabase
        }
    }
}
