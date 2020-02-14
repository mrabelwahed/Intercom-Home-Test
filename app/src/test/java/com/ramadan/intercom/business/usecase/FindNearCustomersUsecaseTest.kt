package com.ramadan.intercom.business.usecase

import com.ramadan.intercom.RxSchedulerRule
import com.ramadan.intercom.business.model.Customer
import com.ramadan.intercom.data.CustomersDataSource
import io.reactivex.Flowable
import io.reactivex.subscribers.TestSubscriber
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import java.util.concurrent.TimeUnit

class FindNearCustomersUsecaseTest {

    @Rule
    @JvmField
    var mockitoRule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var testSchedulerRule = RxSchedulerRule()

    @Mock
    lateinit var dataSource: CustomersDataSource

    private val testSubscriber = TestSubscriber<List<Customer>>()
    @Mock
    lateinit var customersFlowable: Flowable<List<Customer>>

    lateinit var usecase: FindNearCustomersUsecase


    val dummyCustomers = ArrayList<Customer>()

    @Before
    fun setup() {
        usecase = FindNearCustomersUsecase(dataSource)
        dummyCustomers.add(Customer(12, "Christina McArdle", 52.986375, -6.043701))
        dummyCustomers.add(Customer(18, "Bob Larkin", 52.228056, -7.915833))
    }


    @Test
    fun usecase_is_ready_for_test() {
        Assert.assertNotNull(usecase)
    }


    @Test
    fun should_call_CustomerDataSource_when_FindNearCustomersUsecase_called_and_return_customers() {
        `when`(dataSource.getCustomerList()).thenReturn(customersFlowable)
        val result = dataSource.getCustomerList()
        assertThat(result, `is`(customersFlowable))
        verify(dataSource).getCustomerList()
        verifyNoMoreInteractions(dataSource)
    }

    @Test
    fun should_filter_customers_within_100km() {
        `when`(dataSource.getCustomerList()).thenReturn(Flowable.just(dummyCustomers))
        usecase.getNearbyCustomers(100)
            .test()
            .awaitDone(2, TimeUnit.SECONDS)
            .assertNoErrors()
            .assertComplete()
            .assertValueCount(1)
    }
}


