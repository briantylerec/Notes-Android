package com.monksoft.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.monksoft.notes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() , OnClickListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var notesAdapter : NoteAdapter
    private lateinit var notesFinishedAdapter: NoteAdapter
    private lateinit var  database: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = DataBaseHelper(this)

        notesAdapter = NoteAdapter(mutableListOf(), this)
        binding.rvNotes.apply{
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = notesAdapter
        }

        notesFinishedAdapter = NoteAdapter(mutableListOf(), this)
        binding.rvNotesFinished.apply{
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = notesFinishedAdapter
        }

        binding.btnAdd.setOnClickListener {
            if(binding.etDescription.text.toString().isNotBlank()){
                val note = Note(description = binding.etDescription.text.toString().trim())

                if (database.insertNote(note) != -1L){
                    addNoteAuto(note)
                    binding.etDescription.text?.clear()
                    Snackbar.make(binding.root, "Ingreso exitoso", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.root, "Error al ingresar en la base de datos", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                binding.etDescription.error = "Campor requerido"
            }
        }
    }

    override fun onStart() {
        super.onStart()
        getData()
    }

    private fun getData(){
//        val data = mutableListOf(
//            Note(1,"Estudiar"),
//            Note(2, "Ir al mercado"))

        val data = database.getAllNotes()

        data.forEach { note ->
            addNoteAuto(note)
        }
    }

    private fun addNoteAuto(note: Note) {
        if(note.isFinished) notesFinishedAdapter.add(note)
        else notesAdapter.add(note)
    }

    override fun onLongCLick(note: Note, currentAdapter: NoteAdapter) {
        var builder = AlertDialog.Builder(this)
            .setTitle("Eliminar nota?")
            .setPositiveButton("Eliminar", { dialogInterface, i ->
                if (database.deleteNote(note)){
                    currentAdapter.remove(note)
                    Snackbar.make(binding.root, "Eliminacion exitosa", Snackbar.LENGTH_SHORT)
                } else{
                    Snackbar.make(binding.root, "No se puede eliminar", Snackbar.LENGTH_SHORT)
                }
            })
            .setNegativeButton("Cancelar", null).show()
    }

    override fun onChecked(note: Note) {
        if(database.updateNote(note)) {
            deleteNodeAuto(note)
            addNoteAuto(note)
        } else {
            Snackbar.make(binding.root, "No se puede actualizar", Snackbar.LENGTH_SHORT)
        }
    }

    private fun deleteNodeAuto(note: Note) {
        if(note.isFinished) notesAdapter.remove(note)
        else notesFinishedAdapter.remove(note)
    }
}