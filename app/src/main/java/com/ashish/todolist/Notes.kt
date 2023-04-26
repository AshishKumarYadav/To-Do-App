package com.ashish.todolist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notesTable")
data class Notes(
    @ColumnInfo(name = "notes") val notes: String,
    @ColumnInfo(name = "time")val time: String,
    @ColumnInfo(name = "checked") var isChecked: Boolean = false, @PrimaryKey(autoGenerate = true) var id: Int = 0
)
