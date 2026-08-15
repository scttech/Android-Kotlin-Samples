package com.scttech.android.kotlin.samples.notesdatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Owns the [NoteDao] and exposes the notes table as a [StateFlow], keeping all database
 * access off the UI thread and behind coroutines. The Compose UI never touches the DAO
 * directly — it only reads [notes] and calls [addNote] / [updateNote] / [deleteNote].
 */
class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = NotesDatabase.getInstance(application).noteDao()

    val notes: StateFlow<List<Note>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addNote(title: String, body: String) {
        viewModelScope.launch {
            dao.insert(Note(title = title, body = body))
        }
    }

    fun updateNote(note: Note, title: String, body: String) {
        viewModelScope.launch {
            dao.update(note.copy(title = title, body = body, updatedAt = System.currentTimeMillis()))
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            dao.delete(note)
        }
    }

    /** Re-inserts a previously deleted note with the same id, powering the Snackbar "Undo" action. */
    fun restoreNote(note: Note) {
        viewModelScope.launch {
            dao.insert(note)
        }
    }
}
