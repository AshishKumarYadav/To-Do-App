package com.ashish.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashish.todolist.adapter.RecyclerAdapter
import com.ashish.todolist.databinding.ActivityMainBinding
import com.ashish.todolist.viewmodel.NoteViewModal

class MainActivity : AppCompatActivity(), View.OnClickListener,RecyclerAdapter.NoteCheckedInterface,RecyclerAdapter.DeleteNotes {

    private lateinit var binding: ActivityMainBinding
    lateinit var myAdapter: RecyclerAdapter
    lateinit var viewModal: NoteViewModal


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        myAdapter = RecyclerAdapter(this,this,this)

        setRecyclerView()

        binding.fab.setOnClickListener(this)

        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModal::class.java)

        Log.d("MAINACTIVITY_","notes "+viewModal.allNotes)

        viewModal.allNotes.observe(this, Observer { list ->
            list?.let {
                // on below line we are updating our list.
                myAdapter.updateList(it)
            }
        })
    }

    private fun setRecyclerView(){
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = myAdapter

        }
    }
    override fun onClick(v: View?) {
        when(v){
            binding.fab -> addNewItemDialog()
        }
    }

    private fun addNewItemDialog() {
        val alert = AlertDialog.Builder(this)
        val itemEditText = EditText(this)
        alert.setMessage("Add new notes")
        alert.setTitle("Enter To Do Item Text")
        alert.setView(itemEditText)
        alert.setPositiveButton("Add") { dialog, positiveButton ->
            if(!itemEditText.text.toString().isNullOrEmpty()){
                val note = Notes(itemEditText.text.toString(),"09:00 AM",false)
                viewModal.addNote(note)
            } else Toast.makeText(this,"Please add some notes",Toast.LENGTH_LONG).show()
        }
        alert.show()
    }

    override fun itemChecked(note: Notes) {
       viewModal.updateNote(note)
    }

    override fun deleteNote(note: Notes) {
        viewModal.deleteNote(note)
    }

}