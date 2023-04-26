package com.ashish.todolist.adapter

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ashish.todolist.Notes
import com.ashish.todolist.R

class RecyclerAdapter(context: Context,val notesCheckedInterface: NoteCheckedInterface,val deleteNote:DeleteNotes
): RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>(){

    private val allNotes = ArrayList<Notes>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
            val checkBox:CheckBox =  itemView.findViewById(R.id.itemCheckbox)
            val timeText:TextView =  itemView.findViewById(R.id.timeText)
            val tvText:TextView =  itemView.findViewById(R.id.tvText)
            val deleteBtn:ImageButton =  itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_list, parent, false)
        return RecyclerAdapter.MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvText.text = allNotes.get(position).notes

        holder.timeText.text = "8:00 AM"
        Log.d("TODO ","Adapter "+allNotes.get(position).isChecked)
        if (allNotes.get(position).isChecked) {

            holder.tvText.setPaintFlags(holder.tvText.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            holder.checkBox.isChecked = true
            holder.deleteBtn.visibility = View.VISIBLE


        } else {

            holder.tvText.setPaintFlags(holder.tvText.getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv())
            holder.deleteBtn.visibility = View.INVISIBLE
        }

        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            allNotes[position].isChecked = isChecked
            notesCheckedInterface.itemChecked(allNotes[position])
        }

        holder.deleteBtn.setOnClickListener {
            deleteNote.deleteNote(allNotes[position])
        }
    }
    // below method is use to update our list of notes.
    fun updateList(newList: List<Notes>) {
        // on below line we are clearing
        // our notes array list
        allNotes.clear()
        // on below line we are adding a
        // new list to our all notes list.
        allNotes.addAll(newList)
        // on below line we are calling notify data
        // change method to notify our adapter.
        notifyDataSetChanged()
    }
    interface NoteCheckedInterface{
        fun itemChecked(note:Notes)
    }
    interface DeleteNotes{
        fun deleteNote(note:Notes)
    }
}