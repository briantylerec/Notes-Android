package com.monksoft.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.monksoft.notes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() , OnClickListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var notesAdapter : NoteAdapter
    private lateinit var notesFinishedAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)



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
                val note = Note((notesAdapter.itemCount+1).toLong(), binding.etDescription.text.toString().trim())

                addNoteAuto(note)
                binding.etDescription.text?.clear()
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
        val data = mutableListOf(
            Note(1,"Estudiar"),
            Note(2, "Ir al mercado"))

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
                currentAdapter.remove(note)
            })
            .setNegativeButton("Cancelar", null).show()
    }

    override fun onChecked(note: Note) {
        deleteNodeAuto(note)
        addNoteAuto(note)
    }

    private fun deleteNodeAuto(note: Note) {
        if(note.isFinished) notesAdapter.remove(note)
        else notesFinishedAdapter.remove(note)
    }
}