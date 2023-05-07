package com.ashish.todolist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "notesTable")
data class Notes(
    @ColumnInfo(name = "notes") val notes: String,
    @ColumnInfo(name = "time")val time: String,
    @ColumnInfo(name = "date", defaultValue = "")var date: String,
    @ColumnInfo(name = "checked") var isChecked: Boolean, @PrimaryKey(autoGenerate = true) var id: Int = 0)


