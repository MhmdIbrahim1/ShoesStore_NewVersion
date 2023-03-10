package com.example.shoesstore.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoesstore.model.ShoeListData

class DataViewModel(application: Application) : AndroidViewModel(application) {

    private var shoesList = mutableListOf<ShoeListData>()

    private var _shoeListLiveData =MutableLiveData<List<ShoeListData>>()

    val dataShoeList : LiveData<List<ShoeListData>>
            get() =_shoeListLiveData

    fun onSave(shoeName: String ,shoeCompany: String ,shoeSize:String ,shoeDescription: String,images: List<Int>, shoePrice: String){
        val newItem = ShoeListData(shoeName,shoeCompany,shoeSize,shoeDescription,shoePrice,images)
        newItem.let {
            shoesList.add(it)
            _shoeListLiveData.value = shoesList
        }
    }

}