package com.example.tpingsoftware.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Appreciation
import com.example.tpingsoftware.databinding.ItemAppreciationsBinding

class AppreciationsAdapter(
    private val listAppreciation: List<Appreciation>
) : RecyclerView.Adapter<AppreciationsAdapter.AppreciationsViewHolder>(){

    inner class AppreciationsViewHolder(
        private val itemView: ItemAppreciationsBinding,
        private val context: Context
    ):RecyclerView.ViewHolder(itemView.root){

        fun bind(appreciation: Appreciation){

            itemView.findViewById<RatingBar>(R.id.rbAppreciations).rating = appreciation.rating.toFloat()
            itemView.findViewById<TextView>(R.id.tvComment).text = appreciation.comment
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppreciationsViewHolder {
        val binding = ItemAppreciationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppreciationsViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return listAppreciation.size
    }

    override fun onBindViewHolder(holder: AppreciationsViewHolder, position: Int) {
        holder.bind(listAppreciation[position])
    }

}