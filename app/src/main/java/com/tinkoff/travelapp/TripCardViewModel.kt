package com.tinkoff.travelapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinkoff.travelapp.data.repository.Repository
import com.tinkoff.travelapp.model.route.Route
import kotlinx.coroutines.launch
import retrofit2.Response

class TripCardViewModel : ViewModel() {

    var repository = Repository()
    val tripDataList: MutableLiveData<Response<Route>> = MutableLiveData()

    fun getRoute(categories: List<String>, startTime: String, endTime: String, budget: Int) {
        viewModelScope.launch {
            tripDataList.value = repository.getRoute(categories, startTime, endTime, budget)
        }
    }
}