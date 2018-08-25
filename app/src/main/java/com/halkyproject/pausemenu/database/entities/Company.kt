package com.halkyproject.pausemenu.database.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.halkyproject.pausemenu.enum.Country
import java.io.Serializable


@Entity
abstract class Company() : Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", index = true)
    var id: Int? = null
    @ColumnInfo(name = "mainName")
    lateinit var mainName: String
    @ColumnInfo(name = "realName")
    lateinit var realName: String
    @ColumnInfo(name = "cityDisplayName")
    lateinit var cityDisplayName: String
    @ColumnInfo(name = "addressMapsCode")
    lateinit var addressMapsCode: String
    @ColumnInfo(name = "documentNumber")
    lateinit var documentNumber: String
    @ColumnInfo(name = "country", index = true)
    lateinit var country: Country

}
