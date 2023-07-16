package com.example.shoesstore.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shoesstore.R
//add the converter import
import androidx.room.TypeConverters
@Entity(tableName = "shoe_table")
//add the converter annotation
data class ShoeListData(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var shoeName: String ="",
    var shoeCompany: String = "",
    var shoeSize:String = "",
    var shoeDescription: String= "",
    var shoePrice: String = "",
    var shoeImageUri: String? = null, // new field for image

    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean = false

){
    constructor():this(0,"","","","","",null,false)
}
