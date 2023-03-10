package com.example.shoesstore.ui

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shoesstore.R
import com.example.shoesstore.model.ShoeListData
import com.example.shoesstore.databinding.FragmentShoeDetailsBinding
import com.example.shoesstore.viewmodels.DataViewModel
import kotlinx.android.synthetic.main.list_view.imageView3

class ShoeDetailsFragment : Fragment() {
//    private val IMAGE_PICK_REQUEST_CODE = 1
    private val mDataViewModel: DataViewModel by viewModels()
    private var _binding: FragmentShoeDetailsBinding? = null
    private lateinit var newData: ShoeListData // Declare newData here
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentShoeDetailsBinding.inflate(layoutInflater, container, false)
        binding.cancelBtnDetails.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.saveButton.setOnClickListener {
            insertDataToDb()
        }

        return binding.root

    }
        private fun insertDataToDb() {
            val mName = binding.nameEt.text.toString()
            val mSize = binding.sizeEt.text.toString()
            val mCompany = binding.companyEt.text.toString()
            val mDescription = binding.descriptionEt.text.toString()
            val mPrice = binding.shoePriceEt.text.toString()

            val validation =
                mDataViewModel.verifyData(mName, mSize, mCompany, mDescription, mPrice)
            if (validation) {
                // Insert Data to Database
                newData = ShoeListData(0, mName, mSize, mCompany, mDescription, mPrice)
                mDataViewModel.insertData(newData)
                Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
                // Navigate Back
                findNavController().navigate(R.id.action_shoeDetailsFragment_to_shoeListFragment)
            } else {
                Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }


