package com.tinkoff.travelapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tinkoff.travelapp.data.repository.Repository
import com.tinkoff.travelapp.model.street.Street
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthorizationViewModel : ViewModel() {

    var repository = Repository()
    var Street: MutableLiveData<Response<Street>> = MutableLiveData()

    fun getStreet(auth: String) {
        viewModelScope.launch {
            Street.value = repository.getStreet(auth)
        }
    }
}
