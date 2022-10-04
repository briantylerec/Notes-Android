package com.monksoft.notes

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper (context: Context) : SQLiteOpenHelper(context,
    Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE ${Constants.ENTITY_NOTE} (" +
                "${Constants.PROPERTY_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${Constants.PROPERTY_DESCRIPTION} varchar(20), " +
                "${Constants.PROPERTY_IS_FINISHED} BOOLEAN)"

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun conn(): SQLiteDatabase {
        return this.readableDatabase
    }

    @SuppressLint("Range")
    fun getAllNotes() : MutableList<Note>{
        val notes: MutableList<Note> = mutableListOf()
        val db = conn()
        val query = "SELECT * FROM ${Constants.ENTITY_NOTE}"

        val result = db.rawQuery(query, null)

        if(result.moveToFirst()){
            do{
                val note = Note()
                note.id = result.getLong(result.getColumnIndex(Constants.PROPERTY_ID))
                note.description = result.getString(result.getColumnIndex(Constants.PROPERTY_DESCRIPTION))
                note.isFinished = result.getInt(result.getColumnIndex(Constants.PROPERTY_IS_FINISHED)) == 1

                notes.add(note)
            } while (result.moveToNext())
        }
        return notes
    }

    fun insertNote(note: Note) : Long {
        val db = conn()
        val contentValues = ContentValues().apply{
            put(Constants.PROPERTY_DESCRIPTION, note.description)
            put(Constants.PROPERTY_IS_FINISHED, note.isFinished)
        }
        return db.insert(Constants.ENTITY_NOTE, null, contentValues)
    }

    fun updateNote(note: Note) : Boolean {
        val db = conn()
        val contentValues = ContentValues().apply{
            put(Constants.PROPERTY_DESCRIPTION, note.description)
            put(Constants.PROPERTY_IS_FINISHED, note.isFinished)
        }
        return db.update(Constants.ENTITY_NOTE,
             contentValues,
            "${Constants.PROPERTY_ID} = ${note.id}",
            null) == 1
    }

    fun deleteNote(note: Note): Boolean{
        val db = conn()
        return db.delete(Constants.ENTITY_NOTE, "${Constants.PROPERTY_ID} = ${note.id}",null) == 1
    }
}