package com.example.tpingsoftware.ui.view.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.databinding.ItemServiceHomeBinding
import com.example.tpingsoftware.ui.view.DetailServiceActivity
import com.example.tpingsoftware.ui.viewModels.HomeVIewModel
import com.example.tpingsoftware.utils.Constants
import com.squareup.picasso.Picasso

class HomeServicesAdapter(
    private var listService : List<Service>,
    private val viewModel : HomeVIewModel
) : RecyclerView.Adapter<HomeServicesAdapter.HomeServicesViewHolder>(){

    fun clear(){
        listService = arrayListOf()
        notifyDataSetChanged()
    }

    inner class HomeServicesViewHolder(
        private val context: Context,
        private val itemView:ItemServiceHomeBinding
    ):RecyclerView.ViewHolder(itemView.root) {
        fun bind(service: Service) {

            itemView.findViewById<TextView>(R.id.tvTitle).text = service.title

            if (service.idImage != null){
                Picasso.get().load(service.imageUir).into(itemView.findViewById<ImageView>(R.id.ivServiceHome))
            }

            itemView.setOnClickListener {

                val bundle = Bundle()
                bundle.putParcelable(Constants.KEY_SERVICE, service)

                val intent = Intent(context, DetailServiceActivity::class.java)
                intent.putExtra(Constants.KEY_BUNDLE_SERVICE_TO_DETAILS, bundle)

                context.startActivity(intent)
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