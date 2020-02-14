package com.ramadan.intercom.di

import android.app.Application
import android.content.Context
import com.ramadan.intercom.data.CustomersDataSource
import dagger.Module
import dagger.Provides

@Module
class CustomersDataSourceModule(private val context: Context) {

    @Provides
    fun provideCustomersDataSource()= CustomersDataSource(context)
}