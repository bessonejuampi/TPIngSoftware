package com.example.tpingsoftware.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tpingsoftware.databinding.FragmentMyServiceBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.tpingsoftware.ui.viewModels.HomeVIewModel

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

        setupView()

    }

    private fun setupView() {
        binding.fabAddService.setOnClickListener {

            viewModel.goToAddService()
        }
    }
}