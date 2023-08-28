package com.example.tpingsoftware.di.repository

import com.example.tpingsoftware.ui.viewModels.AddServiceViewModel
import com.example.tpingsoftware.ui.viewModels.EditProfileViewModel
import com.example.tpingsoftware.ui.viewModels.ForgotPasswordViewModel
import com.example.tpingsoftware.ui.viewModels.HomeVIewModel
import com.example.tpingsoftware.ui.viewModels.LoginViewModel
import com.example.tpingsoftware.ui.viewModels.RegisterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelsModule = module {
    viewModel{ LoginViewModel(get(), androidContext()) }
    viewModel{ RegisterViewModel(get(), androidContext()) }
    viewModel{ HomeVIewModel(androidContext()) }
    viewModel{ ForgotPasswordViewModel(get(), androidContext()) }
    viewModel{ EditProfileViewModel(get(), androidContext()) }
    viewModel{ AddServiceViewModel(get(), androidContext()) }
}