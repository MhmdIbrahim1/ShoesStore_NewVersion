package com.example.shoesstore.model

import androidx.lifecycle.LiveData

class ShoeRepository(private val shoeDao: ShoeDao) {

    val getAllData: LiveData<List<ShoeListData>> = shoeDao.getAllShoes()

    suspend fun insertData(shoeListData: ShoeListData){
        shoeDao.insertShoe(shoeListData)
    }

    suspend fun deleteShoe(shoeListData: ShoeListData){
        shoeDao.deleteShoe(shoeListData)
    }

    suspend fun deleteAll(){
            shoeDao.deleteAll()
    }

    fun searchDatabase(searchQuery: String): LiveData<List<ShoeListData>> {
        return shoeDao.searchDatabase(searchQuery)
    }

    suspend fun updateShoe(shoeListData: ShoeListData){
        shoeDao.updateShoe(shoeListData)
    }
}
