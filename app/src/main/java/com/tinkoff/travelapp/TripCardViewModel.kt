package com.tinkoff.travelapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinkoff.travelapp.data.repository.Repository
import com.tinkoff.travelapp.model.route.Route
import com.tinkoff.travelapp.model.street.Street
import kotlinx.coroutines.launch
import retrofit2.Response

class TripCardViewModel: ViewModel() {

    var repository = Repository()
    val tripDataList: MutableLiveData<Response<Route>> = MutableLiveData()
    var Street: MutableLiveData<Response<Street>> = MutableLiveData()

//    fun getRoute() {
//        viewModelScope.launch {
//            tripDataList.value = repository.getRoute()
//        }
//    }

    fun getStreet() {
        viewModelScope.launch {
            Street.value = repository.getStreet()
        }
    }
}