package com.example.tpingsoftware.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Request
import com.example.tpingsoftware.data.models.Service
import com.example.tpingsoftware.data.models.TypeStates
import com.example.tpingsoftware.databinding.ItemHiredServicesBinding

class HiredServicesAdapter(
    private val listRequest:List<Request>
):RecyclerView.Adapter<HiredServicesAdapter.HiredServicesViewHolder>() {

    inner class HiredServicesViewHolder(
        private val itemView : ItemHiredServicesBinding,
        private val context: Context
    ):RecyclerView.ViewHolder(itemView.root){

        fun bind(request : Request){

            itemView.findViewById<TextView>(R.id.tvSolicitedService).text = "Servicio requerido: ${request.titleService}"

            when(request.state) {
                TypeStates.PENDING ->{
                    itemView.findViewById<TextView>(R.id.tvState).text = "Estado: Pendiente"
                }
                TypeStates.FINISHED -> {
                    itemView.findViewById<TextView>(R.id.tvState).text = "Estado: Finalizado"
                    itemView.findViewById<TextView>(R.id.llBtnAppreciate).visibility = View.VISIBLE
                }
                TypeStates.ACCEPTED -> {
                    itemView.findViewById<TextView>(R.id.tvState).text = "Estado: Aceptado"
                }
                TypeStates.REJECTED -> {
                    itemView.findViewById<TextView>(R.id.tvState).text = "Estado: Rechazado"
                }
                TypeStates.ERROR ->  itemView.findViewById<TextView>(R.id.tvState).text = ""
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiredServicesViewHolder {

        val binding = ItemHiredServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HiredServicesViewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {

        return listRequest.size
    }

    override fun onBindViewHolder(holder: HiredServicesViewHolder, position: Int) {
        holder.bind(listRequest[position])
    }
}