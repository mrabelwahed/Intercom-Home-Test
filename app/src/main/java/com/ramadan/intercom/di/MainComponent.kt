package com.ramadan.intercom.di

import android.app.Activity
import android.app.Application
import com.ramadan.intercom.data.CustomersDataSource
import com.ramadan.intercom.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CustomersDataSourceModule::class])
interface MainComponent {
    fun getCustomersDataSource():CustomersDataSource
}