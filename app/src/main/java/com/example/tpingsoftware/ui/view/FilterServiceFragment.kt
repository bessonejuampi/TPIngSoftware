package com.example.tpingsoftware.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.DialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.Observer
import com.example.tpingsoftware.R
import com.example.tpingsoftware.data.models.Location
import com.example.tpingsoftware.data.models.Province
import com.example.tpingsoftware.data.models.User
import com.example.tpingsoftware.databinding.FragmentAppreciateBinding
import com.example.tpingsoftware.databinding.FragmentFilterServiceBinding
import com.example.tpingsoftware.ui.viewModels.HomeVIewModel

class FilterServiceFragment : DialogFragment() {

    private lateinit var binding: FragmentFilterServiceBinding

    private var filterListener: ((String, String?) -> Unit)? = null

    private val viewModel : HomeVIewModel by viewModel()

    var provinces : ArrayList<Province>? = null

    private var provinceSelected : String? = null
    private var locationSelected : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return FragmentFilterServiceBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeMutableLiveData()
    }

    private fun observeMutableLiveData() {

        viewModel.listProvinceLiveData.observe(this, Observer { provinces ->
            setProvincesExposedDropdownMenu(provinces)
        })

        viewModel.listLocalitiesLiveData.observe(this, Observer { localities ->
            setLocalitiesExposedDropdownMenu(localities)
        })
    }

    private fun setupView() {
        viewModel.getProvinces()

        binding.actvProvince.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedOption = parent.getItemAtPosition(position).toString()
            provinceSelected = selectedOption
            if (provinces != null){
                provinces?.forEach { province ->
                    if (selectedOption == province.name){
                        viewModel.getLocalities(province.id)
                    }
                }
            }
        }

        binding.actvLocalities.onItemClickListener = AdapterView.OnItemClickListener{ parent, view, position, id ->
            locationSelected = parent.getItemAtPosition(position).toString()
        }

        binding.btnFilter.setOnClickListener {

            if (!provinceSelected.isNullOrEmpty()){
                filterListener?.invoke(provinceSelected!! , locationSelected)
                dismiss()
            }else{
                binding.actvProvince.error = "Debe seleccionar por lo menos una provincia"
            }

        }


    }

    fun setFilterListener(listener: (String, String?) -> Unit){
        filterListener = listener
    }

    private fun setProvincesExposedDropdownMenu(listProvinces:ArrayList<Province>) {

        val items = arrayListOf<String>()
        listProvinces.forEach {
            items.add(it.name)
        }

        provinces = listProvinces
        val adapter = ArrayAdapter(requireContext(), R.layout.list_items_provinces, items)
        (binding.tfProvince.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setLocalitiesExposedDropdownMenu(listLocalities:ArrayList<Location>) {

        val items = arrayListOf<String>()
        listLocalities.forEach {
            items.add(it.municipality.name)
        }
        val cleanItems = items.distinct()
        val sortedItems = cleanItems.sortedBy { it }
        val adapter = ArrayAdapter(requireContext(), R.layout.list_items_localities, sortedItems)
        (binding.tfLocalities.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

}