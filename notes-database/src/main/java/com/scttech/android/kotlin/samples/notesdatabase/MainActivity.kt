package com.scttech.android.kotlin.samples.notesdatabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NotesScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(viewModel: NotesViewModel = viewModel()) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()

    var showForm by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<Note?>(null) }
    val sheetState = rememberModalBottomSheetState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val noteAddedMessage = stringResource(R.string.note_added)
    val noteUpdatedMessage = stringResource(R.string.note_updated)
    val noteDeletedMessage = stringResource(R.string.note_deleted)
    val undoLabel = stringResource(R.string.undo)

    fun openNewNoteForm() {
        editingNote = null
        showForm = true
    }

    fun openEditForm(note: Note) {
        editingNote = note
        showForm = true
    }

    fun saveNote(title: String, body: String) {
        val note = editingNote
        if (note == null) {
            viewModel.addNote(title, body)
            coroutineScope.launch { snackbarHostState.showSnackbar(noteAddedMessage) }
        } else {
            viewModel.updateNote(note, title, body)
            coroutineScope.launch { snackbarHostState.showSnackbar(noteUpdatedMessage) }
        }
        showForm = false
    }

    fun deleteNote(note: Note) {
        viewModel.deleteNote(note)
        coroutineScope.launch {
            val result = snackbarHostState.showSnackbar(
                message = noteDeletedMessage,
                actionLabel = undoLabel,
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.restoreNote(note)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.notes_title)) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { openNewNoteForm() }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_note))
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data -> Snackbar(snackbarData = data) }
        }
    ) { innerPadding ->
        if (notes.isEmpty()) {
            EmptyState(modifier = Modifier.padding(innerPadding))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteCard(
                        note = note,
                        onClick = { openEditForm(note) },
                        onDelete = { deleteNote(note) }
                    )
                }
            }
        }

        if (showForm) {
            NoteFormSheet(
                note = editingNote,
                sheetState = sheetState,
                onDismiss = { showForm = false },
                onSave = ::saveNote
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.empty_state_title),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = stringResource(R.string.empty_state_body),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun NoteCard(note: Note, onClick: () -> Unit, onDelete: () -> Unit) {
    val formatter = remember { SimpleDateFormat("MMM d, yyyy · h:mm a", Locale.getDefault()) }

    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (note.body.isNotBlank()) {
                    Text(
                        text = note.body,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Text(
                    text = formatter.format(note.updatedAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.delete_note))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteFormSheet(
    note: Note?,
    sheetState: androidx.compose.material3.SheetState,
    onDismiss: () -> Unit,
    onSave: (title: String, body: String) -> Unit
) {
    var title by remember(note) { mutableStateOf(note?.title ?: "") }
    var body by remember(note) { mutableStateOf(note?.body ?: "") }
    var titleError by remember(note) { mutableStateOf<String?>(null) }

    val titleRequiredMessage = stringResource(R.string.error_title_required)

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(
                    if (note == null) R.string.new_note_heading else R.string.edit_note_heading
                ),
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    if (titleError != null) titleError = null
                },
                label = { Text(stringResource(R.string.note_title_label)) },
                singleLine = true,
                isError = titleError != null,
                supportingText = { titleError?.let { Text(it) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            OutlinedTextField(
                value = body,
                onValueChange = { body = it },
                label = { Text(stringResource(R.string.note_body_label)) },
                minLines = 3,
                keyboardOptions = KeyboardOptions.Default,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 8.dp)
            ) {
                Button(
                    onClick = {
                        val trimmedTitle = title.trim()
                        if (trimmedTitle.isEmpty()) {
                            titleError = titleRequiredMessage
                        } else {
                            onSave(trimmedTitle, body.trim())
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.save))
                }
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    MaterialTheme {
        EmptyState()
    }
}
