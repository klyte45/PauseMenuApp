package com.halkyproject.pausemenu.database.main.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.halkyproject.pausemenu.database.main.entities.GeneralConfig


@Dao
interface GeneralConfigDao {
    @get:Query("SELECT * FROM generalconfig")
    val all: LiveData<List<GeneralConfig>>

    @Query("SELECT * FROM generalconfig WHERE `key` IN (:keys)")
    fun loadAllByKeys(keys: Array<String>): LiveData<List<GeneralConfig>>

    @Query("SELECT * FROM generalconfig WHERE `key` = :key")
    fun getByKey(key: String): LiveData<GeneralConfig?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg values: GeneralConfig)

    @Delete
    fun delete(item: GeneralConfig)
}
