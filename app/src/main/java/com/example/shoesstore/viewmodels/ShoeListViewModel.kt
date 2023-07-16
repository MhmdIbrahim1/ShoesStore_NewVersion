package com.example.shoesstore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoesstore.model.Products
import com.example.shoesstore.util.NetworkResult
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoeListViewModel @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : ViewModel() {

    private val _bestProducts =
        MutableStateFlow<NetworkResult<List<Products>>>(NetworkResult.UnSpecified())
    val bestProducts = _bestProducts as StateFlow<NetworkResult<List<Products>>>


    private val pagingInfoBestProduct = PagingInfo()

    init {
        fetchBestProducts()
    }

     fun fetchBestProducts() {
        fetchProductsByCategory(null, pagingInfoBestProduct, _bestProducts)
    }

    private fun fetchProductsByCategory(
        category: String?,
        pagingInfo: PagingInfo,
        resultFlow: MutableStateFlow<NetworkResult<List<Products>>>
    ) {
        if (!pagingInfo.isPagingEnd) {
            viewModelScope.launch {
                resultFlow.emit(NetworkResult.Loading())
            }
            val query = if (category != null) {
                firebaseFirestore.collection("products")
                    .whereEqualTo("category", category)
            } else {
                firebaseFirestore.collection("products")
            }
            query.limit(pagingInfo.page * 10)
                .get()
                .addOnSuccessListener { result ->
                    val productList = result.toObjects(Products::class.java)
                    pagingInfo.isPagingEnd = productList == pagingInfo.oldData
                    pagingInfo.oldData = productList
                    viewModelScope.launch {
                        resultFlow.emit(NetworkResult.Success(productList))
                    }
                    pagingInfo.page++
                }
                .addOnFailureListener { exception ->
                    viewModelScope.launch {
                        resultFlow.emit(NetworkResult.Error(exception.message.toString()))
                    }
                }
        }

    }

}

internal data class PagingInfo(
    var page: Long = 1,
    var oldData: List<Products> = emptyList(),
    var isPagingEnd: Boolean = false
)