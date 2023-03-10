package com.example.shoesstore.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.shoesstore.R
import com.example.shoesstore.model.ShoeListData
import com.example.shoesstore.databinding.FragmentShoeDetailsBinding
import com.example.shoesstore.viewmodels.DataViewModel

class ShoeDetailsFragment : Fragment(){
     private lateinit var dataViewModel : DataViewModel
     private  var _binding: FragmentShoeDetailsBinding? = null
    private val binding get() = _binding!!
     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shoe_details, container,false)
        binding.shoeListDetails= ShoeListData()
        dataViewModel = ViewModelProvider(requireActivity())[DataViewModel::class.java]
        binding.cancelBtnDetails.setOnClickListener {
            findNavController().navigateUp()
          }
        binding.saveButton.setOnClickListener {
            saveDetail()
        }
        return binding.root

    }

     private fun saveDetail() {
         val bindingData = _binding?.shoeListDetails
         val shoeName = bindingData?.shoeName.toString()
         val shoeCompany = bindingData?.shoeCompany.toString()
         val shoeDescription = bindingData?.shoeDescription.toString()
         val shoeSize = bindingData?.shoeSize.toString()
         val images = bindingData?.images
         val price = bindingData?.shoePrice.toString()
         if (shoeName.isEmpty()|| shoeSize.isEmpty()|| shoeCompany.isEmpty()|| shoeDescription.isEmpty() || price.isEmpty()){

             Toast.makeText(context,"Please Fill The Empty Fields",Toast.LENGTH_SHORT).show()
         }else{
             Toast.makeText(context,"Saved",Toast.LENGTH_SHORT).show()
             dataViewModel.onSave(shoeName, shoeSize, shoeCompany, shoeDescription, images!!, price)
             findNavController().navigate(R.id.action_shoeDetailsFragment_to_shoeListFragment)
         }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}