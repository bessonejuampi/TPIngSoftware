package com.example.tpingsoftware.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tpingsoftware.R
import com.example.tpingsoftware.databinding.FragmentServiceBinding
import com.example.tpingsoftware.ui.view.adapters.HomeServicesAdapter
import com.example.tpingsoftware.ui.viewModels.HomeVIewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ServiceFragment : Fragment() {

    private lateinit var binding : FragmentServiceBinding

    private val viewModel: HomeVIewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentServiceBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllService()

        observeMutableLiveData()

    }

    private fun observeMutableLiveData() {

        viewModel.listServiceLiveData.observe(requireActivity(), Observer { listService ->

            binding.rvAllServices.layoutManager = GridLayoutManager(requireContext(), 2)
            val adapter = HomeServicesAdapter(listService, viewModel)
            binding.rvAllServices.adapter = adapter
        })
    }

}