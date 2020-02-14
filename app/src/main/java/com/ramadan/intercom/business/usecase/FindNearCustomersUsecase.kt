package com.ramadan.intercom.business.usecase

import com.ramadan.intercom.business.model.Customer
import com.ramadan.intercom.data.CustomersDataSource
import io.reactivex.Flowable
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class FindNearCustomersUsecase @Inject constructor(private val dataSource: CustomersDataSource) :
    UseCase<Long, List<Customer>> {
    override fun execute(param: Long): Flowable<List<Customer>> {
        return getNearbyCustomers(param)
    }

    private fun isNearby(customer: Customer): Boolean {

        var R = 6371e3 // metres
        var φ1 = OFFICE_LAT.toRadians()
        var φ2 = customer.lat.toRadians()
        var Δφ = (customer.lat - OFFICE_LAT).toRadians()
        var Δλ = (customer.lng - OFFICE_LONG).toRadians()

        var a = sin(Δφ / 2) * sin(Δφ / 2) +
                cos(φ1) * cos(φ2) *
                sin(Δλ / 2) * sin(Δλ / 2)
        var c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c <= 100e3
    }

    public fun getNearbyCustomers(radius: Long): Flowable<List<Customer>> {
        return dataSource.getCustomerList()
            .flatMapIterable { it }
            .filter { isNearby(it) }
            .toList()
            .toFlowable()
    }


    companion object {
        const val OFFICE_LAT = 53.339428
        const val OFFICE_LONG = -6.257664
    }


    private fun Double.toRadians(): Double = Math.toRadians(this)


}