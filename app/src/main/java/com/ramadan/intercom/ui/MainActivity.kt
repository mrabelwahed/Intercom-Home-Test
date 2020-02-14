package com.ramadan.intercom.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ramadan.intercom.R
import com.ramadan.intercom.business.model.Customer
import com.ramadan.intercom.business.usecase.FindNearCustomersUsecase
import com.ramadan.intercom.di.CustomersDataSourceModule
import com.ramadan.intercom.di.DaggerMainComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var text = ""

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appComponent = DaggerMainComponent.builder()
            .customersDataSourceModule(CustomersDataSourceModule((applicationContext)))
            .build()

        val dataSource = appComponent.getCustomersDataSource()

        FindNearCustomersUsecase(dataSource).execute(100000)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { res ->
                setData(res)
            }

    }

    private fun setData(list: List<Customer>) {
        list.sortedBy { it.id }.forEach {
            text += "name= ${it.name} and id= ${it.id} \n"
        }

        customersText.text = text
    }
}
