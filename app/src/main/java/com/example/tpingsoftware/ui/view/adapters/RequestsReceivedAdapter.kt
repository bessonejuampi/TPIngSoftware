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
import com.example.tpingsoftware.databinding.ItemRequestsReceivedBinding
import com.example.tpingsoftware.ui.viewModels.RequestReceivedViewModel

class RequestsReceivedAdapter(
    private val listRequestReceived : List<Request>,
    private val listService : List<Service>,
    private val viewModel : RequestReceivedViewModel
) : RecyclerView.Adapter<RequestsReceivedAdapter.RequestReceiverViewHolder>() {

    inner class RequestReceiverViewHolder(
        private val context: Context,
        private val itemView : ItemRequestsReceivedBinding
    ) : RecyclerView.ViewHolder(itemView.root){

        fun bind(request : Request){

                listService.forEach { service ->
                 if (request.idService == service.id){
                     itemView.findViewById<TextView>(R.id.tvSolicitedService).text = "Servicio requerido: ${service.title}"
                 }
                }


            itemView.findViewById<TextView>(R.id.tvRequestingUser).text = "Usuario solicitante: ${request.idRequestingUser}"

            when(request.state) {
                TypeStates.PENDING ->{
                  itemView.findViewById<TextView>(R.id.tvState).text = "Estado: Pendiente"
                  itemView.findViewById<LinearLayout>(R.id.llButtonsPending).visibility = View.VISIBLE
                }
                TypeStates.FINISHED -> {
                    itemView.findViewById<TextView>(R.id.tvState).text = "Estado: Finalizado"
                }
                TypeStates.ACCEPTED -> {
                    itemView.findViewById<TextView>(R.id.tvState).text = "Estado: Aceptado"
                    itemView.findViewById<LinearLayout>(R.id.llBtnAccepted).visibility = View.VISIBLE

                }
                TypeStates.REJECTED -> {
                    itemView.findViewById<TextView>(R.id.tvState).text = "Estado: Rechazado"
                }
                TypeStates.ERROR ->  itemView.findViewById<TextView>(R.id.tvState).text = ""
            }

            itemView.findViewById<Button>(R.id.btnAcceptService).setOnClickListener {
                viewModel.acceptRequest(request)
            }

            itemView.findViewById<Button>(R.id.btnFinishService).setOnClickListener {
                viewModel.finishRequest(request)
            }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestReceiverViewHolder {

        val binding = ItemRequestsReceivedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestReceiverViewHolder(parent.context, binding)
    }

    override fun getItemCount(): Int {
        return listRequestReceived.size
    }

    override fun onBindViewHolder(holder: RequestReceiverViewHolder, position: Int) {

        holder.bind(listRequestReceived[position])
    }
}