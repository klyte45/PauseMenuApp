package com.halkyproject.pausemenu.database.main.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
class GeneralConfig(@PrimaryKey
                    @ColumnInfo(name = "key")
                    var key: String,
                    @ColumnInfo(name = "value")
                    var value: String) {
}
