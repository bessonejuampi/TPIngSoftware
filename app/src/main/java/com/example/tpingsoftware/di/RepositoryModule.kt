package com.example.tpingsoftware.di

import com.example.tpingsoftware.di.repository.MainRepository
import com.example.tpingsoftware.di.repository.MainRepositoryContract
import org.koin.dsl.module

val RepositoryModule = module {
    factory<MainRepositoryContract> { MainRepository() }
}