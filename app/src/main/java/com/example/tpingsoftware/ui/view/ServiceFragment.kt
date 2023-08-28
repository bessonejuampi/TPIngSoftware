package com.example.tpingsoftware.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tpingsoftware.R
import com.example.tpingsoftware.databinding.FragmentServiceBinding

class ServiceFragment : Fragment() {

    private lateinit var binding : FragmentServiceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentServiceBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvAllServices.layoutManager = GridLayoutManager(requireContext(), 2)
        //val adapter = HomeServicesAdapter(/*TODO:pasar listado de service*/)

        //binding.rvAllServices.adapter = adapter
    }

}