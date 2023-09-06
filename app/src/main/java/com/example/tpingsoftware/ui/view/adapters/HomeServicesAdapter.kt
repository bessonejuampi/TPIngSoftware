package com.example.tpingsoftware.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.databinding.ItemServiceHomeBinding
import com.example.tpingsoftware.ui.viewModels.HomeVIewModel
import com.squareup.picasso.Picasso

class HomeServicesAdapter(
    private val listService : List<Service>,
    private val viewModel : HomeVIewModel
) : RecyclerView.Adapter<HomeServicesAdapter.HomeServicesViewHolder>(){


    inner class HomeServicesViewHolder(
        private val context: Context,
        private val itemView:ItemServiceHomeBinding
    ):RecyclerView.ViewHolder(itemView.root) {
        fun bind(service: Service) {

            itemView.findViewById<TextView>(R.id.tvTitle).text = service.title
            itemView.findViewById<TextView>(R.id.tvDescription).text = service.description

            if (service.idImage != null){
                Picasso.get().load(service.imageUir).into(itemView.findViewById<ImageView>(R.id.ivServiceHome))
            }

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