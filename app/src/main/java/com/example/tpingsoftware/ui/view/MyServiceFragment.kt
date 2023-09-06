package com.example.tpingsoftware.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tpingsoftware.databinding.FragmentMyServiceBinding
import com.example.tpingsoftware.ui.view.adapters.HomeMyServicesAdapter
import com.example.tpingsoftware.ui.view.adapters.HomeServicesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.tpingsoftware.ui.viewModels.HomeVIewModel
import com.example.tpingsoftware.utils.AppPreferences

class MyServiceFragment : Fragment() {

    private lateinit var binding : FragmentMyServiceBinding

    private val viewModel: HomeVIewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return FragmentMyServiceBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getServiceFromUser(AppPreferences.getUserSession(requireContext())!!)

        setupView()

        observeMutableLiveData()

    }

    private fun observeMutableLiveData() {

        viewModel.listServiceFromUserLiveData.observe(requireActivity(), Observer {

            binding.rvServicesFromUser.layoutManager = GridLayoutManager(requireContext(), 2)
            val adapter = HomeMyServicesAdapter(it)
            binding.rvServicesFromUser.adapter = adapter
        })
    }

    private fun setupView() {


        binding.fabAddService.setOnClickListener {

            viewModel.goToAddService()
        }
    }
}