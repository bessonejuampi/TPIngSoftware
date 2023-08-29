package com.example.tpingsoftware.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Availability
import com.example.tpingsoftware.databinding.ItemAvailabilityBinding

class AddAvailabilityAdapter(val listItems : ArrayList<Availability>) : RecyclerView.Adapter<AddAvailabilityAdapter.AddAvailabilityViewHolder>() {

    inner class AddAvailabilityViewHolder(
        private val context : Context,
        itemView : ItemAvailabilityBinding
    ) : RecyclerView.ViewHolder(itemView.root){

        fun bind(availability : Availability){

            itemView.findViewById<EditText>(R.id.etAddDate).setText(availability.date)
            itemView.findViewById<EditText>(R.id.etAddHour).setText(availability.hour)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddAvailabilityViewHolder {

        val binding = ItemAvailabilityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddAvailabilityViewHolder(parent.context ,binding)
    }

    override fun getItemCount(): Int {

        return listItems.size
    }

    override fun onBindViewHolder(holder: AddAvailabilityViewHolder, position: Int) {

        holder.bind(listItems[position])
    }
}