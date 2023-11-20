package com.example.tugasroom2

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasroom2.database.Note
import com.example.tugasroom2.database.NoteDao
import com.example.tugasroom2.database.NoteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class dataAdapter(val dataNotes: List<Note>?): RecyclerView.Adapter<dataAdapter.MyViewHolder>(){
    lateinit var executorService: ExecutorService
    lateinit var mNotesDao: NoteDao

    class MyViewHolder (view: View):RecyclerView.ViewHolder(view){
        //Mendeklarasikan kelas MyViewHolder yang mewakilkan tampilan setiap item dalam daftar.
        // ViewHolder ini memiliki referensi ke elemen-elemen UI seperti judul (tittle),
        // deskripsi (desc), tanggal (date), dan tombol update dan delete.
        val tittle = view.findViewById<TextView>(R.id.title_txt)
        val desc = view.findViewById<TextView>(R.id.desc_txt)
        val date = view.findViewById<TextView>(R.id.date_txt)
        val btnUpdate = view.findViewById<Button>(R.id.btn_update)
        val btnDelete = view.findViewById<Button>(R.id.btn_delete)
    }

    //Membuat dan mengembalikan instance dari MyViewHolder
    // dengan menggunakan layout inflater untuk menginisialisasi tampilan.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes, parent, false)
        return MyViewHolder(view)
    }

    //mengembalikan jumlah item dalam daftar.
    // Jika dataNotes tidak null, mengembalikan jumlah item; jika null, mengembalikan 0.
    override fun getItemCount(): Int {
        if(dataNotes!=null){
            return dataNotes.size
        }
        return 0
    }

    //Menginisialisasi executorService dan mendapatkan instance NoteDao dari database Room.
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(holder.itemView.context)
        mNotesDao  = db!!.noteDao()!!

        //Menetapkan nilai dari objek Note ke elemen-elemen UI dalam ViewHolder.
        holder.tittle.text = "tittle : ${dataNotes?.get(position)?.title}"
        holder.desc.text = "description : ${dataNotes?.get(position)?.description}"
        holder.date.text = "date : ${dataNotes?.get(position)?.date}"

        //Menetapkan aksi untuk tombol update dan delete. Jika tombol update diklik, membuka InputActivity untuk mengedit catatan.
        // Jika tombol delete diklik, menghapus catatan dengan memanggil fungsi deleteNoteById.
        holder.btnUpdate.setOnClickListener{
            val intentToDetail = Intent(holder.itemView.context, InputActivity::class.java)
            intentToDetail.putExtra("ID", dataNotes?.get(position)?.id)
            intentToDetail.putExtra("TITTLE", dataNotes?.get(position)?.title)
            intentToDetail.putExtra("DESC", dataNotes?.get(position)?.description)
            intentToDetail.putExtra("DATE", dataNotes?.get(position)?.date)
            intentToDetail.putExtra("COMMAND", "UPDATE")
            holder.itemView.context.startActivity(intentToDetail)
        }

        holder.btnDelete.setOnClickListener{
            val noteId = dataNotes?.get(position)?.id
            noteId?.let { deleteNoteById(it) }
            Toast.makeText(holder.itemView.context, "Berhasil Menghapus Data", Toast.LENGTH_SHORT).show()
            true
        }
    }

    //Menghapus catatan dari database menggunakan executorService di latar belakang.
    // Fungsi ini dipanggil saat tombol delete diklik.
    private fun deleteNoteById(noteId: Int) {
        executorService.execute {
            mNotesDao.deleteById(noteId)
        }
    }

}