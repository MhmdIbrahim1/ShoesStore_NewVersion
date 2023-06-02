package com.example.shoesstore.ui.ShoesList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.shoesstore.R
import com.example.shoesstore.databinding.FragmentShoeFavoriteBinding
import kotlinx.android.synthetic.main.fragment_shoe_favorite.img_home


class ShoeFavoriteFragment : Fragment() {
    private lateinit var binding: FragmentShoeFavoriteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding  = FragmentShoeFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // navigate to ShoeListFragment
        binding.imgHome.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_shoeInfoFragment_to_shoeListFragment)
        }
    }


}