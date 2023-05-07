package com.ashish.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashish.todolist.adapter.RecyclerAdapter
import com.ashish.todolist.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener,RecyclerAdapter.NoteCheckedInterface,RecyclerAdapter.DeleteNotes {

    private lateinit var binding: ActivityMainBinding
    lateinit var myAdapter: RecyclerAdapter
    lateinit var viewModal: NoteViewModal
    var time = ""
    var date = ""
    var timeInMilli:Long = 0L


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
        )[NoteViewModal::class.java]

        Log.d("MAINACTIVITY_","notes "+viewModal.allNotes)
        viewModal.getAllNotes()
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
            binding.fab -> showDialog()
        }
    }
    private fun showDialog() {
        val dialog = AlertDialog.Builder(this)
        val dialogView = this.layoutInflater.inflate(R.layout.custom_layout_dialog, null)
        dialog.setCancelable(false)
        dialog.setTitle("Add new notes")
        dialog.setView(dialogView)

        val notes = dialogView.findViewById(R.id.notes) as EditText
        val timeView = dialogView.findViewById(R.id.timeView) as Button
        val dateView = dialogView.findViewById(R.id.dateView) as Button
        val cancelBtn = dialogView.findViewById(R.id.cancel_btn) as Button
        val addBtn = dialogView.findViewById(R.id.add_btn) as Button


        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        var minute = mcurrentTime.get(Calendar.MINUTE)
        var cal = Calendar.getInstance()

        // to pre-populate view
        time =String.format("%d : %02d", hour, minute)+if (hour < 12) " AM" else " PM"
        timeView.text =time

        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        date = sdf.format(cal.time)
        dateView!!.text = date
        timeInMilli = cal.timeInMillis

        // create an OnDateSetListener
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)


                val myFormat = "dd/MM/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                date = sdf.format(cal.time)
                dateView!!.text = date
                timeInMilli = cal.timeInMillis
            }

        dateView.setOnClickListener {

                DatePickerDialog(this@MainActivity,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()

        }

        mTimePicker = TimePickerDialog(this,
            { view, hourOfDay, minute ->
                val am_pm = if (hourOfDay < 12) "AM" else "PM"
                time = String.format("%d : %d", hourOfDay, minute)+" $am_pm"
                timeView.text = time
            }, hour, minute, false)

        timeView.setOnClickListener {
            mTimePicker.show()
        }

        val alertDialog = dialog.create()

        addBtn.setOnClickListener {
            if(!notes.text.toString().isNullOrEmpty()){
                val note = Notes(notes.text.toString(),time,date,false)
                viewModal.addNote(note)
                alertDialog.cancel()
            } else {
                notes.error = "Add notes here"
                Toast.makeText(this,"Please add some notes",Toast.LENGTH_LONG).show()

            }

        }
        cancelBtn.setOnClickListener {
            alertDialog.cancel()
        }
        alertDialog.show()
    }

    override fun itemChecked(note: Notes) {
        Log.d("TODO ", "MainActivity_ItemClicked $note")
        viewModal.updateNote(note)
    }

    override fun deleteNote(note: Notes) {
        viewModal.deleteNote(note)
    }

}