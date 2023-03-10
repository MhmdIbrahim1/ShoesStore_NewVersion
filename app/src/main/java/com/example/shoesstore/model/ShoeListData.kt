package com.example.shoesstore.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shoesstore.R
//add the converter import
import androidx.room.TypeConverters
@Entity(tableName = "shoe_table")
//add the converter annotation
@TypeConverters(Converters::class)
data class ShoeListData(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var shoeName: String ="",
    var shoeCompany: String = "",
    var shoeSize:String = "",
    var shoeDescription: String= "",
    var shoePrice: String = "",
   // val images: Int = R.drawable.shoe_1
)
