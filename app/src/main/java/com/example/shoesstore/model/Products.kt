package com.example.shoesstore.model

data class Products(
    val id: String,
    var shoeName: String ="",
    var shoeCompany: String = "",
    var shoeSize:String = "",
    var shoeDescription: String= "",
    val price: Float,
    val images: List<String>,
    var category: String = ""
) {
    constructor() : this("0", "", "", "","",0f, emptyList(),"")
}