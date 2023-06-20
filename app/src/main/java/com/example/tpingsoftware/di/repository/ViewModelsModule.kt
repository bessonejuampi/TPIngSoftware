package com.example.tpingsoftware.di.repository

import com.example.tpingsoftware.ui.viewModels.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelsModule = module {
    viewModel{ LoginViewModel() }
}