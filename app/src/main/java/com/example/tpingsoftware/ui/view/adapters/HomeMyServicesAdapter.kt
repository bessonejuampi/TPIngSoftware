package com.example.tpingsoftware.ui.view.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.databinding.ItemServiceHomeBinding
import com.example.tpingsoftware.ui.view.EditMyServiceActivity
import com.example.tpingsoftware.utils.Constants
import com.squareup.picasso.Picasso

class HomeMyServicesAdapter(
    private val listService : ArrayList<Service>
) : RecyclerView.Adapter<HomeMyServicesAdapter.HomeMyServicesViewHolder>() {

    inner class HomeMyServicesViewHolder(
        private val itemView : ItemServiceHomeBinding,
        private val context: Context
    ):RecyclerView.ViewHolder(itemView.root){

        fun bind(service: Service){

            itemView.findViewById<TextView>(R.id.tvTitle).text = service.title

            if (service.idImage != null){
                Picasso.get().load(service.imageUir).into(itemView.findViewById<ImageView>(R.id.ivServiceHome))
            }

            itemView.setOnClickListener {

                val bundle = Bundle()
                bundle.putParcelable(Constants.KEY_SERVICE, service)

                val intent = Intent(context, EditMyServiceActivity::class.java)
                intent.putExtra(Constants.KEY_BUNDLE_SERVICE_TO_EDIT_MY_SERVICE, bundle)

                context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMyServicesViewHolder {

        val binding = ItemServiceHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeMyServicesViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {

        return listService.size
    }

    override fun onBindViewHolder(holder: HomeMyServicesViewHolder, position: Int) {

        holder.bind(listService[position])
    }
}