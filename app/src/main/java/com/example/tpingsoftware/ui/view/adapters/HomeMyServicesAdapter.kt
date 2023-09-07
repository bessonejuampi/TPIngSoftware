package com.example.tpingsoftware.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.databinding.ItemServiceHomeBinding
import com.squareup.picasso.Picasso

class HomeMyServicesAdapter(
    private val listService : ArrayList<Service>
) : RecyclerView.Adapter<HomeMyServicesAdapter.HomeMyServicesViewHolder>() {

    inner class HomeMyServicesViewHolder(
        private val itemView : ItemServiceHomeBinding
    ):RecyclerView.ViewHolder(itemView.root){

        fun bind(service: Service){

            itemView.findViewById<TextView>(R.id.tvTitle).text = service.title

            if (service.idImage != null){
                Picasso.get().load(service.imageUir).into(itemView.findViewById<ImageView>(R.id.ivServiceHome))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMyServicesViewHolder {

        val binding = ItemServiceHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeMyServicesViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return listService.size
    }

    override fun onBindViewHolder(holder: HomeMyServicesViewHolder, position: Int) {

        holder.bind(listService[position])
    }
}