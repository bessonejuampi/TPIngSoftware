package com.example.tpingsoftware.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.databinding.ItemServiceHomeBinding

class HomeServicesAdapter(
    private val listService : List<Service>
) : RecyclerView.Adapter<HomeServicesAdapter.HomeServicesViewHolder>(){


    inner class HomeServicesViewHolder(
        private val context: Context,
        itemView:ItemServiceHomeBinding
    ):RecyclerView.ViewHolder(itemView.root) {
        fun bind(service: Service) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeServicesViewHolder {
        val binding = ItemServiceHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeServicesViewHolder(parent.context ,binding)
    }

    override fun getItemCount(): Int {
       return listService.size
    }

    override fun onBindViewHolder(holder: HomeServicesViewHolder, position: Int) {
        holder.bind(listService[position])
    }
}