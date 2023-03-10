package com.example.shoesstore.model
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShoeDao {

    @Query("SELECT * FROM shoe_table ORDER BY id ASC")
    fun getAllShoes(): LiveData<List<ShoeListData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertShoe(shoeListData: ShoeListData)

    @Delete
    suspend fun deleteShoe(shoeListData: ShoeListData)

    @Query("DELETE FROM shoe_table")
    suspend fun deleteAll()

}