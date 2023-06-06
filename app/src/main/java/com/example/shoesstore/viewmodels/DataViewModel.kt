package com.example.shoesstore.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoesstore.model.ShoeDatabase
import com.example.shoesstore.model.ShoeListData
import com.example.shoesstore.model.ShoeRepository
import com.example.shoesstore.model.UserModel.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataViewModel(application: Application) : AndroidViewModel(application) {
    private val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)
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

    fun deleteAll(){
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteAll()
            }
        }

    fun searchDatabase(searchQuery: String): LiveData<List<ShoeListData>> {
        return repository.searchDatabase(searchQuery)
    }

    fun verifyData(shoeName: String ,shoeCompany: String ,shoeSize:String ,shoeDescription: String,shoePrice: String): Boolean {
        return shoeName.isNotEmpty() && shoeCompany.isNotEmpty() && shoeSize.isNotEmpty() && shoeDescription.isNotEmpty()  && shoePrice.isNotEmpty()
    }

    // Setter method for the adapter

    fun updateShoe(shoeListData: ShoeListData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateShoe(shoeListData)

        }
    }
}