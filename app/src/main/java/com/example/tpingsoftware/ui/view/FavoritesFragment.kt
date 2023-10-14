package com.example.tpingsoftware.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tpingsoftware.R
import com.example.tpingsoftware.databinding.FragmentFavoritesBinding
import com.example.tpingsoftware.ui.view.adapters.HomeServicesAdapter
import com.example.tpingsoftware.ui.viewModels.HomeVIewModel
import com.example.tpingsoftware.utils.AppPreferences
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    private val viewModel: HomeVIewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return FragmentFavoritesBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoritesServices(AppPreferences.getUserSession(requireContext())!!)
        showProgress()

        observeMutableLiveData()
    }

    private fun observeMutableLiveData() {

        viewModel.listServiceFavoritesFromUserLiveData.observe(requireActivity(), Observer {

            hideProgress()
            if (it.isNotEmpty()){
                binding.rvFavoritesServices.layoutManager = GridLayoutManager(requireContext(), 2)

                val adapter = HomeServicesAdapter(it, viewModel)
                binding.rvFavoritesServices.adapter = adapter
            }else{
                showEmptyView()
            }

        })
    }

    private fun showProgress() {

        binding.lyProgress.visibility = View.VISIBLE
    }

    private fun hideProgress() {

        binding.lyProgress.visibility = View.GONE
    }

    private fun showEmptyView(){
        binding.llEmptyView.visibility = View.VISIBLE
        binding.rvFavoritesServices.visibility = View.GONE
    }

}