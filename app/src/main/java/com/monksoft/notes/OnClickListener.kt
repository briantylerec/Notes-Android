package com.monksoft.notes

interface OnClickListener {
    fun onLongCLick(note: Note, currentAdapter: NoteAdapter)
    fun onChecked(note: Note)
}