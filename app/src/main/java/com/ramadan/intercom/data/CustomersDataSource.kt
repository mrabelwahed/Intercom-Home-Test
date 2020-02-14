package com.ramadan.intercom.data

import android.content.Context
import com.ramadan.intercom.business.model.Customer
import io.reactivex.Flowable
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject


class CustomersDataSource @Inject constructor(private val context: Context) {
    private val customers = ArrayList<Customer>()
    fun getCustomerList(): Flowable<List<Customer>> {
        return readCustomersAssetFile()
    }

    private fun readCustomersAssetFile(): Flowable<List<Customer>> {
        val filename = "customers.txt"
        val lineList = ArrayList<String>()
        try {
            val inputStream: InputStream = context.assets.open(filename)
            inputStream.bufferedReader().useLines { lines -> lines.forEach { lineList.add(it) } }
            lineList.forEach { addToCustomers((toJson(it))) }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Flowable.just(customers)
    }

    private fun toJson(str: String): JSONObject {
        return JSONObject(str)
    }

   private  fun addToCustomers(jsonObject: JSONObject) {
        customers.add(
            Customer(
                jsonObject.optInt("user_id"),
                jsonObject.optString("name"),
                jsonObject.optDouble("latitude"),
                jsonObject.optDouble("longitude")
            )
        )
    }
}