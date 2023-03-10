package com.example.shoesstore.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoesstore.model.ShoeDatabase
import com.example.shoesstore.model.ShoeListData
import com.example.shoesstore.model.ShoeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataViewModel(application: Application) : AndroidViewModel(application) {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfDatabaseEmpty(shoeListData: List<ShoeListData>) {
        emptyDatabase.value = shoeListData.isEmpty()
    }


    private val shoeDao = ShoeDatabase.getDatabase(
        application
    ).shoeDao()

    private val repository: ShoeRepository = ShoeRepository(shoeDao)

    val getAllData: LiveData<List<ShoeListData>> = repository.getAllData

    fun insertData(shoeListData: ShoeListData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(shoeListData)
        }
    }

    fun deleteShoe(shoeListData: ShoeListData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteShoe(shoeListData)
        }
    }


    fun verifyData(shoeName: String ,shoeCompany: String ,shoeSize:String ,shoeDescription: String,shoePrice: String): Boolean {
        return shoeName.isNotEmpty() && shoeCompany.isNotEmpty() && shoeSize.isNotEmpty() && shoeDescription.isNotEmpty()  && shoePrice.isNotEmpty()
    }


}