package com.ashish.todolist


import androidx.lifecycle.LiveData
import com.ashish.todolist.Notes

class NoteRepository(val notesDao: NotesDao) {

    // on below line we are creating a variable for our list
    // and we are getting all the notes from our DAO class.
    val allNotes: LiveData<List<Notes>> = notesDao.getAllNotes()

    // on below line we are creating an insert method
    // for adding the note to our database.
    suspend fun insert(note: Notes) {
        notesDao.insert(note)
    }

    // on below line we are creating a delete method
    // for deleting our note from database.
    suspend fun delete(note: Notes){
        notesDao.delete(note)
    }

    // on below line we are creating a update method for
    // updating our note from database.
    suspend fun update(note: Notes){
        notesDao.update(note)
    }
}