package com.ashish.todolist

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {

    @Insert
    suspend fun insert(notes: Notes)

    @Delete
    suspend fun delete(note: Notes)

    @Query("Select * from notesTable order by id ASC")
    fun getAllNotes(): LiveData<List<Notes>>

    // below method is use to update the note.
    @Update
    suspend fun update(note: Notes)

}
