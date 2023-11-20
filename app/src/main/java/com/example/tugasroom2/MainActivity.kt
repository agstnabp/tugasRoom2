package com.example.tugasroom2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasroom2.R
import com.example.tugasroom2.database.Note
import com.example.tugasroom2.database.NoteDao
import com.example.tugasroom2.database.NoteRoomDatabase
import com.example.tugasroom2.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService
    private lateinit var ArrayData : LiveData<List<Note>>
    private var updateId: Int=0

    //Menginisialisasi variabel dan properti, mendapatkan instance NoteDao dan LiveData dari database Room,
    // dan memanggil fungsi getAllNotes untuk menampilkan semua catatan.
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao  = db!!.noteDao()!!
        ArrayData = db!!.noteDao()!!.allNotes
        getAllNotes()

        with(binding) {
            btnadd.setOnClickListener{
                val intentToInput = Intent(this@MainActivity, InputActivity::class.java)
                intentToInput.putExtra("COMMAND", "ADD")
                startActivity(intentToInput)
            }
        }
    }
    private fun getAllNotes() {
        mNotesDao.allNotes.observe(this) { notes ->
            //Jika daftar catatan tidak kosong, menampilkan RecyclerView dengan adapter yang sesuai;
            // jika kosong, menampilkan pesan bahwa tidak ada catatan.
            if (notes.isNotEmpty()) { // Periksa apakah daftar catatan tidak kosong
                binding.rvnotes.isVisible = true
                binding.textEmpty.isVisible = false
                val recyclerAdapter = dataAdapter(notes)
                binding.rvnotes.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    setHasFixedSize(true)
                    adapter = recyclerAdapter
                }
            }else{
                binding.rvnotes.isVisible = false
                binding.textEmpty.isVisible = true
            }
        }
    }

    //untuk melakukan operasi CRUD pada database menggunakan executorService di latar belakang.
    private fun insert(note: Note) {
        executorService.execute { mNotesDao.insert(note) }
    }

    private fun delete(note: Note) {
        executorService.execute { mNotesDao.delete(note) }
    }

    private fun update(note: Note) {
        executorService.execute { mNotesDao.update(note) }
    }

    //dipanggil saat aktivitas ditampilkan kembali.
    // Memanggil kembali fungsi getAllNotes untuk memperbarui tampilan setelah kembali dari aktivitas lain.
    override fun onResume() {
        super.onResume()
        getAllNotes()
    }
}